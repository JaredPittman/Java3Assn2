package com.example.assn2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "library-data", value = "/library-data")
public class Servlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        String view = request.getParameter("view");

        ArrayList<Book> bookList;
        ArrayList<Author> authorList;

        try {
            System.out.println(view);
            bookList = DatabaseManager.getBooks();
            authorList = DatabaseManager.getAuthors();
            DatabaseManager.createAuthorsBookList(bookList, authorList, DatabaseManager.getAuthorISBN());
            DatabaseManager.createBooksAuthorList(bookList, authorList, DatabaseManager.getAuthorISBN());
            if(view.equals("books")){
                for (Book book : bookList) {
                    out.println("<h1>" + book.getTitle() + "</h1>");
                    out.println("<h2>" + book.getIsbn() + "</h2>");
                    out.println("<h2>" + book.getEdNumber() + "</h2>");
                    for (Author author : book.getAuthors()) {
                        out.println("<h3>" + author.getAuthorFirstName() + " " + author.getAuthorLastName() + "</h3>");
                    }
                }
//                RequestDispatcher requestDispatcher = request.getRequestDispatcher("viewallbooks.jsp");
//                request.setAttribute("booklist", bookList);
//                requestDispatcher.forward(request, response);
            } else if(view.equals("authors")){
                for (Author author : authorList) {
                    out.println("<h1>" + author.getAuthorFirstName() + " " + author.getAuthorLastName() + "</h1>");
                    for (Book book : author.getBooks()) {
                        out.println("<h2>" + book.getTitle() + "</h2>");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String view = request.getParameter("view");

        if(view.equals("add_book")){
            try{
                Book book = new Book();
                book.setIsbn(request.getParameter("isbn"));
                book.setTitle(request.getParameter("title"));
                book.setEdNumber(Integer.valueOf(request.getParameter("edition_number")));
                book.setCopyright(request.getParameter("copyright"));
                DatabaseManager.addBook(book);
                DatabaseManager.addAuthorISBN(request.getParameter("isbn"), request.getIntHeader("author_id"));
                out.println("Book added successfully");
                out.println("<a href='index.jsp'>Back</a>");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else if(view.equals("add_author")){
            try{
                Author author = new Author();
                author.setAuthorID(request.getIntHeader("author_id"));
                author.setAuthorFirstName(request.getParameter("first_name"));
                author.setAuthorLastName(request.getParameter("last_name"));
                DatabaseManager.addAuthor(author);
                out.println("Author added successfully");
                out.println("<a href='index.jsp'>Back</a>");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}