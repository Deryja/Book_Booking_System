package com.example.book_booking_system_version3;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class Controller {

    @Autowired

    private DatabaserRepo rep;


    @Autowired
    private HttpSession session;
    private Logger logger = LoggerFactory.getLogger(Controller.class);



    //Without the RequestBody code, the specific book a user wants to order wont be saved inside the database,
    //Because it ensures that the Book object is correctly constructed from the JSON
    //STEP 1
    @PostMapping("/book-api")
    public void saveBooks(@RequestBody Books inbook) {

        //The rest of the logic for this function is written in the repository, hence we just refer to it:
        rep.saveBooks(inbook);
    }






    //PREVIOUSLY I INSERTED THREE BOOKS DIRECTLY INSIDE THE data.sql, BUT SINCE IT WASN'T DYNAMIC AND SUITABLE FOR THIS PROJECT, I CHANGED IT TO THIS: INSERT INTO PREMADEBOOKS(books, image_url) VALUES('?', '?');

//EXCLUSIVE FOR THIS VERSION 2 PROJECT: --> Bring out the books from the PREMADEBOOKS table and place them inside "Books" table
    //upon order
    //FOR VERSION 2 BELOW:


    //Function to take out all objects from the PREMADEBOOKS table and saving them inside an list, afterwards these objects
    //Are written out in a for loop (in the method below this) and then saved inside the Books table from the function in step 1
    @GetMapping("/getAllPreMadeBooks")
    public List<Books> getAllInsertPremadeBooksInsideBooksAfterOrder() {


        List<Books> books = rep.BringoutPremadeBooks(); //All the objects that was taken out from the table
        //Are saved inside this list


        savePreMadeBooksAsBooks(books); //Call on the method below to make it functional
        // which takes out all the objects from the list above and saves them inside "Books" table by saving them inside the function we made

        //Return the list of objects from the Books table that are saved locally inside a list
        return books;
    }


    //Writes out all the objects from the Books table that were saved inside a list above (saving them locally), and then
    //Saves those specific objects inside the "Books" table by inserting them inside the saveBooks function
    private void savePreMadeBooksAsBooks(List<Books> books) {
        for (Books books1 : books) {
            Books book = new Books(books1.getBooks(), books1.getImage_url(), books1.getDatefrom(), books1.getDateto());

            rep.saveBooks(book);
        }}




//FOR VERSION 2 ABOVE
















    //STEP 2
    @GetMapping("/showbooks")
    public List<Books> showBooksList() {

        List<Books> MyList = rep.bringAllBooks();

        return MyList;
    }


    //STEP 3 --> User login/registration


    //LOGIN
    @GetMapping("/login")
    public boolean login(User fields, HttpServletResponse response) throws IOException {
        if (rep.login(fields)) {

            session.setAttribute("LoggedIn", fields.getEmail()); //Store username in session
            return true;
        }
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Wrong email or password");
        return false;
    }


    // Confirm if the user is logged in
    @GetMapping("/confirmLogin")
    public boolean confirmLogin() {
        return session.getAttribute("LoggedIn") != null;
    }


    //Restricting the index.html to only be seen if the person is logged in, and if not --> the person will be redirected to login page
    @GetMapping("/index.html")
    public void serveIndexPage(HttpServletResponse response) throws IOException {
        if (confirmLogin()) {
            // User is logged in, serve the index.html content
            // Assuming index.html is a static file in your resources folder
            Resource resource = new ClassPathResource("/static/index.html");

            response.setContentType(MediaType.TEXT_HTML_VALUE);

            FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());



        } else {
            // User is not logged in, redirect to login page
            response.sendRedirect("/login.html");
        }
    }


    //Restricting the allebøker.html to only be seen if the person is logged in, and if not --> the person will be redirected to login page
    @GetMapping("/allebøker.html")
    public void serveAllebøkerPage(HttpServletResponse response) throws IOException {
        if (confirmLogin()) {
            // User is logged in, serve the index.html content
            // Assuming index.html is a static file in your resources folder
            Resource resource2 = new ClassPathResource("/static/allebøker.html");

            response.setContentType(MediaType.TEXT_HTML_VALUE);

            FileCopyUtils.copy(resource2.getInputStream(), response.getOutputStream());



        } else {
            // User is not logged in, redirect to login page
            response.sendRedirect("/login.html");
        }
    }








    //CREATION of user account --> still STEP 3

    @PostMapping("/createUser")
    public boolean create(User user, HttpServletResponse response) throws IOException {


        if (!rep.create(user)) {

            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User already exists, or the username and password is too weak, try again.");
            return false; //The user is not created and not saved inside the database because it either already exists, or regex is invalid

        }



        return true; //The user is created and successfully saved inside the database



    }





    //STEP 5 - deleting all booked books
    @DeleteMapping("/apiDelete")

    public void deleteAllBooks() {
        rep.deleteAllBooks();
    }




//STEP 6 - logout
@GetMapping("/logout")
public void logout() {
 //We remove the logged in attribute which means the client gets logged out from his account by clicking on the button
    session.removeAttribute("LoggedIn");


}

}