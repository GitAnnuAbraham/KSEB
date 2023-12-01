package com.kseb.consumers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kseb.beans.ComplaintDetailsBean;

@WebServlet("/createComplaint")
public class CreateComplaint extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    if (session != null && session.getAttribute("username") != null
	            && "Consumer".equals(session.getAttribute("designation"))) {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        out.println("<html><head><link rel='stylesheet' type='text/css' href='consumer.css'></head><body>");
	        out.println("<ul>");
	        out.println("  <li><a href='consumer'>Home</a></li>");
			out.println("  <li><a href='createComplaint'>Create Complaints</a></li>");
			out.println("  <li><a href='showComplaintStatus'>Show Complaints</a></li>");
			out.println("  <li><a href='logout'>Logout</a></li>");
			out.println("</ul>");
	        out.println("<form name='consumerComplaint' action='createComplaint' method='post'>");
	        out.println("    <h1>Consumer Complaint Form</h1>");
	        out.println("    <div id='input-text'>");
	        out.println("        <label><b>Consumer Id</b></label>");
	        out.println("        <input type='text' name='consumerId'>");
	        out.println("        <label><b>Complaint Description</b></label>");
	        out.println("        <textarea rows='5' cols='50' name='desc'></textarea>");
	        out.println("    </div>");
	        out.println("    <div id='signup'>");
	        out.println("        <input type='submit' value='Lodge Complaint' id='signup-btn'/>");
	        out.println("    </div>");
	        out.println("</form>");

	        out.println("</body></html>");
	        out.close();
	    } else {
	        response.sendRedirect("login");
	    }
	}
			
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String complaintDesc = req.getParameter("desc");
		String consumerId = req.getParameter("consumerId");
		ComplaintDetailsBean complaintBean = new ComplaintDetailsBean();
		complaintBean.setDesc(complaintDesc);
		complaintBean.setLoggedOn(new Date());
		complaintBean.setStatus("PENDING_ALLOCATION");
		complaintBean.setConsumerId(Integer.parseInt(consumerId));
		if(createComplaint(complaintBean)) {
			req.setAttribute("createComplaint", "Y");
		} else {
			req.setAttribute("createComplaint", "N");
		}
		RequestDispatcher dispatcher = req.getRequestDispatcher("consumer");
		dispatcher.forward(req, resp);
	}

	private boolean createComplaint(ComplaintDetailsBean complaintBean) throws IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kseb", "root", "root");
			PreparedStatement pst = conn.prepareStatement(
					"insert into Complaint_details (Complaint_desc, Logged_on, Status, Consumer_id) values(?,?,?,?)");
			pst.setString(1, complaintBean.getDesc());
			pst.setDate(2,  new java.sql.Date(complaintBean.getLoggedOn().getTime()));
			pst.setString(3, complaintBean.getStatus());
			pst.setInt(4, complaintBean.getConsumerId());
			pst.executeUpdate();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}
