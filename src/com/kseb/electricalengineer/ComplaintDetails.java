package com.kseb.electricalengineer;

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

import com.kseb.beans.ComplaintDetailsBean;

@WebServlet("/complaints")
public class ComplaintDetails extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& ("Electrical Engineer".equals(session.getAttribute("designation"))
						|| "Admin".equals(session.getAttribute("designation")))) {
			List<ComplaintDetailsBean> complaintDetailsList = fetchComplaintDetails();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='tables.css'></head><body>");
			if ("Electrical Engineer".equals(session.getAttribute("designation"))) {
				out.println("<ul>");
				out.println("  <li><a href='engineer'>Home</a></li>");
				out.println("  <li><a href='complaints'>List Complaints</a></li>");
				out.println("  <li><a href='logout'>Logout</a></li>");
				out.println("</ul>");
			} else if ( "Admin".equals(session.getAttribute("designation"))) {
				out.println("<ul>");
				out.println("  <li><a href='admin'>Home</a></li>");
				out.println("  <li><a href='createUser'>Create User</a></li>");
				out.println("  <li><a href='listUsers'>List Users</a></li>");
				out.println("  <li><a href='complaints'>List Complaints</a></li>");
				out.println("  <li><a href='logout'>Logout</a></li>");
				out.println("</ul>");
			} 
			out.print("<h1>Complaint details</h1>");
			out.print("<table border = '1'>");
			out.print("<tr><th>Complaint Id</th><th>Complaint desc</th><th>Logged on</th>"
					+ "<th>Status</th><th>Resolved On</th><th>Allocated To</th><th>Consumer Id</th></tr>");
			complaintDetailsList.stream().forEach( e -> {
				if("Electrical Engineer".equals(session.getAttribute("designation"))) {
					out.print("<tr><td><a href='allocateComplaint?complaintId=" + e.getId() + "'>" + e.getId() + "</a>"
							+ "</td><td>" + e.getDesc() 
							+ "</td><td>" + e.getLoggedOn() 
							+ "</td><td>" + e.getStatus()
							+ "</td><td>" + e.getResolvedOn()
							+ "</td><td>" + e.getAllocatedTo() 
							+ "</td><td>" + e.getConsumerId() 
							+ "</td></tr>");
				} else {
					out.print("<tr><td><a href='#'>" + e.getId() + "</a>"
							+ "</td><td>" + e.getDesc() 
							+ "</td><td>" + e.getLoggedOn() 
							+ "</td><td>" + e.getStatus()
							+ "</td><td>" + e.getResolvedOn()
							+ "</td><td>" + e.getAllocatedTo() 
							+ "</td><td>" + e.getConsumerId() 
							+ "</td></tr>");
				}
				
			});
			out.print("</table>");
			out.print("</body></html");
			
		} else {
			response.sendRedirect("login");
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	
	private List<ComplaintDetailsBean> fetchComplaintDetails() {
		List<ComplaintDetailsBean> complaintBeanList = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement("Select Complaint_Id, Complaint_desc, Logged_on, Status, "
					+ "Resolved_on, user_details.User_name, Consumer_id "
					+ "from Complaint_details "
					+ "left join user_details on user_details.User_id = complaint_details.Allocated_to "
					+ "where Status != 'COMPLETED' ");
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				ComplaintDetailsBean complaintDetailsBean = new ComplaintDetailsBean();
				complaintDetailsBean.setId(rs.getInt(1));
				complaintDetailsBean.setDesc(rs.getString(2));
				complaintDetailsBean.setLoggedOn(rs.getDate(3));
				complaintDetailsBean.setStatus(rs.getString(4));
				complaintDetailsBean.setResolvedOn(rs.getDate(5));
				complaintDetailsBean.setAllocatedTo(rs.getString(6));
				complaintDetailsBean.setConsumerId(rs.getInt(7));
				complaintBeanList.add(complaintDetailsBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return complaintBeanList;
	}

}
