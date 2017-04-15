package tcg_auto.hci;

import java.net.URL;
import java.util.ResourceBundle;

import org.junit.Test;

import tcg_auto.lang.Messages;
import tcg_auto.utils.HCIUtils;

public class ResourcesTest {
	
	@Test
	public void test() {
		testResourceBundle(Messages.EN_BUNDLE);
		testURL(HCIUtils.PATH_APPLICATION_ICON);
		testURL(HCIUtils.PATH_LOADING_IMAGE);
	}
	
	private void testResourceBundle(String ressourceName){
		ResourceBundle ressourceBundle = ResourceBundle.getBundle(ressourceName);
		org.junit.Assert.assertNotNull("testResourceBundle of " + ressourceName, ressourceBundle);
	}
	
	private void testURL(String urlPath){
		URL url = HCIUtils.getUrlFromPath(urlPath);
		org.junit.Assert.assertNotNull("testURL of " + urlPath, url);
	}

}
