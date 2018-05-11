package com.dbbrowser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.Service;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * Servlet implementation class ExecuteQuery
 */
@WebServlet("/ExecuteQuery")
public class ExecuteQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String query = request.getParameter("query");
		String url = request.getParameter("url");
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String db = request.getParameter("db");
		String table = request.getParameter("table");
		String queryType = request.getParameter("queryType");

		System.out.println("table = " + table);

		System.out.println(query + ", db= " + db);

		System.out.println(queryType);

		PrintWriter pw = response.getWriter();

		url += '/' + db;
		System.out.println(url);
		Connection con2 = null;
		try {
			con2 = Service.getSingleConnection(url, uname, pass);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			pw.println(e1.getMessage());
			try {
				con2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			pw.println(e1.getMessage());
			try {
				con2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Connection  con =null;
		if (queryType.equals("select")) {
			try {

				con = Service.getMetaDataConnections();

				ResultSet rs = con.getMetaData().getColumns(db, null, table,
						"%");

				String columnName = "";
				pw.println("<table style='border:1px solid;width:100%;border-collapse:collapse'><tr>");
				int columnCount = 0;
				while (rs.next()) {
					columnName = rs.getString(4);
					pw.println("<th style='border:1px solid;' id='dbcolumns_"
							+ columnName + "' >" + columnName + "</th>");
					columnCount++;
				}

				pw.println("</tr>");
				rs.close();
				Service.pool2.free(con);

				java.sql.PreparedStatement stmt = con2.prepareStatement(query);

				ResultSet res = stmt.executeQuery();

				while (res.next()) {

					pw.println("<tr>");

					for (int i = 1; i <= columnCount; i++) {
						pw.println("<td>" + res.getString(i) + "</td>");
					}

					pw.println("</tr>");
				}
				pw.println("</tr></table>");
				stmt.close();
				res.close();
				con2.close();

				// System.out.println(md.getCatalogs().getMetaData().getCatalogName(0));
			} catch (SQLException | ClassNotFoundException e) {
				try {
					con2.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				Service.pool2.free(con);
				e.printStackTrace();
				pw.println(e.getMessage());
			}

		}else{
			try {
				Statement stmt = con2.createStatement();
				int rs = stmt.executeUpdate(query);
				
					pw.println(query+" =>   "+ rs +" row(s) affercted");
					stmt.close();
					con2.close();
			}catch (SQLException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				pw.println("Error Code : "+e.getErrorCode()+" , ");
				pw.println(e.getMessage());
				
			}
		}

	}
}
