package com.kseb.lineman;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.ComplaintDetailsBean;

@WebServlet("/processComplaint")
public class ProcessComplaint extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Lineman".equals(session.getAttribute("designation"))) {

			String complaintId = request.getParameter("complaintId");
			ComplaintDetailsBean complaintDetailsBean = fetchComplaintDetailsById(complaintId);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print("<html><head><link rel='stylesheet' type='text/css' href='complaint.css'></head><body>");
			out.println("<ul>");
			out.println("  <li><a href='lineman'>Home</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");
			out.print("<h1>Update Complaint Status</h1>");
			
            out.print("<p>Complaint ID: " + complaintDetailsBean.getId() + "</p>");
            out.print("<p>Description: " + complaintDetailsBean.getDesc() + "</p>");
            out.print("<p>Logged On: " + complaintDetailsBean.getLoggedOn() + "</p>");
            out.print("<p>Status: " + complaintDetailsBean.getStatus() + "</p>");
            out.print("<p>Resolved On: " + complaintDetailsBean.getResolvedOn() + "</p>");
            out.print("<p>Allocated To: " + complaintDetailsBean.getAllocatedTo() + "</p>");
            out.print("<p>Consumer ID: " + complaintDetailsBean.getConsumerId() + "</p>");
            
            out.print("<form action='processComplaint' method='post'>");
            out.print("<label>Update Status:</label>");
            out.print("<select name='status'>");
            out.print("<option value='RESOLVED'>RESOLVED</option>");
            out.print("</select>");
            out.print("<input type='hidden' name='complaintId' value='" + complaintId + "'>");
            out.print("<input type='submit' value='Update'>");
            out.print("</form>");
			
			out.print("</body></html");
		} else {
			response.sendRedirect("login");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String complaintId = request.getParameter("complaintId");
		updateComplaintStatus(complaintId);
		doGet(request, response);
	}
	
	private void updateComplaintStatus(String complaintId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement("update complaint_details set Status = ?, Resolved_on = ? "
					+ "where Complaint_Id = ?");
			pst.setString(1, "COMPLETED");
			pst.setDate(2, new java.sql.Date(new Date().getTime()));
			pst.setInt(3, Integer.valueOf(complaintId));
			pst.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private ComplaintDetailsBean fetchComplaintDetailsById(String complaintId) {
		ComplaintDetailsBean complaintDetailsBean = new ComplaintDetailsBean();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement("Select Complaint_Id, Complaint_desc, Logged_on, Status, "
					+ "Resolved_on, user_details.User_name, Consumer_id from complaint_details "
					+ "left join user_details on user_details.User_id = complaint_details.Allocated_to "
					+ "where Complaint_Id = ?");
			pst.setString(1, complaintId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				complaintDetailsBean.setId(rs.getInt(1));
				complaintDetailsBean.setDesc(rs.getString(2));
				complaintDetailsBean.setLoggedOn(rs.getDate(3));
				complaintDetailsBean.setStatus(rs.getString(4));
				complaintDetailsBean.setResolvedOn(rs.getDate(5));
				complaintDetailsBean.setAllocatedTo(rs.getString(6));
				complaintDetailsBean.setConsumerId(rs.getInt(7));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return complaintDetailsBean;
	}
}
