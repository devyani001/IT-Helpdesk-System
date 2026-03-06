package com.tca.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateticketservlet")
public class UpdateTicketServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        // 1️⃣ Session Protection FIRST
        HttpSession hs = request.getSession(false);

        if (hs == null || hs.getAttribute("admin") == null) {
            response.sendRedirect("adminLogin.html");
            return;
        }

        // 2️⃣ Get Parameters
        String id = request.getParameter("id");
        String status = request.getParameter("status");

        // 3️⃣ Validate Parameters
        if (id == null || status == null || id.trim().isEmpty() || status.trim().isEmpty()) {
            response.sendRedirect("admindashboardservlet");
            return;
        }

        final String DB_URL = "jdbc:mysql://nozomi.proxy.rlwy.net:37413/railway";
        final String DB_USER = "root";
        final String DB_PWD  = "JtoWvJhTaVbwzEJzvhCXScgNRVKNRFiw";
        final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";


        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            // 4️⃣ Correct Parameter Order
            ps = con.prepareStatement(
                    "UPDATE tickets SET status=? WHERE ticket_id=?");

            ps.setString(1, status);                     // first ? = status
            ps.setInt(2, Integer.parseInt(id));          // second ? = ticket_id

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("admindashboardservlet");
            } else {
                response.getWriter().println("Update failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Something went wrong!");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}