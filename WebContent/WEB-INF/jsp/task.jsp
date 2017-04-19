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
	var taskCount = 0, singleCount = 5, totalPages = 1, currentPage = 1;
	var dataObj;
	$(document).ready(function() {
		write();
		$("#refresh").click(function(){
			write();
		});
		/*
		$("#upload").ajaxForm(function(data,status) {
			try {
				var returnJson = JSON.parse(data);
				setTimeout(function() {
					$("#myModal").modal("hide");
					$("#modal-footer").show();
					$("#upload").show();
					$("#progress").attr("style", "display:none");
					if (returnJson.state == 1) {
						toastr.success("上传成功！", "提示");
					} else {
						toastr.error("上传失败！", "提示");
					}
				}, 1000);
			} catch (e) {
				$("#myModal").modal("hide");
				$("#modal-footer").show();
				$("#upload").show();
				$("#progress").attr("style", "display:none");
				toastr.error("上传失败！", "提示");
			}
		});
		*/
		$("#fileSubmit").click(function(){
			showProgress();
			$("#upload").ajaxSubmit({
				type:'post',
				url:$("#fileUpload").attr("action"),
				success:function(data){
					try {
						var returnJson = JSON.parse(data);
						setTimeout(function() {
							$("#myModal").modal("hide");
							$("#modal-footer").show();
							$("#upload").show();
							$("#progress").attr("style", "display:none");
							if (returnJson.state == 1) {
								toastr.success("上传成功！", "提示");
							} else {
								toastr.error("上传失败！", "提示");
							}
						}, 1000);
					} catch (e) {
						$("#myModal").modal("hide");
						$("#modal-footer").show();
						$("#upload").show();
						$("#progress").attr("style", "display:none");
						toastr.error("上传失败！", "提示");
					}
				},
				error:function(data){
					$("#myModal").modal("hide");
					$("#modal-footer").show();
					$("#upload").show();
					$("#progress").attr("style", "display:none");
					toastr.error("上传失败！", "提示");
				},
			});
		});
	});
	function write() {
		$.post("getAllHomeworkInfo?${_csrf.parameterName}=${_csrf.token}", {}, function(data, status) {
			if (status == "success") {
				try {
					var json = JSON.parse(data);
					if (typeof (json.state) == "undefined") {
						dataObj = json.homeworks;
						taskCount = dataObj.length;
						totalPages = Math.ceil(taskCount / singleCount);
						if (totalPages == 0) {
							totalPages++;
						}
						currentPage = 1;
						writeItems(currentPage);
						writePages(currentPage);
						writeAction();
						toastr.success("更新列表成功！", "提示");
					} else if (json.state != 1) {
						toastr.error("获取作业列表失败！", "警告");
					} else {
						currentPage = 1;
						writeItems(currentPage);
						writePages(currentPage);
						writeAction();
						toastr.success("更新列表成功！", "提示");
					}
				} catch (e) {
					toastr.error("获取作业列表失败！", "警告");
				}
			} else {
				toastr.error("服务器故障！", "错误");
			}
		});
	};
	function writePages(current) {
		var html = "";
		html += '<li id="btn-previous"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
		for (var i = 1; i <= totalPages; i++) {
			if (i == current) {
				html += '<li id="btn-p' + i + '" class="active"><a href="javascript:void(0)" >' + i + '</a></li>';
			} else {
				html += '<li id="btn-p' + i + '"><a href="javascript:void(0)">' + i + '</a></li>';
			}
		}
		html += '<li id="btn-next"><a href="javascript:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
		$('#pages').empty().append(html);
	};
	function writeItems(current) {
		var html = "";
		var begin = singleCount * (current - 1);
		var end = (begin + singleCount > taskCount ? taskCount : begin
				+ singleCount);
		for (var i = begin; i < end; i++) {
			html += '<tr>';
			html += '<td><center>' + dataObj[i].id + '</center></td>';
			html += '<td>' + dataObj[i].name + '</td>';
			html += '<td><center>' + dataObj[i].deadline + '</center></td>';
			if (dataObj[i].cut_off == 0) {
				html += '<td><center><span class="label label-danger">已截止</span></center></td>';
			} else {
				html += '<td><center><span class="label label-success">可提交</span></center></td>';
			}
			html += '<td><center><button id="task' + dataObj[i].id + '" type="button" class="btn btn-xs btn-info" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-search"></span> 查看</button></center></td>';
			html += '</tr>';
		}
		$('#tasks').empty().append(html);
	};
	function writeAction() {
		$("#pages").on("click", "li", function() {
			var label = $(this).attr("id");
			if (label == 'btn-previous') {
				if (currentPage != 1) {
					currentPage--;
					writePages(currentPage);
					writeItems(currentPage);
				}
			} else if (label == 'btn-next') {
				if (currentPage != totalPages) {
					currentPage++;
					writePages(currentPage);
					writeItems(currentPage);
				}
			} else {
				currentPage = Number(label.substring(5));
				writePages(currentPage);
				writeItems(currentPage);
			}
		});
		$("#tasks").on("click","button",function() {
			var taskID = Number($(this).attr("id").substring(4));
			$.post("getHomeworkInfoById?${_csrf.parameterName}=${_csrf.token}",{"homework_id" : taskID},function(data, status) {
				if (status == "success") {
					var json = JSON.parse(data);
					if (typeof (json.state) == "undefined") {
						document.getElementById("task_id").innerHTML = "作业编号："+json.id;
						document.getElementById("task_title").innerHTML = "作业标题："+json.name;
						document.getElementById("create_time").innerHTML = "创建时间："+json.createtime;
						document.getElementById("stop_time").innerHTML = "截止时间："+json.deadline;
						document.getElementById("task_explanation").innerHTML = "作业说明："+json.details;
						$("#upload").attr("action","<c:url value='homeworkSubmit?homework_id="+json.id+"&${_csrf.parameterName}=${_csrf.token}' />");
						if (json.cut_off == "0") {
							document.getElementById("task_state").innerHTML = "状态：已截止";
							$("#uploadForm").attr("style","display:none");
						} else {
							document.getElementById("task_state").innerHTML = "状态：可提交";
							$("#uploadForm").attr("style",false);
						}
					} else {
						toastr.error("获取作业详情失败！","警告");
					}
				}
			});
		});
	};
	function showProgress() {
		$("#modal-footer").hide(0);
		$("#upload").hide(0);
		$("#progress").attr("style", false);
	};
