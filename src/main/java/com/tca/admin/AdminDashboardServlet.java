package com.tca.admin;

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
import jakarta.servlet.http.HttpSession;

@WebServlet("/admindashboardservlet")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		final String DB_URL = "jdbc:mysql://localhost:3306/ajdb21";
		final String DB_USER = "root";
		final String DB_PWD = "Cdevyani@1";
		final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		
		try
        {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            HttpSession hs = request.getSession(false);

            if(hs == null || hs.getAttribute("admin") == null) {
                response.sendRedirect("adminLogin.html");
                return;
            }
    		else
    		{
    			ps = con.prepareStatement("SELECT * FROM tickets ORDER BY ticket_id DESC");
    			rs = ps.executeQuery();
    			
    			out.println("<!DOCTYPE html>");
    			out.println("<html>");
    			out.println("<head>");
    			out.println("<meta charset='UTF-8'>");
    			out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
    			out.println("<title>Admin Dashboard</title>");
    			out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css' rel='stylesheet'>");
    			out.println("</head>");
    			out.println("<body class='bg-light'>");

    			// Navbar
                out.println("<nav class='navbar navbar-dark bg-dark'>");
                out.println("<div class='container'>");
                out.println("<a class='navbar-brand'>Admin Dashboard</a>");
                out.println("<div class='ms-auto'>");
                out.println("<a href='logoutservlet' class='btn btn-danger btn-sm'>Logout</a>");
                out.println("</div>");
                out.println("</div>");
                out.println("</nav>");
                
    			// ✅ Container
    			out.println("<div class='container mt-4'>");
    			out.println("<div class='card shadow'>");
    			out.println("<div class='card-body'>");

    			out.println("<h4 class='mb-3'>All Tickets</h4>");

    			out.println("<div class='table-responsive'>");
    			out.println("<table class='table table-bordered table-hover align-middle text-center'>");

    			// Table Header
    			out.println("<thead class='table-dark'>");
    			out.println("<tr>");
    			out.println("<th>ID</th>");
    			out.println("<th>Name</th>");
    			out.println("<th>Emp ID</th>");
    			out.println("<th>Department</th>");
    			out.println("<th>Issue</th>");
    			out.println("<th>Status</th>");
    			out.println("<th>Action</th>");
    			out.println("</tr>");
    			out.println("</thead>");
    			out.println("<tbody>");
    			
    			while(rs.next()) {

    			    int id = rs.getInt("ticket_id");
    			    String currentStatus = rs.getString("status");

    			    out.println("<tr>");
    			    out.println("<td>" + id + "</td>");
    			    out.println("<td>" + rs.getString("employee_name") + "</td>");
    			    out.println("<td>" + rs.getString("employee_id") + "</td>");
    			    out.println("<td>" + rs.getString("department") + "</td>");
    			    out.println("<td>" + rs.getString("issue") + "</td>");
    			    out.println("<td>" + currentStatus + "</td>");

    			    // 🔥 ACTION COLUMN
    			    out.println("<td>");

    			    if(!"Resolved".equalsIgnoreCase(currentStatus)) {
    			    	out.println("<div class='grid gap-2 d-md-block'>");
    			        out.println("<a class='btn btn-success' href='updateticketservlet?id=" + id + "&status=Resolved'>Resolve</a> | ");
    			        out.println("<a class='btn btn-primary' href='updateticketservlet?id=" + id + "&status=In Progress'>In Progress</a>");
    			        out.println("</div>");
    			    } 
    			    else {
    			        out.println("Completed");
    			    }

    			    out.println("</td>");
    			    out.println("</tr>");
    			}
    			
    			out.println("</table>");
    			out.println("<br>");
    			out.println("<div class='d-grid gap-2'>");
    			out.println("<a class='btn btn-danger' href='logoutservlet'>Logout</a>");
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
