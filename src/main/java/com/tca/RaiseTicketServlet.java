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

@WebServlet({"/raiseTicketServlet","/raiticketser"})
public class RaiseTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get Parameters
        String name = request.getParameter("nm");
        String eid  = request.getParameter("eid");
        String dept = request.getParameter("dept");
        String issue = request.getParameter("issue");
        String desc = request.getParameter("desc");

        // ==============================
        // 🔐 SERVER SIDE VALIDATION
        // ==============================

        if (name == null || name.trim().isEmpty()) {
            out.println("Employee Name is required.");
            return;
        }

        if (!name.matches("[a-zA-Z ]+")) {
            out.println("Employee Name should contain only letters.");
            return;
        }

        if (eid == null || eid.trim().isEmpty()) {
            out.println("Employee ID is required.");
            return;
        }

        if (!eid.matches("[a-zA-Z0-9]+")) {
            out.println("Employee ID must be alphanumeric.");
            return;
        }

        if (dept == null || dept.trim().isEmpty()) {
            out.println("Please select a Department.");
            return;
        }

        if (issue == null || issue.trim().isEmpty()) {
            out.println("Please select an Issue type.");
            return;
        }

        if (desc == null || desc.trim().isEmpty()) {
            out.println("Description is required.");
            return;
        }

        if (desc.length() < 10 || desc.length() > 500) {
            out.println("Description must be between 10 and 500 characters.");
            return;
        }

        // ==============================
        // 🗄 DATABASE INSERTION
        // ==============================

        final String DB_URL = "jdbc:mysql://localhost:3306/ajdb21";
        final String DB_USER = "root";
        final String DB_PWD  = "Cdevyani@1";
        final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            ps = con.prepareStatement(
                "INSERT INTO tickets (employee_name, employee_id, department, issue, description, status) VALUES (?, ?, ?, ?, ?, 'Pending')",
                PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name.trim());
            ps.setString(2, eid.trim());
            ps.setString(3, dept);
            ps.setString(4, issue);
            ps.setString(5, desc.trim());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int ticketId = rs.getInt(1);

                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<meta charset='UTF-8'>");
                    out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
                    out.println("<title>Ticket Submitted</title>");
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

                    out.println("<div class='card shadow-lg border-0'>");

                    out.println("<div class='card-header bg-success text-white text-center'>");
                    out.println("<h4 class='mb-0'>Ticket Submitted Successfully!</h4>");
                    out.println("</div>");

                    out.println("<div class='card-body text-center'>");

                    out.println("<div class='display-5 text-success mb-3'>✔</div>");

                    out.println("<p class='fs-5'><strong>Ticket ID:</strong> " + ticketId + "</p>");

                    out.println("<p class='fs-5'><strong>Status:</strong> ");
                    out.println("<span class='badge bg-warning text-dark'>Pending</span></p>");

                    out.println("<hr>");

                    out.println("<div class='d-grid gap-2'>");
                    out.println("<a href='trackTicket.html' class='btn btn-primary'>Track This Ticket</a>");
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
            } else {
                out.println("Failed to submit ticket.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Something went wrong while saving ticket.");
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