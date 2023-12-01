package com.kseb.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.UserBean;

@WebServlet("/listUsers")
public class UserList extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("username") != null
					&& "Admin".equals(session.getAttribute("designation"))) {
				List<UserBean> fetchUserDetails = fetchUserDetails();
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.print("<html><head><link rel='stylesheet' type='text/css' href='tables.css'></head><body>");
				out.println("<ul>");
				out.println("  <li><a href='admin'>Home</a></li>");
				out.println("  <li><a href='createUser'>Create User</a></li>");
				out.println("  <li><a href='listUsers'>List Users</a></li>");
				out.println("  <li><a href='complaints'>List Complaints</a></li>");
				out.println("  <li><a href='logout'>Logout</a></li>");
				out.println("</ul>");
				out.print("<h1>User details</h1>");
				out.print("<table border = '1'>");
				out.print("<tr><th>Complaint Id</th><th>User deignation</th><th>Username</th></tr>");
				fetchUserDetails.stream().forEach( e -> {
					out.print("<tr><td>" + e.getUserId() 
							+ "</td><td>" + e.getDesignation() 
							+ "</td><td>" + e.getUserName()
							+ "</td></tr>");
				});
				out.print("</table>");
				out.print("</body></html");
			} else {
				response.sendRedirect("login");
			}
		}

	private List<UserBean> fetchUserDetails() {
		List<UserBean> userList = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn
					.prepareStatement("Select User_id, User_designation, User_name " + "from user_details ");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				UserBean userBean = new UserBean();
				userBean.setUserId(rs.getString(1));
				userBean.setDesignation(rs.getString(2));
				userBean.setUserName(rs.getString(3));
				userList.add(userBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}
}