<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="com.db.Service"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>

<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />
<style>
	.indexCSS{
		margin-left:12%;
		padding:1%;
		height:80%;
		border:1px solid black;
		width:20%;
	}
	html,body { height:100% ;}
</style>

</head>
<body class='indexCSS2'>
	<center><h2>Welcome to MySQL</h2></center>
	<div class ="indexCSS">
	<a href="login.jsp">Create new connection</a>
	<div><br></div>
	<div>OR Use the following connections</div><br>

	<%
		ResultSet rs = Service.getAllConnections();

		while (rs.next()) {
	%>
	<div 
		onclick="authenticate('<%=rs.getInt(1)%>','<%=rs.getString(5)%>','<%=rs.getString(6)%>','<%=rs.getString(3)%>','<%=rs.getInt(4)%>')"><%=rs.getString(2)%></div>
	<%
		}
		rs.close();
	%>
	</div>
	<div id="dialog" style="display: none">
		<form action="browser.jsp" id="submitForm" class="hideField" method="post">
			<label class="hideField">Enter Password for </label><span id="forUname"></span><br> <input
				type="password" id="pass">
				<input style="display:none" type="text" id ="hiddId" name="hiddId">
				<input style="display:none" type="text" id ="hiddURL" name="hiddURL">
				<input style="display:none" type="text" id ="hiddUname" name="hiddUname">
				<input style="display:none" type="text" id ="hiddPass" name="hiddPass">
		</form>
	</div>
	<script>
		$(document).ready(function() {
			//alert('hi');
		});
		function authenticate(id, uname, pass,host,port) {
			//alert('hello- '+id+ " un- "+uname+" pass- "+pass);
			$('.hideField').display = "block";
			$('#forUname').text(uname);
			
			$('#dialog').dialog({
				modal : false,
				autoOpen : true,
				title : "Connect to MySQL Server",
				buttons : {
					"Connect" : function() {
						var upass = $('#pass').val();
						if (upass == pass) {
							var url = "jdbc:mysql://"+host+":"+port;
							
							//alert(url);
							$('#hiddId').val(id);
							
							$('#hiddURL').val(url);
							$('#hiddUname').val(uname);
							$('#hiddPass').val(pass);
							
							$.ajax({
								url:'AssignConnectionPool',
								data:{url:url,uname:uname,pass:pass},
								type:'POST',
								dataType:'html',
								success:function(result){
									console.log('success '+result);
								},
								error: function(){
									alert('Error!');
								}
								
							});
							setTimeout(function(){
								$('#submitForm').submit();
								$(this).dialog("close");	
							},200);
							
						} else{
							$('#pass').val('');
							$('#pass').focus();
							
							alert("Incorrect username or password");
						}
						
					}
				}
			});
		}
	</script>
</body>
</html>