
//Step 3
function login() {

    const email = $("#email");
    const password = $("#password");



    const User = {
        email: email.val(),
        password: password.val()
    }



    //If data = if a person accessed the index.html with the data of a username + password from the database, if not then
    //A html will popup that says "You need to log in"

    if (inputvalidation(User)){ //User er det jeg skriver inn i inputfield, og data er infoen inni databasen som sammenlignes
    $.get("/login", User, function(data) {
        if(data) {

         //If user is not logged in, clicking on "log in" won't navigate to index.html
            $.get("/confirmLogin", function(loggedIn) { //data over --> Så skal jeg med get request endre session attributten til loggedIn
                if(loggedIn) {
            window.location.href = "allebøker.html";
        } else {
            $("#wronglogin").html("You must log in");
                }
            });
        }

            else {
                    $("#wronglogin").html("Wrong username or password");
                }

    }).fail(function(jqXHR) {
        const json = $.parseJSON(jqXHR.responseText);
        $("#wronglogin").html(json.message);
        $("#login").html("Logg inn");
    });


    }
    if (!User.email ){
        console.log("Wrong input")
        document.getElementById("fieldforemail").innerHTML = "Write in email";}
    else {
        document.getElementById("fieldforemail").innerHTML = "";}

    if (!User.password){
        document.getElementById("fieldforpassword").innerHTML = "Write in password";}
    else {
        document.getElementById("fieldforpassword").innerHTML = "";}


    if (User.email && User.password){
        $("#login").html('Loading...')
    }




}





const inputvalidation = User => {
    if (User.email === "") return false
    else return User.password !== "";
}








//To jump to create user if you want
function ChangeToCreateUser() {
    window.location.href = "create.html";
}