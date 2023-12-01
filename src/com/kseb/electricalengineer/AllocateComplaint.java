package com.kseb.electricalengineer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.ComplaintDetailsBean;


@WebServlet("/allocateComplaint")
public class AllocateComplaint extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Electrical Engineer".equals(session.getAttribute("designation"))) {
			
			String complaintId = request.getParameter("complaintId");
			ComplaintDetailsBean complaintDetailsBean = fetchComplaintDetailsById(complaintId);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print("<html><head><link rel='stylesheet' type='text/css' href='complaint.css'></head><body>");
			out.println("<ul>");
			out.println("  <li><a href='engineer'>Home</a></li>");
			out.println("  <li><a href='complaints'>List Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");
			out.print("<h1>Assign Complaint</h1>");
			
            out.print("<p>Complaint ID: " + complaintDetailsBean.getId() + "</p>");
            out.print("<p>Description: " + complaintDetailsBean.getDesc() + "</p>");
            out.print("<p>Logged On: " + complaintDetailsBean.getLoggedOn() + "</p>");
            out.print("<p>Status: " + complaintDetailsBean.getStatus() + "</p>");
            out.print("<p>Resolved On: " + complaintDetailsBean.getResolvedOn() + "</p>");
            out.print("<p>Allocated To: " + complaintDetailsBean.getAllocatedTo() + "</p>");
            out.print("<p>Consumer ID: " + complaintDetailsBean.getConsumerId() + "</p>");
            
            Map<Integer, String> userMap = getLinemen();
            out.print("<form action='allocateComplaint' method='post'>");
            out.print("<label for='lineman'>Select Lineman:</label>");
            out.print("<select name='lineman'>");
            userMap.entrySet().stream().forEach(e -> {
            	out.print("<option value='" + e.getKey() + "'>" + e.getValue() + "</option>");
            });
            out.print("</select>");
            out.print("<input type='hidden' name='complaintId' value='" + complaintId + "'>");
            out.print("<input type='submit' value='Assign'>");
            out.print("</form>");
			
			out.print("</body></html");
		} else {
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String complaintId = request.getParameter("complaintId");
		String lineman = request.getParameter("lineman");
		assginComplaint(complaintId, lineman);
		doGet(request, response);
	}
	
	private void assginComplaint(String complaintId, String lineman) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement("update complaint_details set Allocated_to = ?, status = ? "
					+ "where Complaint_Id = ?");
			pst.setString(1, lineman);
			pst.setString(2, "ALLOCATED");
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

	private Map<Integer, String> getLinemen() {
		Map<Integer, String> userMap = new HashMap<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = con
					.prepareStatement("select User_id, User_name from user_details where User_designation = ?");
			pst.setString(1, "Lineman");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				userMap.put(rs.getInt(1), rs.getString(2));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userMap;
	}
}
