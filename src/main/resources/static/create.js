//Step 3 - user creation

function create() {

    const email = $("#email");
    const password = $("#password");


    const User = {
        email: email.val(),
        password: password.val()
    }


if (validerEmail() && validerPassword){
    if (inputvalidation(User)) {
        $.post("/createUser", User, function (data) {
            if (data) {
                window.location.href = "login.html";
            }
        }).fail(function (jqXHR) {
            const json = $.parseJSON(jqXHR.responseText);
            $("#wrongCreate").html(json.message);
            $("#create").html('create');
        });
    }}



    if (!User.username ){
        console.log("Wrong input")
        document.getElementById("fieldforemail").innerHTML = "Write in email";}
    else {
        document.getElementById("fieldforemail").innerHTML = "";}

    if (!User.password){
        document.getElementById("fieldforpassword").innerHTML = "Write in password";}
    else {
        document.getElementById("fieldforpassword").innerHTML = "";}


    //This if sentence is used to show the word "loading" if the username and password aren't empty, and if they are strong enough
    if (User.email && User.email && validerEmail() && validerPassword()){
        $("#create").html('User creation successful, redirecting to login...')
    }

}



//To check if the fields aren't empty
const inputvalidation = User => {
    if (User.email === "") return false
    else return User.password !== "";
}





//STEP 4 --> REGEX VALIDATION. This is only the frontend part of regex where error messages will be shown, and no redirection
//Will happen to the login page if the username or password are too weak. However this also needs to be done
//In the backend because this wont





//Input validation for regex --> having proper username and password
// The regex for username when creating a user: /^[a-zA-Z0-9_-]{3,16}$/;
// The regex for password when creating a user: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;

function validerEmail() {

    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    const Email = $("#email").val();

    const EmailOK = regexEmail.test(Email);
    if (!EmailOK) {
        $("#fieldforemail").html("Invalid email, please write a proper one")
        return false;
    } else {
        $("#fieldforemail").html("")
        return true;
    }
}


    function validerPassword(){

        const regexPassword =  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
        const Password = $("#password").val();

        const passwordOK = regexPassword.test(Password);
        if (!passwordOK){
            $("#fieldforpassword").html("The password is too weak, must be 6-20 characters long and contain at least one digit, one lowercase letter, and one uppercase letter.")
            return false;}

        else {
            $("#fieldforpassword").html("")
            return true;}


//The regex for the backend continues in the Controller.java class



}