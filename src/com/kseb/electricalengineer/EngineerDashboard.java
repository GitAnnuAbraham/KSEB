package com.kseb.electricalengineer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/engineer")
public class EngineerDashboard extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Electrical Engineer".equals(session.getAttribute("designation"))) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='admin.css'></head><body>");
			out.println("<h3>Engineer Dashboard</h3>");
			out.println("<ul>");
			out.println("  <li><a href='engineer'>Home</a></li>");
			out.println("  <li><a href='complaints'>List Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");

			String workAllocation = (String) request.getAttribute("workAllocation");
			if (workAllocation != null && "Y".equals(workAllocation)) {
				out.println("<p style='color:green;'>Work allocated successfully.</p>");
			} else if (workAllocation != null && "N".equals(workAllocation)) {
				out.println("<p style='color:red;'>Work Allocation failed.</p>");
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

