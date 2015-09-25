<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="UTF-8">
	<title>Smarttrip System后台管理系统</title>
	<link rel="stylesheet" type="text/css" href="${contextPath}/static/framework/jeasyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${contextPath}/static/framework/jeasyui/themes/icon.css">
	<script type="text/javascript" src="${contextPath}/static/framework/jeasyui/jquery.min.js"></script>
	<script type="text/javascript" src="${contextPath}/static/framework/jeasyui/jquery.easyui.min.js"></script>
</head>
<body>
	<div class="easyui-layout" style="width: 100%; height: 600px;">
		<!-- west region 开始 -->
		<div region="west" split="true" title="菜单导航" class="easyui-accordion" style="width: 250px;">
				<div title="内容管理" style="overflow:auto;padding:10px;">
					<h3 style="color:#0099FF;">Accordion for jQuery</h3>
					<p>Accordion is a part of easyui framework for jQuery. It lets you define your accordion component on web page more easily.</p>
				</div>
				<div title="程序管理" selected="true" style="padding:10px;">
					easyui help you build your web page easily
				</div>
				<div title="用户管理">
					<ul id="tt1" class="easyui-tree">
						<li>
							<span>Folder1</span>
							<ul>
								<li>
									<span>Sub Folder 1</span>
									<ul>
										<li><span>File 11</span></li>
										<li><span>File 12</span></li>
										<li><span>File 13</span></li>
									</ul>
								</li>
								<li><span>File 2</span></li>
								<li><span>File 3</span></li>
							</ul>
						</li>
						<li><span>File2</span></li>
					</ul>
				</div>
		</div>
		<!-- west region 结束 -->
		
		<!-- center region 开始 -->
		<div id="content" region="center" title="内容" style="padding: 5px;">
			<div class="easyui-tabs" style="width:400px;height:100px;">
				<div title="First Tab" style="padding:10px;">
					First Tab
				</div>
				<div title="Second Tab" closable="true" style="padding:10px;">
					Second Tab
				</div>
				<div title="Third Tab" iconCls="icon-reload" closable="true" style="padding:10px;">
					Third Tab
				</div>
			</div>
		</div>
		<!-- center region 结束 -->
	</div>
</body>
</html>