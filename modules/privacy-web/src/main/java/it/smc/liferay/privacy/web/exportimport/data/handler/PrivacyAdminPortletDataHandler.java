package it.smc.liferay.privacy.web.exportimport.data.handler;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.liferay.exportimport.kernel.lar.BasePortletDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import it.smc.liferay.privacy.web.util.PrivacyPortletKeys;
import it.smc.liferay.privacy.web.util.PrivacyUtil;

@Component(immediate = true, property = { "javax.portlet.name=" + PrivacyPortletKeys.PRIVACY,
		"javax.portlet.name=" + PrivacyPortletKeys.PRIVACY_ADMIN }, service = PortletDataHandler.class)
public class PrivacyAdminPortletDataHandler extends BasePortletDataHandler {

	public static final String NAMESPACE = "privacy_web";

	public static final String SCHEMA_VERSION = "1.0.0";

	@Activate
	protected void activate() {
		setPublishToLiveByDefault(true);
		setDataAlwaysStaged(false);
//	    setDataPortletPreferences("rootFolderId");
//	    setDeletionSystemEventStagedModelTypes(
//	        new StagedModelType(BookmarksEntry.class),
//	        new StagedModelType(BookmarksFolder.class));
	    setExportControls(
	        new PortletDataHandlerBoolean(NAMESPACE, "privacy-web"));
//	    setImportControls(getExportControls());
	}

	@Override
	protected String doExportData(PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences) throws Exception {
		
		portletPreferences = PrivacyUtil.getPrivacyAdminSettings(portletDataContext.getCompanyId(), 
				portletDataContext.getGroupId());
		String privacyPolicyArticleId = portletPreferences.getValue("privacyPolicyArticleId", StringPool.BLANK);
		String privacyInfoMessageArticleId = portletPreferences.getValue("privacyInfoMessageArticleId", StringPool.BLANK);
		String privacyPolicyFriendlyURL = portletPreferences.getValue("privacyPolicyFriendlyURL", StringPool.BLANK);
		String privacyEnabled = portletPreferences.getValue("privacyEnabled", "false");
		String cookieExpiration = portletPreferences.getValue("cookieExpiration", "30");

		Element rootElement = addExportDataRootElement(portletDataContext);
		rootElement.addAttribute(
				"group-id", String.valueOf(portletDataContext.getScopeGroupId()));
		
		rootElement.addAttribute("privacy-policy-article-id", privacyPolicyArticleId);
		rootElement.addAttribute("privacy-info-message-article-id", privacyInfoMessageArticleId);	
		rootElement.addAttribute("privacy-policy-friendly-url", privacyPolicyFriendlyURL);
		rootElement.addAttribute("privacy-enabled", privacyEnabled);
		rootElement.addAttribute("cookie-expiration", cookieExpiration);
		
		return getExportDataRootElementString(rootElement);
	}
	
	@Override
	protected PortletPreferences doImportData(PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data) throws Exception {
		Document document = SAXReaderUtil.read(data, false);
		Element element = document.getRootElement();
		Attribute privacyPolicyArticleId = element.attribute("privacy-policy-article-id");
		Attribute privacyInfoMessageArticleId = element.attribute("privacy-info-message-article-id");
		Attribute privacyPolicyFriendlyURL = element.attribute("privacy-policy-friendly-url");
		Attribute privacyEnabled = element.attribute("privacy-enabled");
		Attribute cookieExpiration = element.attribute("cookie-expiration");
		
		portletPreferences = PrivacyUtil.getPrivacyAdminSettings(portletDataContext.getCompanyId(), 
				portletDataContext.getGroupId());
		
		if(privacyEnabled != null)
			portletPreferences.setValue("privacyEnabled", privacyEnabled.getValue());
		
		if(privacyPolicyArticleId != null)
			portletPreferences.setValue("privacyPolicyArticleId", privacyPolicyArticleId.getValue());
		
		if(privacyInfoMessageArticleId != null)
			portletPreferences.setValue("privacyInfoMessageArticleId", privacyInfoMessageArticleId.getValue());
		
		if(privacyPolicyFriendlyURL != null)
			portletPreferences.setValue("privacyPolicyFriendlyURL", privacyPolicyFriendlyURL.getValue());
		
		if(cookieExpiration != null)
			portletPreferences.setValue("cookieExpiration", cookieExpiration.getValue());
		
		portletPreferences.store();
		
		return null;
	}
}
