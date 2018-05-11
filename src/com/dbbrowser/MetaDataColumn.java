package com.dbbrowser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.Service;

/**
 * Servlet implementation class MetaDataColumn
 */
@WebServlet("/MetaDataColumn")
public class MetaDataColumn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String url = request.getParameter("url");
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String db = request.getParameter("db");
		String table = request.getParameter("table");
		System.out.println("db = " + db);

		PrintWriter pw = response.getWriter();
		Connection con=null;
		try {

			con = Service.getMetaDataConnections();
			
			ResultSet rs = con.getMetaData().getColumns(db, null,table, "%");
			String columnName = "";
			while (rs.next()) {
				columnName = rs.getString(4);
				pw.println("<div style='margin-left:25%;' id='dbcolumns_"
						+ columnName + "' >" + columnName + "</div>");
			}
			Service.pool2.free(con);
		} catch (SQLException | ClassNotFoundException e) {
			Service.pool2.free(con);
			e.printStackTrace();
		}
	}

}
