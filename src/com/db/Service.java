package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Service {

	public static final ConnectionPool pool1 = new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/dbbrowser", "root", "123", 100, 10, true);
	public static ConnectionPool pool2 = null;
	
	public Connection getConnection() throws SQLException{
				
		Connection con = pool1.getConnection();
		//System.out.println("Total connections = "+ pool1.totalConnections());
		return con;
	}
	
	
	public String testConnection(String conName,String hostname,int portNo, String username,String password)throws ClassNotFoundException, SQLException{
		
		String str="";
		Connection con=null;
		try {
			String url = "jdbc:mysql://"+hostname+":"+portNo;
			//System.out.println(url);

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,username,password);
			str = "Connected to MySQL at "+hostname +" with user "+ username;	
			con.close();
		} catch(IllegalArgumentException e){
			str = e.getMessage();
			con.close();
		}catch (SQLException e) {
				e.printStackTrace();
				str = e.getMessage();
				con.close();
		}
		return str;
		
	}
	public int addConnection(String conName,String hostname,int portNo, String username,String password)throws SQLException{

		Connection conn = pool1.getConnection();
		String sql = "insert into connections (con_name,hostname,port_no,username,password) values(?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, conName);
		stmt.setString(2, hostname);
		stmt.setInt(3, portNo);
		stmt.setString(4, username);
		stmt.setString(5, password);
		pool1.free(conn);
		int rs = stmt.executeUpdate();
		
		stmt.close();
		return rs;
		
	}
	public static ResultSet getAllConnections() throws SQLException{
		String sql = "select * from connections";
		Connection con = pool1.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		pool1.free(con);
		
		return rs;
		
	}
	
	public static Connection getMetaDataConnections() throws SQLException,ClassNotFoundException{
		
		Connection con = pool2.getConnection();
		return con;
	}
	
	public static Connection getSingleConnection(String url,String uname, String pass) throws SQLException,ClassNotFoundException{
		System.out.println("URL = "+ url);
		
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection(url,uname,pass);
		return con;
	}
	
	public static synchronized void createNewConnectionPool(String driver,String url,String uname,String pass, int maxConnections, int initialConnections, boolean waitIfBusy){
	
		pool2 = new ConnectionPool(driver, url, uname, pass, maxConnections, initialConnections, waitIfBusy);
	}
	public static void closeConnections() throws SQLException{
		//pool1.closeAllConnections();
		pool2.closeAllConnections();
	}
	
}
