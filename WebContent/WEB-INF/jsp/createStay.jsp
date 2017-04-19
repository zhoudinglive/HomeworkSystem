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
		$('.form_datetime').datetimepicker({
			language:  'zh-CN',
			weekStart: 0,
			todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			showMeridian: 1
		});
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
		$("#addTask").ajaxForm(function(data) {
			$("#bbb").val("");
			$("#sss").val("");
			try {
				var returnJson = JSON.parse(data);
				if (returnJson.state == 1) {
					write();
					toastr.success("创建成功！", "提示");
				} else {
					toastr.error("创建失败！", "提示");
				}
			} catch (e) {
				toastr.error("创建失败！", "提示");
			};
		});
		$("#delTask").ajaxForm(function(data) {
			try {
				var returnJson = JSON.parse(data);
				$("#deleteModal").modal("hide");
				if (returnJson.state == 1) {
					write();
					toastr.success("删除成功！", "提示");
				} else {
					toastr.error("删除失败！", "提示");
				}
			} catch (e) {
				toastr.error("删除失败！", "提示");
			};
		});
		$("#editTask").ajaxForm(function(data) {
			try {
				var returnJson = JSON.parse(data);
				$("#editModal").modal("hide");
				if (returnJson.state == 1) {
					write();
					toastr.success("修改成功！", "提示");
				} else {
					toastr.error("修改失败！", "提示");
				}
			} catch (e) {
				toastr.error("修改失败！", "提示");
			};
		});

	});
	function write() {
		$.post("getMyCreateStayInfo?${_csrf.parameterName}=${_csrf.token}",{},function(data,status) {
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
					} else if (json.state != 1) {
						toastr.error("获取列表失败！","警告");
					} else {
						dataObj = null;
						taskCount = 0;
						totalPages = 1;
						currentPage = 1;
						writeItems(currentPage);
						writePages(currentPage);
						writeAction();
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
			html += '<td><center><button id="show' + dataObj[i].stay_id + '" type="button" class="btn btn-xs btn-info" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-search"></span>&nbsp;查看</button>&nbsp;';
			html += '<button id="down' + dataObj[i].stay_id + '" type="button" class="btn btn-xs btn-primary"><span class="glyphicon glyphicon-download"></span>&nbsp;下载</button>&nbsp;';
			html += '<button id="edit' + dataObj[i].stay_id + '" type="button" class="btn btn-xs btn-warning" data-toggle="modal" data-target="#editModal"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>&nbsp;';
			html += '<button id="delete' + dataObj[i].stay_id + '" type="button" class="btn btn-xs btn-danger" data-toggle="modal" data-target="#deleteModal"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</button>';
			html += '</center></td>';
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
			var actionType = $(this).attr("id");
			regShow = /^show/;
			regEdit = /^edit/;
			regDelete = /^delete/;
			regDown = /^down/;
			if (regShow.test(actionType)) {
				var stayID = actionType.substring(4);
				$.post("../user/getStayInfoById?${_csrf.parameterName}=${_csrf.token}",{"stay_id":stayID},function(data,status) {
					if (status == "success") {
						var json = JSON.parse(data);
						if (typeof (json.state) == "undefined") {
							document.getElementById("stay_name").innerHTML = "标题：("+json.stay_id+") - "+json.stay_name;
							document.getElementById("stay_createtime").innerHTML = "创建时间："+json.stay_createtime;
							document.getElementById("stay_deadline").innerHTML = "截止时间："+json.stay_deadline;
							document.getElementById("stay_details").innerHTML = "说明："+json.stay_details;
							$("#begin").val(json.stay_begintime.substring(0,10));
							$("#stop").val(json.stay_stoptime.substring(0,10));
							if (json.cut_off == "0") {
								document.getElementById("cut_off").innerHTML = "状态：截止";
								$("#update").hide();
							} else {
								document.getElementById("cut_off").innerHTML = "状态：可用";
								$("#update").show();
							}
						} else {
							toastr.error("获取详情失败！","警告");
						}
					}
				});
				$.post("getMyCreateStayInfoById?${_csrf.parameterName}=${_csrf.token}",{"stay_id":stayID},function(data,status) {
					if (status == "success") {
						var json = JSON.parse(data).stays;
						var good = "<label>已申请：";
						var bad = "<label>未申请：";
						for (var i=0;i<json.length;i++) {
							if (json[i].updated=="1") {
								good += json[i].student_name+" ";
							}
							else {
								bad += json[i].student_name+" ";
							}
						}
						good += "</label>";
						bad += "</label>";
						$("#submitted").empty().append(good);
						$("#unSubmit").empty().append(bad);
					}
				});
			}
			else if (regDelete.test(actionType)) {
				var stayID = actionType.substring(6);
				$("#delTaskById").attr("value",stayID);
			}
			else if (regEdit.test(actionType)) {
				$("#edit_id").val('');
				$("#edit_title").val('');
				$("#edit_deadline").val('');
				$("#edit_details").val('');
				var taskID = actionType.substring(4);
				$("#edit_id").val(taskID);
				$.post("../user/getStayInfoById?${_csrf.parameterName}=${_csrf.token}",{"stay_id":taskID},function(data,status) {
					if (status == "success") {
						var json = JSON.parse(data);
						if (typeof (json.state) == "undefined") {
							$("#edit_title").val(json.stay_name);
							$("#edit_deadline").val(json.stay_deadline);
							$("#edit_details").val(json.stay_details);
						}
						else {
							toastr.error("获取详情失败！","警告");
						}
					}
				});
			}
			else if (regDown.test(actionType)) {
				window.open("downloadStay?stay_id="+actionType.substring(4));
			}
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
					<li onclick='location.href="../user/task"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-tasks">&nbsp;作业</span></a></li>
					<li class="active" onclick='location.href="../user/stayUpdate"'><a href="javascript:void(0)"><span class="glyphicon glyphicon-record">&nbsp;离校</span></a></li>
					<li onclick="javascript:logoutForm.submit()"><a href="javascript:void(0)"><span class="glyphicon glyphicon-log-out">&nbsp;退出</span></a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container" style="justify-content:center;align-items:center;margin-top:100px">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="panel panel-info">
					<div class="panel-heading">统计信息</div>
					<div class="panel-body">
						<div style="text-align:right;"><a type="button" class="btn btn-success" data-toggle="modal" data-target="#addModal" onclick='$("#bbb").val("");$("#sss").val("")'><span class="glyphicon glyphicon-plus"></span>&nbsp;添加记录</a></div>
						<nav><ul id="pages" class="pagination"></ul></nav>
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
					<h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;查看记录</h4>
				</div>
				<div class="modal-body">
					<!--div class="form-group"-->
						<div><label id="stay_name"></label></div>
						<div><label id="stay_createtime"></label></div>
						<div><label id="stay_deadline"></label></div>
						<div><label id="cut_off"></label></div>
						<div class="form-group form-inline"><label>允许时间：</label><div class="input-group date col-md-4" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1"><input class="form-control" id="begin" name="stay_start" placeholder="开始于" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div><label>&nbsp;～&nbsp;</label><div class="input-group date col-md-4" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2"><input class="form-control" id="stop" name="stay_stop" placeholder="结束于" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></div>
						<div><label id="stay_details"></label></div>
						<p></p>
						<div id="submitted"></div>
						<div id="unSubmit"></div>
					<!--/div-->
				</div>
				<div id="modal-footer" class="modal-footer">
					<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>&nbsp;关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!--Model-->

	<!-- Modal -->
	<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="addLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="addLabel"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加记录</h4>
				</div>
				<form id="addTask" method="post" action="createStay" onsubmit='javascript:$("#bbb").val($("#bbb").val()+" 0:0:0");javascript:$("#sss").val($("#sss").val()+" 0:0:0");$("#addModal").modal("hide");'>
					<div class="modal-body">
						<div class="form-group"><label>标题：</label><input type="text" class="form-control" name="stay_name" placeholder="标题"></div>
						<div class="form-group"><label>截止时间：</label><div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1"><input id="add-stay-deadline" class="form-control" name="stay_deadline" placeholder="截止时间" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></div>
						<div><label>允许请假时间段：</label></div>
						<div class="form-group form-inline">
							<div class="input-group date form_date col-md-5" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2"><input id="bbb" class="form-control" name="stay_begintime" placeholder="最早" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div>
							<label>&nbsp;～&nbsp;</label><div class="input-group date form_date col-md-5" data-date-format="yyyy-mm-dd" data-link-field="dtp_input3"><input id="sss" class="form-control" name="stay_stoptime" placeholder="最迟" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div>
						</div>
						<div class="form-group"><label>描述：</label><input type="text" class="form-control" name="stay_details" placeholder="描述"></div>

					</div>
					<div id="deletefooter" class="modal-footer">
						<button type="submit" class="btn btn-sm btn-success"><span class="glyphicon glyphicon-ok"></span>&nbsp;保存</button>
						<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>&nbsp;放弃</button>
					</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				</form>
			</div>
		</div>
	</div>
	<!--Model-->

	<!-- Modal -->
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="deleteLabel"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除记录</h4>
				</div>
				<div class="modal-body">
					<p>确定要将这条信息完全删除？此操作不可撤销！</p>
				</div>
				<div id="deletefooter" class="modal-footer">
					<form id="delTask" action="deleteStay" method="post">
						<input id="delTaskById" type="hidden" name="stay_id">
						<button type="button" class="btn btn-sm btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-repeat"></span>&nbsp;返回</button>
						<button type="submit" class="btn btn-sm btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</button>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
					</form>
				</div>
			</div>
		</div>
	</div>
	<!--Model-->

	<!-- Modal -->
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="editLabel"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改记录</h4>
				</div>
				<form id="editTask" method="post" action="updateStay">
					<div class="modal-body">
						<div class="form-group"><label>编号：</label><input id="edit_id" name="stay_id" class="form-control" readonly></div>
						<div class="form-group"><label>标题：</label><input id="edit_title" class="form-control" readonly></div>
						<div class="form-group"><label>截止时间：</label><div class="input-group date form_datetime col-md-5" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1"><input id="edit_deadline" class="form-control" name="stay_deadline" placeholder="截止时间" size="16" type="text" value="" readonly><span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div></div>
						<div class="form-group"><label>描述：</label><input id="edit_details" name="stay_details" class="form-control"></div>
					</div>
					<div id="editfooter" class="modal-footer">
						<button type="submit" class="btn btn-sm btn-success"><span class="glyphicon glyphicon-ok"></span>&nbsp;保存</button>
						<button type="button" class="btn btn-sm btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>&nbsp;放弃</button>
					</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				</form>
			</div>
		</div>
	</div>
	<!--Model-->
	
	<c:url value="/logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /></form>
	<script src="http://cdn.bootcss.com/jquery.form/3.51/jquery.form.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>