package com.tca;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/trackTicketServlet","/trackservlet"})
public class trackTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        final String DB_URL = "jdbc:mysql://nozomi.proxy.rlwy.net:37413/railway";
        final String DB_USER = "root";
        final String DB_PWD  = "JtoWvJhTaVbwzEJzvhCXScgNRVKNRFiw";
        final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            String tid = request.getParameter("tid");

            ps = con.prepareStatement("SELECT * FROM tickets WHERE ticket_id = ?");
            ps.setString(1, tid);

            rs = ps.executeQuery();   // ✅ Correct method for SELECT

//            if (rs.next()) {
//                out.println("<h3>Ticket Found!</h3>");
//                out.println("Ticket ID : " + rs.getString("ticket_id") + "<br>");
//                out.println("Status : " + rs.getString("status") + "<br>");
//                out.println("Description : " + rs.getString("description") + "<br>");
//            }
            if (rs.next()) {

                String status = rs.getString("status");
                String badgeClass = "bg-secondary";

                if(status.equalsIgnoreCase("Pending"))
                    badgeClass = "bg-warning text-dark";
                else if(status.equalsIgnoreCase("Resolved"))
                    badgeClass = "bg-success";
                else if(status.equalsIgnoreCase("In Progress"))
                    badgeClass = "bg-info text-dark";

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
                out.println("<title>Ticket Result</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body class='bg-light'>");

                // Navbar
                out.println("<nav class='navbar navbar-dark bg-dark'>");
                out.println("<div class='container'>");
                out.println("<a class='navbar-brand'>Ticket Management System</a>");
                out.println("</div>");
                out.println("</nav>");

                // Result Card
                out.println("<div class='container mt-5'>");
                out.println("<div class='row justify-content-center'>");
                out.println("<div class='col-md-6'>");

                out.println("<div class='card shadow-lg border-0'>");

                out.println("<div class='card-header bg-dark text-white text-center'>");
                out.println("<h4 class='mb-0'>Ticket Details</h4>");
                out.println("</div>");

                out.println("<div class='card-body text-center'>");

                out.println("<p class='fs-5'><strong>Ticket ID:</strong> "
                        + rs.getString("ticket_id") + "</p>");

                out.println("<p class='fs-5'><strong>Status:</strong> ");
                out.println("<span class='badge " + badgeClass + "'>"
                        + status + "</span></p>");

                out.println("<p class='fs-6'><strong>Description:</strong><br>"
                        + rs.getString("description") + "</p>");

                out.println("<hr>");

                out.println("<div class='d-grid gap-2'>");
                out.println("<a href='trackTicket.html' class='btn btn-primary'>Track Another Ticket</a>");
                out.println("<a href='index.html' class='btn btn-outline-secondary'>Go To Home</a>");
                out.println("</div>");

                out.println("</div>"); // card-body
                out.println("</div>"); // card

                out.println("</div>");
                out.println("</div>");
                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }
            else {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
                out.println("<title>Ticket Not Found</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body class='bg-light'>");
                
                // Navbar
                out.println("<nav class='navbar navbar-dark bg-dark'>");
                out.println("<div class='container'>");
                out.println("<a class='navbar-brand'>Ticket Management System</a>");
                out.println("</div>");
                out.println("</nav>");

                out.println("<div class='container mt-5'>");
                out.println("<div class='row justify-content-center'>");
                out.println("<div class='col-md-6'>");

                out.println("<div class='alert alert-danger text-center shadow'>");
                out.println("<strong>Ticket ID not found!</strong><br>");
                out.println("Please try again.");
                out.println("</div>");

                out.println("<div class='text-center'>");
                out.println("<a href='trackTicket.html' class='btn btn-danger'>Try Again</a>");
                out.println("</div>");

                out.println("</div>");
                out.println("</div>");
                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Something went wrong !!");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        out.close();	
    }
}