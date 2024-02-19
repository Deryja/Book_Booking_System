package com.example.book_booking_system_version3;
public class PreMadeBook{



    private String books;
    private String image_url;
    private String datefrom;
    private String dateto;


    public PreMadeBook(String books, String image_url, String datefrom, String dateto) {
        this.books = books;
        this.image_url = image_url;
        this.datefrom = datefrom;
        this.dateto = dateto;

    }

    public PreMadeBook() {}

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(String datefrom) {
        this.datefrom = datefrom;
    }

    public String getDateto() {
        return dateto;
    }

    public void setDateto(String dateto) {
        this.dateto = dateto;
    }

}
