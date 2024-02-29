package com.example.assn2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a DatabaseManager object
 */
public class DatabaseManager {

    static final String DB_URL = "jdbc:mariadb://127.0.0.1:3308";
    static final String USER = "root";
    static final String PASS = "lpbroot";
//    static final String JAVA_BOOKS_DB_URL = "jdbc:mariadb://127.0.0.1:3308/books?user=root&password=root";
    static final String JAVA_BOOKS_DB_URL = "jdbc:mariadb://127.0.0.1:3308/books?user=root&password=lpbroot";
    static final String QUERY_ALL_AUTHORS = "SELECT * FROM authors;";
    static final String QUERY_ALL_TITLES = "SELECT * FROM titles;";
    static final String QUERY_ALL_AUTHORISBN = "SELECT * FROM authorISBN;";

    /**
     * This method returns a list of authors
     * @return
     * @throws SQLException
     */
    public static ArrayList<Author> getAuthors() throws SQLException {
        Statement stmt = getBooksDBConnection().createStatement();
        ResultSet authorIsbnRS = stmt.executeQuery(QUERY_ALL_AUTHORS);
        ArrayList<Author> authors = new ArrayList<>();
        while (authorIsbnRS.next()) {
            Author author = new Author();
            author.setAuthorID(authorIsbnRS.getInt("authorID"));
            author.setAuthorFirstName(authorIsbnRS.getString("firstName"));
            author.setAuthorLastName(authorIsbnRS.getString("lastName"));
            author.setBooks(new ArrayList<>());
            authors.add(author);
        }
        return authors;
    }

    /**
     * This method returns a list of books
     * @return
     * @throws SQLException
     */
    public static ArrayList<Book> getBooks() throws SQLException {
        Statement stmt = getBooksDBConnection().createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_ALL_TITLES);
        ArrayList<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book();
            book.setIsbn(rs.getString("isbn"));
            book.setTitle(rs.getString("title"));
            book.setEdNumber(rs.getInt("editionNumber"));
            book.setCopyright(rs.getString("copyright"));
            book.setAuthors(new ArrayList<>());
            books.add(book);
        }
        return books;
    }

    /**
     * This method returns a list of authorISBN

     * @return
     * @throws SQLException
     */
    public static ArrayList<List> getAuthorISBN() throws SQLException {
        Statement stmt = getBooksDBConnection().createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_ALL_AUTHORISBN);
        ArrayList<List> authorISBN = new ArrayList<>();
        while (rs.next()) {
            List<String> authorISBNList = new ArrayList<>();
            authorISBNList.add(rs.getString("isbn"));
            authorISBNList.add(rs.getString("authorID"));
            authorISBN.add(authorISBNList);
        }
        return authorISBN;
    }

    /**
     * This method adds a book to the database
     * @param book
     * @throws SQLException
     */
    public static void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO titles (isbn, title, editionNumber, copyright) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = getBooksDBConnection().prepareStatement(sql);
        pstmt.setString(1, book.getIsbn());
        pstmt.setString(2, book.getTitle());
        pstmt.setInt(3, book.getEdNumber());
        pstmt.setString(4, book.getCopyright());
        pstmt.executeUpdate();
    }

    /**
     * This method adds an author to the database
     * @param author
     * @throws SQLException
     */
    public static void addAuthor(Author author) throws SQLException {
        String sql = "INSERT INTO authors (authorID, firstName, lastName) VALUES (default, ?, ?)";
        PreparedStatement pstmt = getBooksDBConnection().prepareStatement(sql);
        pstmt.setString(1, author.getAuthorFirstName());
        pstmt.setString(2, author.getAuthorLastName());
        pstmt.executeUpdate();
    }

    /**
     *  This method adds an authorISBN to the database
     * @param isbn
     * @param authorID
     * @throws SQLException
     */
    public static void addAuthorISBN(String isbn, int authorID) throws SQLException {
        String sql = "INSERT INTO authorISBN (isbn, authorID) VALUES (?, ?)";
        PreparedStatement pstmt = getBooksDBConnection().prepareStatement(sql);
        pstmt.setString(1, isbn);
        pstmt.setInt(2, authorID);
        pstmt.executeUpdate();
    }

    /**
     * This method creates a list of books and authors
     * @param books
     * @param authors
     * @param authorISBN
     * @throws SQLException
     */
    static void createBooksAuthorList(ArrayList<Book> books, ArrayList<Author> authors, ArrayList<List> authorISBN) throws SQLException {
        for (Book book : books) {
            for (List authorISBNList : authorISBN) {
                if (Objects.equals(book.getIsbn(), authorISBNList.get(0))) {
                    for (Author author : authors) {
                        if (author.getAuthorID() == Integer.parseInt((String) authorISBNList.get(1))) {
                            book.getAuthors().add(author);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method creates a list of authors and books
     * @param books
     * @param authors
     * @param authorISBN
     * @throws SQLException
     */
    static void createAuthorsBookList(ArrayList<Book> books, ArrayList<Author> authors, ArrayList<List> authorISBN) throws SQLException {
        for (Author author : authors) {
            for (List authorISBNList : authorISBN) {
                if (author.getAuthorID() == Integer.parseInt((String) authorISBNList.get(1))) {
                    for (Book book : books) {
                        if (Objects.equals(book.getIsbn(), authorISBNList.get(0))) {
                            author.getBooks().add(book);
                        }
                    }
                }
            }
        }
    }
    private static Connection getBooksDBConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            System.out.println("Option 1: Find the class worked!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: unable to load driver class!");
        } catch(IllegalAccessException ex) {
            System.err.println("Error: access problem while loading!");
        } catch(InstantiationException ex){
            System.err.println("Error: unable to instantiate driver!");
        }

        return DriverManager.getConnection(JAVA_BOOKS_DB_URL, USER, PASS);
    }
}