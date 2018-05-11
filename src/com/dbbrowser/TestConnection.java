package com.dbbrowser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.Service;

/**
 * Servlet implementation class TestConnection
 */
@WebServlet("/TestConnection")
public class TestConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		System.out.println("Inside test connection");

		String conName = request.getParameter("conName");
		String hostname = request.getParameter("hostname");
		int portNo = Integer.parseInt(request.getParameter("portNo"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		response.setContentType("html");

		String res="";
		
		Service s = new Service();
		try {
			 res = s.testConnection(conName, hostname, portNo, username, password);
			 System.out.println(res.substring(0,27));
			 if(res.substring(0,27).equals("Communications link failure")){
				 res = "Connection Failed!";
			 }
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.println(res);
		
	}

}
