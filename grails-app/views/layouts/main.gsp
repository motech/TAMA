<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>TAMA - <g:layoutTitle default="" /></title>
<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
<link rel="stylesheet" href="${resource(dir:'css/custom-theme',file:'jquery-ui-1.8.11.custom.css')}" />
<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
<g:javascript library="application" />
<g:javascript src="jquery/jquery-1.5.1.min.js" />
<g:javascript src="jquery/jquery-ui-1.8.11.custom.min.js" />
<g:javascript src="jquery/jquery.validate.min.js" />
<script type="text/javascript">
	$(function(){
		
	});
</script>
<g:layoutHead />
</head>
<body>
	<div id="spinner" class="spinner" style="display: none;">
		<img src="${resource(dir:'images',file:'spinner.gif')}"	alt="${message(code:'spinner.alt',default:'Loading...')}" />
	</div>
	<div id="tama-logo"> </div>
	<g:render template="/layouts/mainTabs" />
	
	<div id="content">
		<g:layoutBody />
	</div>
	
</body>
</html>