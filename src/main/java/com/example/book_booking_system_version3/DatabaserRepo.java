package com.example.book_booking_system_version3;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaserRepo {


    private final Logger logger = LoggerFactory.getLogger(Repository.class);
    BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(15);

    @Autowired
    private JdbcTemplate db;



    //This is the save function: makes it possible to save the object created from the Books table, and then saving it inside
    //the Books table (in the database).

    //STEP 1
    public void saveBooks(Books inbook){
        String sql = "INSERT INTO Books(books, image_url, datefrom, dateto) VALUES (?,?, ?, ?)";

//Now we'll insert it into the db
        db.update(sql, inbook.getBooks(), inbook.getImage_url(), inbook.getDatefrom(), inbook.getDateto());


    }



    //Function to take out all objects from the Books table and saving them inside an list, afterwards these objects
    //Are written out in a for loop (in the method below this) and then saved inside the Books table from the function in step 1
    public List<Books> BringoutPremadeBooks() {
        String sql = "SELECT * FROM Books ORDER BY books"; //Take out all objects
        List<Books> allBooks = db.query(sql, new BeanPropertyRowMapper<>(Books.class)); //Place all objects from the table inside here
        return allBooks;
    }


















    //STEP 2
    public List<Books> bringAllBooks(){
String sql = "SELECT * FROM Books ORDER BY books";

List<Books> allBooks = db.query(sql, new BeanPropertyRowMapper<>(Books.class));

return allBooks;
    }




    //Note: userExists is refered to a user (username + password) that is saved inside the database after creation
    //fields is refered to the username field and password field on the login/creation site

    //For example fields.getUsername refers to the value the client types inside the username field in the create/login site

    //STEP 3 --> User login/registration


    //This compares the password written inside the password field with the encrypted password inside the database
    public boolean sjekkPassord(String password, String hashPassord) {
        return bCrypt.matches(password, hashPassord);}


    public boolean login(User fields) {
        String sql = "SELECT * FROM User WHERE email = ?";

        try {
            User userExists = db.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class), fields.getEmail());

            if (userExists != null) {
                // Check if the provided password matches the stored hashed password
                if (!fields.getPassword().isEmpty() && sjekkPassord(fields.getPassword(), userExists.getPassword())) {
                    return true; // Both username and password are correct
                }
            }
            return false; // Either username is wrong or password is empty/wrong
        } catch (Exception e) {
            logger.error("Feil med login() " + e);
            return false;
        }
    }





    //This will be used to encrypt the password which a user creates before it is saved inside the database encrypted
    public String krypterPassord(String password) {
        return bCrypt.encode(password);}


    //Creation --> still STEP 3
    public boolean create(User fields) {
        String sql1 = "SELECT COUNT(*) FROM User WHERE email = ?";

        String sql2 = "INSERT INTO User (email, password) VALUES (?, ?)";

            int antallBrukereInniDatabasenMedSammeBrukernavn = db.queryForObject(sql1, Integer.class, fields.getEmail());

            try {

                //Check if user already exists, if yes return false (don't create user)
                if (antallBrukereInniDatabasenMedSammeBrukernavn > 0) {
                    return false;
                }

             // Checks if the regex validation is valid or invalid (if invalid --> don't create user by returning false
               if (!regexValidation(fields)) {
                    return false;
               }


                else {

                    //Hvis ikke noen har samme brukernavn --> oppdater databasen med ny bruker
                    db.update(sql2, fields.getEmail(), krypterPassord(fields.getPassword()));
                    return true;
                }
            } catch (Exception e) {
                logger.error("Error with creation of user " + e);
                return false;
            }
        }




//STEP 4 - REGEX INPUT VALIDATION (CONTINATION LIKE IN THE FRONTEND)

    public boolean regexValidation(User user){
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String regexPassword = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}";

        boolean emailOK = user.getEmail().matches(regexEmail);
        boolean passwordOK = user.getPassword().matches(regexPassword);

        return emailOK && passwordOK;
    }










    //STEP 5 - Method to delete all books from the database
  public void deleteAllBooks(){
String sql = "DELETE FROM Books";
db.update(sql);
  }


}
