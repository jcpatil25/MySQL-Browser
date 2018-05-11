package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class ConnectionPool implements Runnable{

	private String driver;
	private String url; 
	private String uname;
	private String pass; 
	
	private int maxConnections;
	private int initialConnections;
	private boolean waitIfBusy;
	private boolean connectionPending = false;
	
	public Vector<Connection> availableConnections, busyConnections;
	
	public ConnectionPool(String driver,String url,String uname,String pass, int maxConnections, int initialConnections, boolean waitIfBusy) {
		
		this.driver = driver;
		this.url = url;
		this.uname = uname;
		this.pass = pass;
		this.maxConnections =maxConnections;
		this.initialConnections = initialConnections;
		this.waitIfBusy = waitIfBusy;
		
		availableConnections = new Vector<Connection>();
		busyConnections = new Vector<Connection>();
		
		for(int i=0;i<initialConnections;i++){
			availableConnections.addElement(createNewConnection());
		}
	}

	private Connection createNewConnection() {
		Connection con =null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url,uname,pass);
			
		}catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
		System.out.println("create connection called");

		return con;
	}
	
	public synchronized Connection getConnection() throws SQLException{
		System.out.println("get connection called");
		Connection existingConnection=null;
		if(!availableConnections.isEmpty()){
			 existingConnection = availableConnections.lastElement();
			
			int lastIndex = availableConnections.size()-1;
			availableConnections.removeElementAt(lastIndex);
			
			if(existingConnection.isClosed()){
				notifyAll();
				return getConnection();
			}else{
				busyConnections.addElement(existingConnection);
				return existingConnection;
			}
			
		}else{
			if(initialConnections<maxConnections && !connectionPending){
				createBackgroundConnection();
			}
			else if(availableConnections.size()>=maxConnections){
				throw new SQLException("Connection Limit Reached");

			}else if(!waitIfBusy){
				throw new SQLException("Connection Busy !");
			}
		}
		try{
			wait();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return getConnection();
	}

	private void createBackgroundConnection() {
		connectionPending = true;
		Thread connectThread = new Thread(this);
		connectThread.start();
	}

	@Override
	public void run() {

		try {
			Connection con = getConnection();
			synchronized(this){
				availableConnections.addElement(con);
				connectionPending = false;
				notifyAll();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void free(Connection con){
		busyConnections.removeElement(con);
		availableConnections.addElement(con);
		notifyAll();
	}
	
	public int totalConnections(){
		return (availableConnections.size()+busyConnections.size());
	}
	public synchronized void closeAllConnections() throws SQLException{
		closeConnection(availableConnections);
		availableConnections = new Vector<Connection>();
		closeConnection(busyConnections);
		busyConnections = new Vector<Connection>();
	}
	public void closeConnection(Vector<Connection> con) throws SQLException{
		for(int i=0;i<con.size();i++){
			Connection connection = con.elementAt(i);
			if(!connection.isClosed()){
				connection.close();
			}
		}
	}

}
 