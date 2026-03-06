package com.tca.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/adminloginservlet")
public class AdminLoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		   final String DB_URL = "jdbc:mysql://nozomi.proxy.rlwy.net:37413/railway";
	       final String DB_USER = "root";
	       final String DB_PWD  = "JtoWvJhTaVbwzEJzvhCXScgNRVKNRFiw";
	       final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            String un = request.getParameter("un");
    		String pwd = request.getParameter("pwd");

    		if(un == null || pwd == null || un.trim().isEmpty() || pwd.trim().isEmpty()) {
    		    out.println("Username and Password required!");
    		    return;
    		}
    		
    		ps = con.prepareStatement("SELECT admin_id FROM admins WHERE username=? AND password=?");
    		ps.setString(1, un);
    		ps.setString(2, pwd);
    		
    		rs = ps.executeQuery();
    		
    		if(rs.next())
    		{
    			HttpSession hs = request.getSession(true);
    			hs.setAttribute("admin", un);	
    			
    			response.sendRedirect("./admindashboardservlet");
    		}
    		else {

    		    out.println("<!DOCTYPE html>");
    		    out.println("<html>");
    		    out.println("<head>");
    		    out.println("<meta charset='UTF-8'>");
    		    out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
    		    out.println("<title>Login Failed</title>");
    		    out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css' rel='stylesheet'>");
    		    out.println("</head>");
    		    out.println("<body class='bg-light'>");

    		    out.println("<div class='container mt-5'>");
    		    out.println("<div class='row justify-content-center'>");
    		    out.println("<div class='col-md-4'>");

    		    out.println("<div class='card shadow'>");
    		    out.println("<div class='card-body text-center'>");

    		    out.println("<div class='alert alert-danger'>");
    		    out.println("<strong>Invalid Credentials!</strong><br>");
    		    out.println("Please check your username and password.");
    		    out.println("</div>");

    		    out.println("<a href='adminLogin.html' class='btn btn-dark w-100'>Try Again</a>");

    		    out.println("</div>");
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
