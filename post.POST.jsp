<%--
  ADOBE CONFIDENTIAL
  ___________________

   Copyright 2016 Adobe Systems Incorporated
   All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.

  ==============================================================================
Form 'action' component

  Handles the form store

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%@page import="com.aem.core.HandleCustomFormAction,
      		org.apache.sling.api.resource.ResourceUtil,
            org.apache.sling.api.resource.ValueMap"%><%
%><%

    final HandleCustomFormAction handleCustomFormAction = sling.getService(HandleCustomFormAction.class);
    handleCustomFormAction.injestFormData(slingRequest, resource);
	ValueMap props = ResourceUtil.getValueMap(resource);
	String redirectPath = (String)props.get("redirect");


String uri = slingRequest.getRequestURI();

String pageName = uri.substring(uri.lastIndexOf("/")+1);
redirectPath = pageName + redirectPath;

	if(null == redirectPath){
	    //out.println("from-->" + (String)props.get("from"));
		out.println("\nForm Submitted!!");

	}else if(redirectPath.indexOf(".html") > 0 || redirectPath.indexOf(".com") > 0 ){
        response.sendRedirect(redirectPath);
	}else{
        response.sendRedirect(redirectPath + ".html");
        }

%>
