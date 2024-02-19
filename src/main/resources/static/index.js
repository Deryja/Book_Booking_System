


$(() => {
    // Get the image URL from the query parameter
    const params = new URLSearchParams(window.location.search); //Sets the image url inside here (after it was sent from allebøker.js)
    const imageUrl = params.get('image_url'); //Bring out the url from the variable params above

    const bookTitle = params.get('book_title'); //Also bring out the title of the book

    // If image URL is present, display it
    if (imageUrl) {

        // Creating a img element because we didn't do it in the html code
        let img = document.createElement('img');
        img.src = imageUrl; //Give the specific url to the image element to display it

        // Append the image to the div element (the place it shall be displayed in)
        $('#bokBildetSittOmråde').append(img);

    }


    // If book title is present, display it
    if (bookTitle) {
        // Set the book title in the bookTitle div
        $('#bookTitle').text(bookTitle);
    }









    // The button to book the specific book that is selected
    $("#order").click(() => {

        //Date --> Added this lastly after successfully being able to order with the book title and link
        const DATEFROM = $("#dateFROM").val();
        const DATETO = $("#dateTO").val();



        // Get today's date in order to make it impossible to order before in time
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Set hours, minutes, seconds, and milliseconds to zero for comparison


        //Making it into a date so it is possible to compare them as greater or lesser than each other, and to compare it with today's date
        const fromDate = new Date(DATEFROM);
        const toDate = new Date(DATETO);


        // Makes it impossible to book before today's date
        if (fromDate < today || toDate < today) {
            $("#feilmeldingDato").html("Date from and to can't be lesser than today's date")
            return false; //Exists function
        }
        else{
            $("#feilmeldingDato").html("")
        }



        //Makes it impossible to book if datefrom is bigger than dateafter, or dateafter is lesser than datebefore
        if (fromDate > toDate || toDate < fromDate){
            $("#feilmeldingDato").html("Date from can't be higher than date to")
            return false; //Exists function
            }
        else{
            $("#feilmeldingDato").html("")
        }








        //Since we use ajax as API holder this time, we need a RequestBody in the controller for the saving function

        // Send a POST request to save the selected book's information (because we send this to the saveBooks function, which in turns has PostMapping to save it inside the database)


        if (DATEFROM && DATETO){ //This if sentence is to prevent an order being made if the date fields are empty
        $.ajax({
            url: '/book-api', //The API name inside the backend
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ books: bookTitle, image_url: imageUrl, datefrom: DATEFROM, dateto: DATETO}), //The "books" and "image_url" must be the same as the variables used for the PREMADEBOOKS table and the PreMadeBook class in the backend. And the text after f.eks imageUrl is the value that will be saved inside the database
            success: function() {
                console.log('Book ordered successfully');


//If ordered successfully, then an another get API will be called which I made inside the backend in order to bring out
//All ordered books from the database, and then display them inside a bootstrap table.

                $.get("/showbooks", bookList => formatbookList(bookList))

                const formatbookList = bookList => {
                    let output = "<table class='table'>";

                    // Add table header row
                    output += "<thead><tr><th>Book title</th><th>Date-from</th><th>Date-to</th></tr></thead>";

                    // Add table body
                    output += "<tbody>";

                    // Add table data rows
                    for (let book of bookList) {
                        output += "<tr class='table-row'>";
                        output += "<td>" + book.books + "</td>"; // Book title
                        output += "<td>" + book.datefrom + "</td>"; // Date from
                        output += "<td>" + book.dateto + "</td>"; // Date to
                        output += "</tr>";

                        // Add a spacer row
                        output += "<tr class='spacer-row'><td colspan='2'></td></tr>";
                    }

                    output += "</tbody>";
                    output += "</table>";

                    $("#AllBooks").html(output);
                }



            },
            error: function() {
                console.error('Failed to order book');
            }
        })}
        else {
            $("#feilmeldingDato").html("Date can't be empty")
            return false;
        };
    });
});


//The difference here in contrast to the earlier API post and get calls from the other javascript files like the create.js and login.js
//Is that this time there are no object I make myself by refering to a specific field inside the index.html file and then
//Creating a object based on the values that are manually set inside the fields. So this time an ajax that just generates
//and JSON object for us by stringifying it based on the variables/values from the logic we have put inside the javascript file
//And then posting it to the saveBooks function in the backend which will save this object inside the database for all orders
// (which I have just called Books).
//It is therefore important to use RequestBody inside the saveBooks function in the backend to ensure that the Book object is correctly constructed from the JSON
//Otherwhise for the other type of post and get calls to the database, a requestbody is not needed (it's only needed for an ajax)



//SHORTLY SUMMARIZED:
//If we have no input fields to manually make objects from, we can use ajax to make a JSON object by the values inside the javascript file
//The JSON object is a stringified object, hence a RequestBody is needed inside the method in the backend. And the syntax of the ajax is somehow different from the syntax of earlier post/get requests to the backend
//WITHOUT A REQUESTBODY IN THE BACKEND THIS WILL NOT WORK!


//  //PREVIOUSLY I INSERTED THREE BOOKS DIRECTLY INSIDE THE , BUT SINCE IT WASN'T DYNAMIC AND SUITABLE FOR THIS PROJECT, I CHANGED IT TO THIS: INSERT INTO PREMADEBOOKS(books, image_url) VALUES('?', '?');










//Button to delete all booked books:
$("#deleteAll").click(() => {
    $.ajax("/apiDelete", {
        type: "DELETE",
        success: () => $("#AllBooks").html(""), //Set the table to an empty string when the delete button is clicked in order to reset the table
        error: (jqXhr, textStatus, errorMessage) => console.log(errorMessage)
    })
});





//STEP 6 --> logout button --> Makes the session offline
$(() => {
    $("#logout").click(() => {

        $.get("/logout", function (){
            //I can use either reload here or direct location to the login page, both is fine (since the default is login page anyways when the attribute of session is not loggedIn)

            window.location.reload(); //The button makes the session offline, and this line here reloads the website after the session is offline
            //And it will automatically go back to the login page as I have made it the default page (if someone is not logged in)
        })
    })
})