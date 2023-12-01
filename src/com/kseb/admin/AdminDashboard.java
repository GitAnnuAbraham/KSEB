package com.kseb.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Admin".equals(session.getAttribute("designation"))) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='admin.css'></head><body>");
			out.println("<h3>Admin Dashboard</h3>");
			out.println("<ul>");
			out.println("  <li><a href='admin'>Home</a></li>");
			out.println("  <li><a href='createUser'>Create User</a></li>");
			out.println("  <li><a href='listUsers'>List Users</a></li>");
			out.println("  <li><a href='complaints'>List Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");

			String createUser = (String) request.getAttribute("createUser");
			if (createUser != null && "Y".equals(createUser)) {
				out.println("<p style='color:green;'>User created successfully.</p>");
			} else if (createUser != null && "N".equals(createUser)) {
				out.println("<p style='color:red;'>Create user failed.</p>");
			}
			out.println("</body></html>");
			out.close();
		} else {
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}


}
