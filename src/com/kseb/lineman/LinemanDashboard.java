package com.kseb.lineman;

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

@WebServlet("/lineman")
public class LinemanDashboard extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("username") != null
				&& "Lineman".equals(session.getAttribute("designation"))) {
			String userName = (String) session.getAttribute("username");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><link rel='stylesheet' type='text/css' href='tables.css'></head><body>");
			out.println("<h3>Lineman Dashboard</h3>");
			out.println("<ul>");
			out.println("  <li><a href='lineman'>Home</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");
			String workStatus = (String) request.getAttribute("workStatus");
			if (workStatus != null && "Completed".equals(workStatus)) {
				out.println("<p style='color:green;'>Work status updated successfully.</p>");
			}
			List<ComplaintDetailsBean> complaintDetailsList = fetchComplaintDetails(userName);
			out.print("<h1>Complaint details</h1>");
			out.print("<table border = '1'>");
			out.print("<tr><th>Complaint Id</th><th>Complaint desc</th><th>Logged on</th>"
					+ "<th>Status</th><th>Allocated To</th><th>Consumer Id</th></tr>");
			complaintDetailsList.stream().forEach( e -> {
				out.print("<tr><td><a href='processComplaint?complaintId=" + e.getId() + "'>" + e.getId() + "</a>"
						+ "</td><td>" + e.getDesc() 
						+ "</td><td>" + e.getLoggedOn() 
						+ "</td><td>" + e.getStatus()
						+ "</td><td>" + e.getAllocatedTo() 
						+ "</td><td>" + e.getConsumerId() 
						+ "</td></tr>");
			});
			out.print("</table>");
			out.println("</body></html>");
			out.close();
		} else {
			response.sendRedirect("login");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private List<ComplaintDetailsBean> fetchComplaintDetails(String userName) {
		List<ComplaintDetailsBean> complaintBeanList = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement("Select Complaint_Id, Complaint_desc, Logged_on, Status, "
					+ "Resolved_on, user_details.User_name, Consumer_id "
					+ "from Complaint_details "
					+ "left join user_details on user_details.User_id = complaint_details.Allocated_to "
					+ "where Status != 'COMPLETED' and user_details.User_name = ?");
			pst.setString(1, userName);
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
