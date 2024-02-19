

// Handle click events on book cards
$(() => {
    $(".card").click((event) => {

        let bookTitle = $(event.currentTarget).find('.card-text h2').text();



        // Get the data-img attribute value of the clicked book (the url inside it) and make it into a variable (let imgUrl)
        let imgUrl = $(event.currentTarget).data('img');

        // Redirect to index.html with the selected image URL variable as a query parameter
      //The bookTitle was made in the same way
        window.location.href = `index.html?image_url=${encodeURIComponent(imgUrl)}&book_title=${encodeURIComponent(bookTitle)}`;

    });
});







//STEP 6 --> logout button --> Makes the session offline
$(() => {
    $("#logout").click(() => {

        $.get("/logout", function (){
            window.location = "/login.html";; //The button makes the session offline, and this line here reloads the website after the session is offline
            //And it will automatically go back to the login page as I have made it the default page (if someone is not logged in)
        })
    })
})