package com.codewithz;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Movie")  // Ensure this matches your form's action URL
public class KunalEkare extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String movieName = request.getParameter("movie_name");

        out.println("<html><body>");
        out.println("<h2>Received Movie Name: " + movieName + "</h2>");

        if (movieName == null || movieName.trim().isEmpty()) {
            out.println("<p style='color: red;'>Movie name is required.</p>");
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movies_demo", "root", "root");

            // SQL Query
            String query = "SELECT * FROM movies_demo WHERE Movie_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, movieName);
            ResultSet rs = stmt.executeQuery();

            // Displaying movie details
            if (rs.next()) {
                out.println("<h1>Movie Details</h1>");
                out.println("<p><strong>Movie Name:</strong> " + rs.getString("Movie_name") + "</p>");
                out.println("<p><strong>Rating:</strong> " + rs.getFloat("Rating") + "</p>");
                out.println("<p><strong>Actor:</strong> " + rs.getString("Actor") + "</p>");
                out.println("<p><strong>Actress:</strong> " + rs.getString("Actress") + "</p>");
                out.println("<p><strong>Director:</strong> " + rs.getString("Director") + "</p>");
                out.println("<p><strong>About Movie:</strong> " + rs.getString("About_Movie") + "</p>");
            } else {
                out.println("<h1>No movie found with the name: " + movieName + "</h1>");
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(out); // Show stack trace only for debugging purposes
            out.println("<p style='color: red;'>Something went wrong. Please try again later.</p>");
        }

        out.println("</body></html>");
    }
}
