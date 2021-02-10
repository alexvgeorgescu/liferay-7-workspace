<%--
/**
 * Copyright (c) SMC Treviso Srl. All rights reserved.
 */
--%>
<%@page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Layout"%>
<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.journal.model.JournalArticle" %>
<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%
JournalArticle privacyPolicy = PrivacyUtil.getPrivacyJournalArticle(scopeGroupId, privacyPolicyArticleId);

Layout privacyLayout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(scopeGroupId, false, privacyPolicyFriendlyURL);

String[] staticPortlets = PropsUtil.getArray(PropsKeys.LAYOUT_STATIC_PORTLETS_ALL);

boolean staticIncluded  = ArrayUtil.contains(staticPortlets, PrivacyPortletKeys.PRIVACY);

boolean showPrivacyInfoMessage = PrivacyUtil.showPrivacyInfoMessage(themeDisplay.isSignedIn(), privacyEnabled, privacyPolicy, privacyLayout, request, nameExtend);
%>


