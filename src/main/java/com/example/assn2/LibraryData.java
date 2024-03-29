package com.example.assn2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "library-data", value = "/library-data")
public class LibraryData extends HttpServlet {



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
                out.println("<table>");
                out.println("<tr><th>Title</th><th>ISBN</th><th>Edition Number</th><th>Authors</th></tr>");

                for (Book book : bookList) {
                    out.println("<tr>");
                    out.println("<td>" + book.getTitle() + "</td>");
                    out.println("<td>" + book.getIsbn() + "</td>");
                    out.println("<td>" + book.getEdNumber() + "</td>");
                    out.println("<td>");
                    for (Author author : book.getAuthors()) {
                        out.println(author.getAuthorFirstName() + " " + author.getAuthorLastName() + "<br>");
                    }
                    out.println("<p>---------------------</p>");
                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
//                RequestDispatcher requestDispatcher = request.getRequestDispatcher("viewallbooks.jsp");
//                request.setAttribute("booklist", bookList);
//                requestDispatcher.forward(request, response);
            } else if(view.equals("authors")){
                out.println("<table>");
                out.println("<tr><th>Author</th><th>Books</th></tr>");

                for (Author author : authorList) {
                    out.println("<tr>");
                    out.println("<td><h1>" + author.getAuthorFirstName() + " " + author.getAuthorLastName() + "   ID:" + author.getAuthorID() + "</h1></td>");
                    out.println("<td>");
                    for (Book book : author.getBooks()) {
                        out.println("<h2>" + book.getTitle() + "</h2>");
                    }
                    out.println("<p>---------------------</p>");
                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
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
                DatabaseManager.addAuthorISBN(request.getParameter("isbn"), Integer.parseInt(request.getParameter("author_id")));
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