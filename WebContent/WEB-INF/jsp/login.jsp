<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>cls</title>
<style type="text/css">body{background:#F7FAFC;overflow:hidden}</style>
<link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/toastr.js/latest/toastr.min.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/toastr.js/latest/toastr.min.js"></script>
<script type="text/javascript">
	$.post("login_session?${_csrf.parameterName}=${_csrf.token}", {}, function(data, status) {
		if (status == "success") {
			try {
				var json = JSON.parse(data);
				if (json.state == 1) {
					location.href = "./";
				}
			} catch (e) {
				;
			}
		}
	});
	$(document).ready(function() {
		$("#loginForm").ajaxForm(function(data) {
			var returnJson;
			try {
				returnJson = JSON.parse(data);
				if (returnJson.state == 1) {
					toastr.success("登录成功！", "提示");
					setTimeout("location.href = '.'", 1000);
				} else {
					toastr.error("登录失败，请检查用户名及密码！", "提示");
				}
			} catch (e) {
				toastr.error("登录失败，请检查用户名及密码！", "提示");
			}
		});
	});
	function check(form) {
		reg = /^[0-9]+$/;
		if (!reg.test(form.username.value)) {
			toastr.error("请输入正确的学号！", "提示");
			form.username.focus();
			return false;
		}
		if (form.password.value == '') {
			toastr.warning("请输入密码！", "提示");
			form.password.focus();
			return false;
		}
		return true;
	};
</script>
</head>
<body>
	<canvas id="canvas" style="position:absolute;z-index:-1;top:0px;left:0px"></canvas>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="javascript:void(0)" onclick='location.href="login"'>e</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="javascript:void(0)"><span class="glyphicon glyphicon-log-in">&nbsp;系统登录</span></a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>
	<div class="container" style="justify-content: center; align-items: center; margin-top: 150px">
		<div class="row">
			<div class="col-sm-4 col-md-4 col-lg-4"></div>
			<div class="col-sm-4 col-md-4 col-lg-4">
				<form id="loginForm" formname="loginForm" method="post" action="<c:url value='/login' />" onsubmit="return check(this)">
					<h2 class="text-center">系统登录</h2>
					<input type="text" name="username" class="form-control" placeholder="学号" autofocus>
					<p></p>
					<input type="password" name="password" class="form-control" placeholder="密码">
					<div class="checkbox">
						<label><input id="remember_me" name="remember-me" type="checkbox" value="on">记住我&nbsp;(7天)</label>
					</div>
					<button class="btn btn-lg btn-primary btn-block" type="submit">登&nbsp;&nbsp;录</button>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				</form>
			</div>
			<div class="col-sm-4 col-md-4 col-lg-4"></div>
		</div>
	</div>
	<!-- /container -->
	<script src="http://cdn.bootcss.com/jquery.form/3.51/jquery.form.min.js"></script>
	<script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var WIDTH = window.innerWidth, HEIGHT = window.innerHeight, POINT = 10;
		var canvas = document.getElementById('canvas');
		canvas.width = WIDTH,
		canvas.height = HEIGHT;
		var context = canvas.getContext('2d');
		context.strokeStyle = 'rgba(0,0,0,0.2)',
		context.strokeWidth = 1,
		context.fillStyle = 'rgba(0,0,0,0.1)';
		var circleArr = [];
		function Line (x, y, _x, _y, o) {
			this.beginX = x,
			this.beginY = y,
			this.closeX = _x,
			this.closeY = _y,
			this.o = o;
		}
		function Circle (x, y, r, moveX, moveY) {
			this.x = x,
			this.y = y,
			this.r = r,
			this.moveX = moveX,
			this.moveY = moveY;
		}		
		function num (max, _min) {
			var min = arguments[1] || 0;
			return Math.floor(Math.random()*(max-min+1)+min);
		}		
		function drawCricle (cxt, x, y, r, moveX, moveY) {
			var circle = new Circle(x, y, r, moveX, moveY)
			cxt.beginPath()
			cxt.arc(circle.x, circle.y, circle.r, 0, 2*Math.PI)
			cxt.closePath()
			cxt.fill();
			return circle;
		}		
		function drawLine (cxt, x, y, _x, _y, o) {
			var line = new Line(x, y, _x, _y, o)
			cxt.beginPath()
			cxt.strokeStyle = 'rgba(0,0,0,'+ o +')'
			cxt.moveTo(line.beginX, line.beginY)
			cxt.lineTo(line.closeX, line.closeY)
			cxt.closePath()
			cxt.stroke();
		}		
		function init () {
			circleArr = [];
			for (var i = 0; i < POINT; i++) {
				circleArr.push(drawCricle(context, num(WIDTH), num(HEIGHT), num(15, 2), num(10, -10)/40, num(10, -10)/40));
			}
			draw();
		}		
		function draw () {
			context.clearRect(0,0,canvas.width, canvas.height);
			for (var i = 0; i < POINT; i++) {
				drawCricle(context, circleArr[i].x, circleArr[i].y, circleArr[i].r);
			}
			for (var i = 0; i < POINT; i++) {
				for (var j = 0; j < POINT; j++) {
					if (i + j < POINT) {
						var A = Math.abs(circleArr[i+j].x - circleArr[i].x),
							B = Math.abs(circleArr[i+j].y - circleArr[i].y);
						var lineLength = Math.sqrt(A*A + B*B);
						var C = 1/lineLength*7-0.009;
						var lineOpacity = C > 0.03 ? 0.03 : C;
						if (lineOpacity > 0) {
							drawLine(context, circleArr[i].x, circleArr[i].y, circleArr[i+j].x, circleArr[i+j].y, lineOpacity);
						}
					}
				}
			}
		}		
		window.onload = function () {
			init();
			setInterval(function () {
				for (var i = 0; i < POINT; i++) {
					var cir = circleArr[i];
					cir.x += cir.moveX;
					cir.y += cir.moveY;
					if (cir.x > WIDTH) cir.x = 0;
					else if (cir.x < 0) cir.x = WIDTH;
					if (cir.y > HEIGHT) cir.y = 0;
					else if (cir.y < 0) cir.y = HEIGHT;
				}
				draw();
			}, 16);
		}
	</script>
</body>
</html>