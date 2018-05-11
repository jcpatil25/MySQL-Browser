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
import com.mysql.jdbc.DatabaseMetaData;

/**
 * Servlet implementation class MetaData
 */
@WebServlet("/MetaData")
public class MetaData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("--------***----------");

		
		PrintWriter pw = response.getWriter();
		
//		int id = Integer.parseInt(request.getParameter("hiddId"));
		String url = request.getParameter("url");
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String db = request.getParameter("db");
		System.out.println("db = "+db);
//		System.out.println(url);
		ResultSet rs = null;
		Connection  con=null;
		try {
						
			con = Service.getMetaDataConnections(); 
			String[] types = { "TABLE" };
			rs = con.getMetaData().getTables(db, null, "%", types);
			String tableName = "";
		     while (rs.next()) {
		       tableName = rs.getString(3);
		       pw.println("<div style='margin-left:16%;' id='dbtables_"+tableName+"' ondblclick='getColumns(\""+tableName+"\",\""+db+"\")'>"+tableName+"</div><div style='display:none' id='dbcolumns_"+db+"_"+tableName+"' ></div>");
		     }

		     Service.pool2.free(con);
			/*while (rs.next()) {
			    System.out.println("TABLE_CAT = " + rs.getString("TABLE_CAT") );
			    pw.println("<div><a href=''>"+rs.getString("TABLE_CAT")+"</a></div>");
//			    pw.println();
			    sb.append("<div><a href=''>"+rs.getString("TABLE_CAT")+"</a></div>");
			
			}*/

			//System.out.println(md.getCatalogs().getMetaData().getCatalogName(0));
		} catch (SQLException | ClassNotFoundException e) {
		
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Service.pool2.free(con);
			e.printStackTrace();
		}
	}
	
}