</script>
</head>

<body>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="javascript:void(0)" onclick='location.href=".."'>e</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li onclick='location.href=".."'><a href="javascript:void(0)"><span class="glyphicon glyphicon-user">&nbsp;信息</span></a></li>
					<li class="active"><a href="javascript:void(0)"><span class="glyphicon glyphicon-tasks">&nbsp;作业</span></a></li>
					<li onclick='location.href="stayUpdate"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-record">&nbsp;离校</span></a></li>
					<li onclick="javascript:logoutForm.submit()"><a href="javascript:void(0)"><span class="glyphicon glyphicon-log-out">&nbsp;退出</span></a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container"
		style="justify-content: center; align-items: center; margin-top: 100px">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="panel panel-primary">
					<div class="panel-heading">作业列表</div>
					<div class="panel-body">
						<nav>
							<ul id="pages" class="pagination"></ul>
						</nav>
						<table class="table">
							<tr>
								<th><center>编号</center></th>
								<th><center>标题</center></th>
								<th><center>截止时间</center></th>
								<th><center>状态</center></th>
								<th><center>操作</center></th>
							</tr>
							<tbody id="tasks"></tbody>
						</table>
						<div style="text-align: right;">
							<a id="refresh" type="button" class="btn btn-success"><span class="glyphicon glyphicon-refresh"></span>&nbsp;刷新列表</a>
							<a type="button" class="btn btn-warning" onclick='location.href="../admin/createHomework"'><span class="glyphicon glyphicon-cog"></span>&nbsp;管理作业</a>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>


	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">
						<span class="glyphicon glyphicon-info-sign"></span> 查看作业
					</h4>
				</div>
				<div class="modal-body">
					<div id="progress" style="display: none">
						<div>
							<p class="text-center">
								<strong>Uploading...</strong>
							</p>
						</div>
						<div class="progress">
							<div
								class="progress-bar progress-bar-success progress-bar-striped active"
								role="progressbar" aria-valuenow="100" aria-valuemin="0"
								aria-valuemax="100" style="width: 100%"></div>
						</div>
					</div>
					<form id="upload" name="upload" action="fileUpload" method="post"
						enctype="multipart/form-data" onsubmit="showProgress()">
						<div>
							<label id="task_id"></label>
							<div>
								<div>
									<label id="task_title"></label>
								</div>
								<div>
									<label id="create_time"></label>
								</div>
								<div>
									<label id="stop_time"></label>
								</div>
								<div>
									<label id="task_state"></label>
								</div>
								<div>
									<label id="task_explanation"></label>
								</div>
								<p></p>
								<div id="uploadForm" class="form-group">
									<p>
										<span class="label label-info">注意：总大小不能超过 100MB</span>
									</p>
									<p>
										<span class="label label-danger">注意：重新提交会覆盖以前的上传</span>
									</p>
									<label for="inputFile">提交你的作业：</label> <input id="file"
										type="file" name="file" multiple>
									<p class="help-block">点击“选择文件”，选择你完成的作业，然后点击“提交”。</p>
									<button id="fileSubmit" type="button" name="submit"
										class="btn btn-sm btn-success" value="提交">
										<span class="glyphicon glyphicon-send"></span> 提交
									</button>
								</div>
					</form>
				</div>
				<div id="modal-footer" class="modal-footer">
					<button type="button" class="btn btn-sm btn-danger"
						data-dismiss="modal">
						<span class="glyphicon glyphicon-remove"></span> 关闭
					</button>
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