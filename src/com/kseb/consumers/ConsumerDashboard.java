package com.kseb.consumers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/consumer")
public class ConsumerDashboard extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Consumer".equals(session.getAttribute("designation"))) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='admin.css'></head><body>");
			out.println("<h3>Consumer Dashboard</h3>");
			out.println("<ul>");
			out.println("  <li><a href='consumer'>Home</a></li>");
			out.println("  <li><a href='createComplaint'>Create Complaints</a></li>");
			out.println("  <li><a href='showComplaintStatus'>Show Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");

			// Check for the attribute indicating a failed login attempt
			String createComplaint = (String) request.getAttribute("createComplaint");
			if (createComplaint != null && "Y".equals(createComplaint)) {
				out.println("<p style='color:green;'>Complaint created successfully.</p>");
			} else if (createComplaint != null && "N".equals(createComplaint)) {
				out.println("<p style='color:red;'>Create Complaint failed.</p>");
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
