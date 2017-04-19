<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>cls</title>
<link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/toastr.js/latest/toastr.min.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/toastr.js/latest/toastr.min.js"></script>
<script type="text/javascript">
			function reset(form) {
				$("#confirmPassword").val('');
				$("#newPassword").val('');
				$("#oldPassword").val('').focus();
			};
			function check(form) {
				if (form.oldPassword.value == '' || form.newPassword.value == '' || form.confirmPassword.value == '') {
					toastr.warning("请填写所有信息！", "提示");
					return false;
				}
				if (form.newPassword.value != form.confirmPassword.value) {
					toastr.error("两次输入的密码不一致，请检查！", "提示");
					return false;
				}
				if (form.newPassword.value.length < 6 || form.newPassword.value.length > 32) {
					toastr.info("密码长度不正确，请输入 6-32 字符！", "提示");
					return false;
				}
				$("#modal-footer").hide(0);
				$("#updatePassword").hide(0);
				$("#progress").attr("style", false);
				return true;
			};
			$(document).ready(function() {
				$.post("user/getBaseInfoById?${_csrf.parameterName}=${_csrf.token}", {}, function(data, status) {
					if (status == "success") {
						try {
							var json = JSON.parse(data);
							if (json.state == 1) {
								document.getElementById("sid").innerHTML=json.student_id;
								$("#name").empty();
								if (json.sex == "男") {
									$("#name").append("<font>"+json.name+'</font><font style="font-family:Microsoft JhengHei;font-weight:bold;color:#3366FF">&nbsp;♂</font>');
								}
								else if (json.sex == "女") {
									$("#name").append("<font>"+json.name+'</font><font style="font-family:Microsoft JhengHei;font-weight:bold;color:#FF99FF">&nbsp;♀</font>');
								}
								$("#user").attr("value", json.student_id + " - " + json.name);
							}
							else {
								toastr.error("未授权，请重新登录！", "警告");
								setTimeout("location.href = 'login'", 1000);
							}
						} catch (e) {
							toastr.error("未授权，请重新登录！", "警告");
							setTimeout("location.href = 'login'", 1000);
						}
					}
					else {
						toastr.error("服务器故障！", "错误");
					}
				});
				$("#reset").click(function() {
					reset(updatePassword);
				});
				$("#save").click(function() {
					if (check(updatePassword))
						$("#updatePassword").submit();
				});
				$("#updatePassword").ajaxForm(function(data) {
					reset(updatePassword);
					try {
						var json = JSON.parse(data);
						setTimeout(function() {
							$("#myModal").modal("hide");
							$("#modal-footer").show();
							$("#updatePassword").show();
							$("#progress").attr("style", "display:none");
							if (json.state == 1) {
								toastr.success("修改密码成功！", "提示");
							}
							else {
								toastr.error("修改失败，请检查原密码！", "警告");
							}
						}, 1000);
					} catch (e) {
						$("#myModal").modal("hide");
						$("#modal-footer").show();
						$("#updatePassword").show();
						$("#progress").attr("style", "display:none");
						toastr.error("修改失败，请检查原密码！", "警告");
					}
				});
			});
		</script>
</head>
<body>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="javascript:void(0)" onclick='location.href="."'>e</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="javascript:void(0)"><span class="glyphicon glyphicon-user">&nbsp;信息</span></a></li>
					<li onclick='location.href="user/task"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-tasks">&nbsp;作业</span></a></li>
					<li onclick='location.href="user/stayUpdate"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-record">&nbsp;离校</span></a></li>
					<li onclick="javascript:logoutForm.submit()"><a href="javascript:void(0)"><span class="glyphicon glyphicon-log-out">&nbsp;退出</span></a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container" style="justify-content: center; align-items: center; margin-top: 100px; font-size: 120%">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<h1 class="text-center">个人信息</h1>
				<h2></h2>
				<table class="table table-striped table-bordered">
					<tr>
						<td><center><b>学&nbsp;号</b></center></td>
						<td id="sid">undefined</td>
						<td><center></center></td>
					</tr>
					<tr>
						<td><center><b>姓&nbsp;名</b></center></td>
						<td id="name"><font>undefined</font></td>
						<td><center></center></td>
					</tr>
					<tr>
						<td><center><b>密&nbsp;码</b></center></td>
						<td>●●●●●</td>
						<td><center><button type="button" class="btn btn-xs btn-warning" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button></center></td>
					</tr>
				</table>
				<p></p>
			</div>
			<div class="col-md-3"></div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;修改密码</h4>
				</div>
				<div class="modal-body">
					<div id="progress" style="display:none">
						<div>
							<p class="text-center"><strong>正在修改密码...</strong></p>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div>
						</div>
					</div>
					<form id="updatePassword" formname="updatePassword" method="post" action="user/updatePassword">
						<div class="form-group">
							<label for="username">当前用户：</label> <input id="user" type="text" class="form-control" value="undefined" disabled="true">
						</div>
						<div class="form-group">
							<label for="oldPassword">当前密码：</label> <input type="password" class="form-control" id="oldPassword" name="oldPassword" placeholder="您账户的当前密码">
						</div>
						<div class="form-group">
							<label for="newPassword">新密码：</label> <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="新密码（ 6-32 字符，字母数字组合）">
						</div>
						<div class="form-group">
							<label for="confirmPassword">确认密码：</label> <input type="password" class="form-control" id="confirmPassword" placeholder="再次输入您的新密码">
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
					</form>
				</div>
				<div id="modal-footer" class="modal-footer">
					<button id="reset" type="button" class="btn btn-sm btn-default"> <span class="glyphicon glyphicon-repeat"></span>&nbsp;清空</button>
					<button id="save" type="button" class="btn btn-sm btn-success"><span class="glyphicon glyphicon-ok"></span>&nbsp;保存</button>
					<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>&nbsp;放弃</button>
				</div>
			</div>
		</div>
	</div>
	<!--Model-->
	<c:url value="/logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /></form>
	<script src="http://cdn.bootcss.com/jquery.form/3.51/jquery.form.min.js"></script>
	<script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>