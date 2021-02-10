<%--
/**
 * Copyright (c) SMC Treviso Srl. All rights reserved.
 */
--%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.model.Layout"%>
<%@page import="com.liferay.portal.kernel.service.LayoutLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@ include file="/display/init.jsp" %>

<c:if test="<%= showPrivacyInfoMessage %>">

	<%
	JournalArticle privacyInfo = PrivacyUtil.getPrivacyJournalArticle(scopeGroupId, privacyInfoMessageArticleId);
	String wrapperCss = "alert alert-info text-center";
	if(staticIncluded)
		wrapperCss += " privacy-info-message";
	%>

	<div class="<%= wrapperCss %>" id="<portlet:namespace />privacy-info-message">

		<c:if test="<%= privacyInfo != null %>">
			<liferay-ui:asset-display
				className="<%= JournalArticle.class.getName() %>"
				classPK="<%= privacyInfo.getResourcePrimKey() %>"
				showHeader="false"
			/>
		</c:if>


		<aui:button-row>

			<c:if test="<%= Validator.isNull(privacyLayout) %>">
				<liferay-portlet:renderURL varImpl="viewPrivacyPolicyURL">
					<portlet:param name="jspPage" value="/display/view_privacy_policy.jsp"/>
				</liferay-portlet:renderURL>
				<%
				viewPrivacyPolicyURL.setWindowState(LiferayWindowState.MAXIMIZED);
	
				String href = viewPrivacyPolicyURL.toString();
	
				viewPrivacyPolicyURL.setWindowState(LiferayWindowState.POP_UP);
	
				String dataHref = viewPrivacyPolicyURL.toString();
	
				Map<String, Object> data = new HashMap<String, Object>();
	
				data.put("title", privacyPolicy.getTitle(locale));
				data.put("href", dataHref);
				%>
	
				<aui:button data="<%= data %>" href="<%= href %>" name="readMore" value="read-more" />
			</c:if>
			<c:if test="<%= Validator.isNotNull(privacyLayout) %>">
				<% 
					String href = "#";
					if(privacyLayout != null) {
						href = PortalUtil.getLayoutFullURL(privacyLayout, themeDisplay);
					}
				%>
				<aui:button href="<%= href %>" name="readMore" value="read-more" />
			</c:if>
			<aui:button name="okButton" primary="true" value="ok" />
		</aui:button-row>
		
		
	</div>

	<aui:script use="aui-base,cookie,liferay-util-window">
		var okButton = A.one('#<portlet:namespace />okButton');
		var readMore = A.one('#<portlet:namespace />readMore');

		okButton.on('click', function (event) {
			hidePrivacyMessage();

			event.halt();
		});

		readMore.on('click', function (event) {
			console.log(this);
			console.log(this.getData('title'));
			if(this.getData('title')) {
				if (!event.metaKey && !event.ctrlKey) {
					Liferay.Util.openInDialog(event);
				}
			}
		});

		var wrapper = A.one('#wrapper');
		var privacyInfoMessage = A.one('#<portlet:namespace />privacy-info-message');

		if (wrapper) {
			wrapper.addClass('wrapper-for-privacy-portlet');
		}

		if (privacyInfoMessage) {
			var hideStripPrivacyInfoMessage = privacyInfoMessage.one('.hide-strip-privacy-info-message');

			if (hideStripPrivacyInfoMessage) {
				hideStripPrivacyInfoMessage.on('click', hidePrivacyMessage);
			}
		}

		function hidePrivacyMessage() {
			privacyInfoMessage.ancestor('.smc-privacy-portlet').hide();

			var today = new Date();
			var expire = new Date();
			var nDays = <%= cookieExpiration %>;

			expire.setTime(today.getTime() + 3600000 * 24 * nDays);

			var expString = "expires=" + expire.toGMTString();

			cookieName = "<%= PrivacyUtil.PRIVACY_READ %><%= nameExtend %>";
			cookieValue = today.getTime();

			document.cookie = cookieName+"="+escape(cookieValue)+ ";expires="+expire.toGMTString();

			wrapper.removeClass('wrapper-for-privacy-portlet');
		}
	</aui:script>

</c:if>