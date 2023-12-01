package com.kseb.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.UserBean;

@WebServlet("/createUser")
public class CreateUserServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Admin".equals(session.getAttribute("designation"))) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='signup.css'></head><body>");
			out.println("<ul>");
			out.println("  <li><a href='admin'>Home</a></li>");
			out.println("  <li><a href='createUser'>Create User</a></li>");
			out.println("  <li><a href='listUsers'>List Users</a></li>");
			out.println("  <li><a href='complaints'>List Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");
			out.println("<br/>");
			out.println("<form name='signup' action='createUser' method='post'>");
	        out.println("  <h1>Create User Form</h1>");
	        out.println("  <div id='input-text'>");
	        out.println("    <label><b>Name</b></label>");
	        out.println("    <input type='text' name='name' placeholder='Enter name'>");
	        out.println("  </div>");
	        out.println("  <div id='input-text'>");
	        out.println("    <label><b>Designation</b></label>");
	        out.println("    <select name='designation'>");
	        out.println("      <option>--Select One--</option>");
	        out.println("      <option value='Electrical Engineer'>Electrical Engineer</option>");
	        out.println("      <option value='Lineman'>Lineman</option>");
	        out.println("      <option value='Inventory Manager'>Inventory Manager</option>");
	        out.println("      <option value='Consumer'>Consumer</option>");
	        out.println("    </select>");
	        out.println("  </div>");
	        out.println("  <div id='input-text'>");
	        out.println("    <label><b>Password</b></label>");
	        out.println("    <input type='password' name='password' placeholder='Enter Password'/>");
	        out.println("  </div>");
	        out.println("  <div id='signup'>");
	        out.println("    <input type='submit' value='Sign Up' id='signup-btn' onclick='return validateForm()'/>");
	        out.println("  </div>");
	        out.println("</form>");
			
			out.println("</body></html>");
			out.close();
		} else {
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("name");
		String designation = request.getParameter("designation");
		String password = request.getParameter("password");

		UserBean userBean = new UserBean();
		userBean.setDesignation(designation);
		userBean.setPassword(password);
		userBean.setUserName(userName);
		if (createUser(userBean)) {
			request.setAttribute("createUser", "Y");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("admin");
		dispatcher.forward(request, response);
	}

	private boolean createUser(UserBean userBean) throws IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement(
					"insert into user_details (User_designation, User_name, User_password) values(?,?,?)");
			pst.setString(1, userBean.getDesignation());
			pst.setString(2, userBean.getUserName());
			pst.setString(3, userBean.getPassword());
			pst.executeUpdate();

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
