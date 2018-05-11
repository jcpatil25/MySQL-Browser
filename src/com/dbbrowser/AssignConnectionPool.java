package com.dbbrowser;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.Service;

@WebServlet("/AssignConnectionPool")
public class AssignConnectionPool extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		PrintWriter out = response.getWriter();
   		System.out.println("Inside assign pool");
		
		String url = request.getParameter("url");
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		
		Service.createNewConnectionPool("com.mysql.jdbc.Driver", url, uname, pass, 10, 3, true);
		out.println("Assigned pool");
		System.out.println("Done Assigned pool");
   	}

}
