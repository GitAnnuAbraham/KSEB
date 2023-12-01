package com.kseb.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.UserBean;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><head><link rel='stylesheet' type='text/css' href='login.css'></head><body>");

		// Check for the attribute indicating a failed login attempt
		Boolean isLoginFailed = (Boolean) request.getAttribute("loginFailed");
		if (isLoginFailed != null && isLoginFailed == true) {
			out.println("<p style='color:red;'>Login Failed. Please check your username and password.</p>");
		}

		out.println("<h2>Login Page</h2>");
		out.println("<form action='login' method='post'>");
		out.println("  <label for='username'>Username:</label>");
		out.println("  <input type='text' id='username' name='username' required><br>");
		out.println("  <label for='password'>Password:</label>");
		out.println("  <input type='password' id='password' name='password' required><br>");
		out.println("  <input type='submit' value='Login'>");
		out.println("</form>");
		out.println("</body></html>");

		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserBean userDetails = getUserDetails(username, password);
		if ("Admin".equals(userDetails.getDesignation()) && password.equals(userDetails.getPassword())) {
			HttpSession session = request.getSession(true);
			session.setAttribute("username", username);
			session.setAttribute("designation", "Admin");
			response.sendRedirect("admin");
		} else if ("Consumer".equals(userDetails.getDesignation()) && password.equals(userDetails.getPassword())) {
			HttpSession session = request.getSession(true);
			session.setAttribute("username", username);
			session.setAttribute("designation", "Consumer");
			response.sendRedirect("consumer");
		} else if("Electrical Engineer".equals(userDetails.getDesignation()) && password.equals(userDetails.getPassword())) {
			HttpSession session = request.getSession(true);
			session.setAttribute("username", username);
			session.setAttribute("designation", "Electrical Engineer");
			response.sendRedirect("engineer");
		} else if ("Lineman".equals(userDetails.getDesignation())
				&& password.equals(userDetails.getPassword())) {
			HttpSession session = request.getSession(true);
			session.setAttribute("username", username);
			session.setAttribute("designation", "Lineman");
			response.sendRedirect("lineman");
		} else {
			request.setAttribute("loginFailed", true);
			doGet(request, response);
		}
	}

	private UserBean getUserDetails(String username, String password) {
		UserBean userBean = new UserBean();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = con.prepareStatement(
					"select User_name, User_designation, User_password from user_details where User_name = ?");
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				userBean.setUserName(rs.getString(1));
				userBean.setDesignation(rs.getString(2));
				userBean.setPassword(rs.getString(3));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userBean;
	}
}
