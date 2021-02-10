package it.smc.liferay.privacy.web.portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.BasePortletProvider;
import com.liferay.portal.kernel.portlet.ViewPortletProvider;

import it.smc.liferay.privacy.web.util.PrivacyPortletKeys;

/**
 * 
 * @author Alexandru Georgescu
 *
 */
@Component(
		immediate = true,
		property = "model.class.name=" + PrivacyPortletKeys.PRIVACY,
		service = ViewPortletProvider.class
	)
public class PrivacyPortletMenuViewPortletProvicer extends BasePortletProvider implements ViewPortletProvider {

	@Override
	public String getPortletName() {
		return PrivacyPortletKeys.PRIVACY;
	}

}
