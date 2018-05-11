<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<center>
		<h2>Create new Database connection</h2>
	</center>
	<h3>Please provide following details</h3>
	<input type="submit" value="Home" style="margin-left: 50%;" id="home">
	<form method="POST">
		<table>
			<tr>
				<td>Connection Name</td>
				<td><input id="conName" type="text"></td>
			</tr>
			<tr>
				<td>Hostname</td>
				<td><input id="hostname" type="text"></td>
				<td>Port No <input id="portNo" type="number"></td>
			</tr>
			<tr>
				<td>Username</td>
				<td><input id="username" type="text"></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input id="password" type="password"></td>
			</tr><tr><td></td></tr>
			<tr>
				<td></td>
				<td><input id="testConnection" type="button"
					value="Test Connection"></td>
				<td><input id="submit" type="button" value="Create"></td>
			</tr>
		</table>
	</form>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="//code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>

	<script type="text/javascript">
		$(document).ready(function() {

			var data = {
				"conName" : "conName",
				"hostname" : "hostname",
				"portNo" : "portNo",
				"username" : "username",
				"password" : "password"
			};

			$("#submit").click(function() {

				var conName = $('#conName').val();
				var hostname = $('#hostname').val();
				var portNo = $('#portNo').val();
				var username = $('#username').val();
				var password = $('#password').val();

				$.ajax({
					url : 'AddConnection',
					data : {
						conName : conName,
						hostname : hostname,
						portNo : portNo,
						username : username,
						password : password
					},
					type : 'POST',
					dataType : 'text',

					error: function (xhr, status) {
					    alert(status);
					},
					success : function(result) {
						alert(result);
						window.location='/DBBrowser/login.jsp';
					}
				});

			});

			$('#home').click(function() {
				document.location = "index.jsp";
			});

			$('#testConnection').click(function(e) {
				e.stopPropagation();
				//alert('test');
				
				var conName = $('#conName').val();
				var hostname = $('#hostname').val();
				var portNo = $('#portNo').val();
				var username = $('#username').val();
				var password = $('#password').val();
				
				$.ajax({
					url : 'TestConnection',
					data : {
						conName : conName,
						hostname : hostname,
						portNo : portNo,
						username : username,
						password : password
					},
					type : 'POST',
					dataType : 'html',
					success : function(result) {
						alert(result);

					},
					error: function (xhr, status) {
					    alert('Error!');
					}
				});

			});
		});
	</script>

</body>
</html>

