<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="com.db.Service"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.db.ConnectionPool" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>dbbrowser</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>


<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />
<style>
.schemas {
	margin-left: 8%;
}

.leftDiv {
	width: 20%;
	height: 80%;
	border: 1px solid black;
	float: left;
	padding: 1%;
}

.rightDiv {
	width: 75%;
	height: 80%;
	border: 1px solid black;
	float: right;
	padding: 1%;
}

textarea {
	width: 98%;
	height: 50%;
	padding: 1%;
}

#execute {
	margin: 1%;
}

.schemas_list {
	/* -webkit-user-select:none;
	-moz-user-select:none;
	-ms-user-select:none;
	user-select:none; */
	
}

html, body {
	height: 100%;
	margin-left: 5%;
}

#result {
	margin-top: 1%;
}

#result td {
	border: 1px solid;
	text-align: center;
}
</style>
</head>
<body>
	<center>
		<h2>Welcome to DB Browser</h2>
	</center>

	<%
		//System.out.println("---- DBBrowser -----------");
	
	
		int id = 0;
		PrintWriter pw = response.getWriter();
		String url=null, uname=null,pass=null;
		try {
			id = Integer.parseInt(request.getParameter("hiddId"));
			url = request.getParameter("hiddURL");
			uname = request.getParameter("hiddUname");
			pass = request.getParameter("hiddPass");
			System.out.println("url = " + url);
			} catch (NumberFormatException | NullPointerException e) {
			response.sendRedirect("index.jsp");
		}
		
		Connection con = null;
		try {
			con = Service.getMetaDataConnections();

			java.sql.DatabaseMetaData md = con.getMetaData();

			ResultSet rs = md.getCatalogs();
			
			StringBuffer sb = new StringBuffer();
	%>
	
	<div class="leftDiv">
		<div class='schemas'>
			<b>Schemas</b>
		</div>
		<br>
		<%
			while (rs.next()) {
					if (!(rs.getString("TABLE_CAT")
							.equals("information_schema")
							|| rs.getString("TABLE_CAT").equals(
									"performance_schema") || rs.getString(
							"TABLE_CAT").equals("mysql"))) {
		/* 				System.out.println("TABLE_CAT = "
								+ rs.getString("TABLE_CAT"));*/
						sb.append("<div><a href=''>"
								+ rs.getString("TABLE_CAT") + "</a></div>");
		%>
		<div class="schemas schemas_list"
			ondblclick="getTables('<%=rs.getString("TABLE_CAT")%>');selectDB('<%=rs.getString("TABLE_CAT")%>')"><%=rs.getString("TABLE_CAT")%></div>
		<div style="display: none"
			id="dbtables_<%=rs.getString("TABLE_CAT")%>"></div>

		<%
			}
				}
				rs.close();
		%>
	</div>
	<div class="rightDiv">
		<input id="execute" type="submit" value="Execute"><input
			id="clear" type="submit" value="Clear"><input type="submit"
			value="Home" style="float: right" id="home">
		<textarea id="textarea" rows="" cols=""></textarea>
		<div>
			<b>Output</b><br>
		</div>
		<div id="result"></div>
	</div>
	<%
		}catch(NullPointerException e){
			response.sendRedirect("index.jsp");
		}
		catch (SQLException e) {
			Service.pool2.free(con);
			e.printStackTrace();
			}
	%>

	<script>
		$(document).ready(function() {
			//alert("hi");
			$('#clear').click(function(){
				
				$('textarea').val('');
			});
			
			$('#home').click(function(){
				$.ajax({
					url:'CloseConnections',
					type:'POST',
					success: function(){
						console.log('Connections closed!');
					},
					error: function(){
						alert('Error!');
					}
				});
				window.location = '/DBBrowser/index.jsp';
			});
			
			$('#execute').click(function(e){
				e.preventDefault();
				
				var query = $('textarea').val();
				
				var x = $('*').filter(function() {
				    return $(this).css("text-decoration").toLowerCase().indexOf('underline') > -1;
				});				

				if(x[0] == null){
					alert('Please select database first!');
					return;
				}else{

					var db = x[0].outerText;

					console.log("dbname="+db);
					var textarea = $('#textarea').val();
					if(textarea ==''){
						alert('Enter query');
					}

					var sell= window.getSelection();
					if(sell==''){
						sell = $('#textarea').val().split('\n')[0];
					} 
					
					console.log("sell="+sell);

					executeQuery(db,sell);
				}
			});
			
			
			
		});

		
		function getTables(db) {
			var url = "<%=url%>";
			var uname = "<%=uname%>";
			var pass = "<%=pass%>";
			 $( "#dbtables_"+db ).toggle();
			//alert("ok - "+db+ ", url- "+url);
			$.ajax({
				url:'MetaData',
				data:{db:db,url:url,uname:uname,pass:pass},
				type:'POST',
				dataType:'html',
				success: function(result){
					//alert("#dbtables_"+db);
					
					$("#dbtables_"+db).html(result);
				}
			});
		}
		
		function getColumns(table,db){
			var url = "<%=url%>";
			var uname = "<%=uname%>";
			var pass = "<%=pass%>";
			$("#dbcolumns_" + db + "_" + table).toggle();
			//alert("ok - "+db+ ", url- "+url);
			$.ajax({
				url : 'MetaDataColumn',
				data : {
					db : db,
					url : url,
					uname : uname,
					pass : pass,
					table : table
				},
				type : 'POST',
				dataType : 'html',
				success : function(result) {
					//alert("#dbcolumns_"+table);

					$("#dbcolumns_" + db + "_" + table).html(result);
				}
			});
		}
		function selectDB(db) {

			$(".schemas_list span").css({
				"text-decoration" : "none",
				"font-style" : "normal",
				"font-weight" : "normal"
			});
			var mytext = selectHTML(db);

			$('.span_' + db).css({
				"text-decoration" : "underline",
				"font-style" : "italic",
				"font-weight" : "bold"
			});
		}

		function selectHTML(db) {
			try {
				if (window.ActiveXObject) {
					var c = document.selection.createRange();
					return c.htmlText;
				}

				var nNd = document.createElement("span");
				var w = getSelection().getRangeAt(0);
				nNd.setAttribute('class', 'span_' + db);
				w.surroundContents(nNd);
				return nNd.innerHTML;
			} catch (e) {
				if (window.ActiveXObject) {
					return document.selection.createRange();
				} else {
					return getSelection();
				}
			}
		}
		
		function executeQuery(db,query){
			
			query += "\n";

			var url = "<%=url%>";
			var uname = "<%=uname%>";
			var pass = "<%=pass%>";
			var table = query.split("from ").pop().split(' ').shift();
			var queryType = query.split(" fff").pop().split(' ').shift();

			console.log('queryType=' + queryType);
			if (table == "insert") {
				table = query.split("into ").pop().split(' ').shift();
			}
			queryType = queryType.trim();
			table = table.trim();
			console.log("table = " + table)

			$.ajax({
				url : 'ExecuteQuery',
				data : {
					db : db,
					query : query,
					url : url,
					uname : uname,
					pass : pass,
					table : table,
					queryType : queryType
				},
				type : 'POST',
				dataType : 'html',
				success : function(result) {
					console.log(result);
					$('#result').html(result);
				},
				error : function(xHr, status) {
					alert(status);

				}
			});
		}
	</script>
</body>
</html>