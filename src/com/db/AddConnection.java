package com.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddConnection")
public class AddConnection extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter pw = response.getWriter();

		// System.out.println("Query String:" +request.getQueryString());

		String conName = request.getParameter("conName");
		String hostname = request.getParameter("hostname");
		int portNo = Integer.parseInt(request.getParameter("portNo"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		response.setContentType("html");
		// System.out.println("conName : "+conName);
		// System.out.println("hostName : "+hostname);
		String str = "";

		Service s = new Service();
		try {
			String r = s.testConnection(conName, hostname, portNo, username,
					password);
			String substr = r.substring(0, 9);
			System.out.println(substr);
			if (substr.equals("Connected")) {
				int res = s.addConnection(conName, hostname, portNo, username,
						password);
				if (res == 1)
					str = "Connection Created Successfully!";
				else
					str = "Something went wrong!";
			} else {
				str = "Connection failed!";
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);

		pw.println(str);

	}

}
