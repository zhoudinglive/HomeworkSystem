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
<link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/toastr.js/latest/toastr.min.css" rel="stylesheet">
<link href="../css/bootstrap-datetimepicker.min.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/toastr.js/latest/toastr.min.js"></script>
<script type="text/javascript">
	var taskCount = 0, singleCount = 5, totalPages = 1, currentPage = 1;
	var dataObj;
	$(document).ready(function() {
		write();
		$('.form_date').datetimepicker({
			language:  'zh-CN',
			weekStart: 0,
			todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
		});
		$("#update").ajaxForm(function(data,status) {
			try {
				var returnJson = JSON.parse(data);
				if (returnJson.state == 1) {
					toastr.success("保存成功！","提示");
				} else {
					toastr.error("保存失败！","提示");
				}
			} catch (e) {
				toastr.error("保存失败！","提示");
			}
		});
	});
	function write() {
		$.post("getAllStayInfo?${_csrf.parameterName}=${_csrf.token}",{},function(data,status) {
			if (status == "success") {
				try {
					var json = JSON.parse(data);
					if (typeof (json.state) == "undefined") {
						dataObj = json.stays;
						taskCount = dataObj.length;
						totalPages = Math.ceil(taskCount / singleCount);
						if (totalPages == 0) {
							totalPages++;
						}
						currentPage = 1;
						writeItems(currentPage);
						writePages(currentPage);
						writeAction();
						toastr.success("更新列表成功！","提示");
					} else if (json.state != 1) {
						toastr.error("获取列表失败！","警告");
					} else {
						currentPage = 1;
						writeItems(currentPage);
						writePages(currentPage);
						writeAction();
						toastr.success("更新列表成功！","提示");
					}
				} catch (e) {
					toastr.error("获取列表失败！","警告");
				}
			} else {
				toastr.error("服务器故障！","错误");
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
		var begin = singleCount*(current-1);
		var end = (begin+singleCount>taskCount ? taskCount : begin+singleCount);
		for (var i = begin; i < end; i++) {
			html += '<tr>';
			html += '<td><center>' + dataObj[i].stay_id + '</center></td>';
			html += '<td>' + dataObj[i].stay_name + '</td>';
			html += '<td><center>' + dataObj[i].stay_deadline + '</center></td>';
			if (dataObj[i].cut_off == "0") {
				html += '<td><center><span class="label label-danger">截止</span></center></td>';
			} else {
				html += '<td><center><span class="label label-success">可用</span></center></td>';
			}
			html += '<td><center><button id="stay' + dataObj[i].stay_id + '" type="button" class="btn btn-xs btn-info" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-check"></span>&nbsp;我要登记</button></center></td>';
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
			var stayID = $(this).attr("id").substring(4);
			$.post("getStayInfoById?${_csrf.parameterName}=${_csrf.token}",{"stay_id":stayID},function(data,status) {
				if (status == "success") {
					var json = JSON.parse(data);
					if (typeof (json.state) == "undefined") {
						document.getElementById("stay_name").innerHTML = "标题：("+json.stay_id+") - "+json.stay_name;
						document.getElementById("stay_deadline").innerHTML = "截止时间："+json.stay_deadline;
						document.getElementById("stay_details").innerHTML = "说明："+json.stay_details;
						$("#stay_id").attr("value",json.stay_id);
						if (json.cut_off == "0") {
							document.getElementById("cut_off").innerHTML = "状态：截止";
							$("#update").hide();
						} else {
							document.getElementById("cut_off").innerHTML = "状态：可用";
							$("#update").show();
						}
						$.post("getMyUpdated?${_csrf.parameterName}=${_csrf.token}",{"stay_id":stayID},function(data,status) {
							var json1 = JSON.parse(data);
							if (typeof (json1.state) == "undefined") {
								$("#begin").val(json1.stay_start.substring(0,10));
								$("#stop").val(json1.stay_stop.substring(0,10));
								$("#reason").val(json1.leave_details);
							}
							else {
								$("#begin").val(json.stay_begintime.substring(0,10));
								$("#stop").val(json.stay_stoptime.substring(0,10));
							}
						});
					} else {
						toastr.error("获取详情失败！","警告");
					}
				}
			});
		});
	};
</script>
</head>

<body>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="javascript:void(0)" onclick='location.href=".."'>e</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li onclick='location.href=".."'><a href="javascript:void(0)"><span class="glyphicon glyphicon-user">&nbsp;信息</span></a></li>
					<li onclick='location.href="task"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-tasks">&nbsp;作业</span></a></li>
					<li class="active"><a href="javascript:void(0)"><span class="glyphicon glyphicon-record">&nbsp;离校</span></a></li>
					<li onclick="javascript:logoutForm.submit()"><a href="javascript:void(0)"><span class="glyphicon glyphicon-log-out">&nbsp;退出</span></a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container" style="justify-content:center;align-items:center;margin-top:100px">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="panel panel-primary">
					<div class="panel-heading">离校登记</div>
					<div class="panel-body">
						<nav>
							<ul id="pages" class="pagination"></ul>
						</nav>
						<table class="table">
							<tr>
								<th><center>编号</center></th>
								<th><center>名称</center></th>
								<th><center>截止时间</center></th>
								<th><center>状态</center></th>
								<th><center>操作</center></th>
							</tr>
							<tbody id="tasks"></tbody>
						</table>
						<div style="text-align:right">
							<a type="button" class="btn btn-warning" onclick='location.href="../admin/createStay"'><span class="glyphicon glyphicon-stats"></span>&nbsp;统计信息</a>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;离校登记</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<div><label id="stay_name"></label></div>
						<!--div><label id="stay_createtime"></label></div-->
						<div><label id="stay_deadline"></label></div>
						<div><label id="cut_off"></label></div>
						<div><label id="stay_details"></label></div>
					</div>
					<form id="update" action="updateTime?${_csrf.parameterName}=${_csrf.token}" method="post" onsubmit='jsvascript:$("#myModal").modal("hide");$("#begin").val($("#begin").val()+" 0:0:0");jsvascript:$("#stop").val($("#stop").val()+" 0:0:0")'>
						<input id="stay_id" type="hidden" name="stay_id">
						<h4><span class="label label-warning">&nbsp;+++++&nbsp;请认真填写以下信息&nbsp;+++++&nbsp;</span></h4>
						<div class="form-group"><label>请假开始于：</label><div class="input-group date form_date col-md-5" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1"><input class="form-control" id="begin" name="stay_start" placeholder="开始于" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></div>
						<div class="form-group"><label>请假结束于：</label><div class="input-group date form_date col-md-5" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2"><input class="form-control" id="stop" name="stay_stop" placeholder="结束于" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></div>
						<div class="form-group"><label>离校事由：</label><input id="reason" class="form-control" name="leave_details" type="text" placeholder="离校事由"></div>
						<h4><span class="label label-info">首次申请请填写离校事由！</span></h4>
						<p class="help-block">填写完成后，点击“提交”。</p>
						<div class="form-group"><button type="submit" class="btn btn-sm btn-success" value="提交"><span class="glyphicon glyphicon-send"></span>&nbsp;提交</button></div>
					</form>
				</div>
				<div id="modal-footer" class="modal-footer">
					<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>&nbsp;关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!--Model-->
	
	<c:url value="/logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"></form>
	<script src="http://cdn.bootcss.com/jquery.form/3.51/jquery.form.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>