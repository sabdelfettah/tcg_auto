package tcg_auto.hci;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.Test;

import tcg_auto.lang.Messages;
import tcg_auto.manager.Initializator;
import tcg_auto.utils.HCIUtils;

public class ResourcesTest {
	
	@Test
	public void testResourcesBundles() {
		testResourceBundle(Messages.EN_BUNDLE);
		testURL(HCIUtils.PATH_APPLICATION_ICON);
		testURL(HCIUtils.PATH_LOADING_IMAGE);
	}
	
	@Test
	public void testProperties() {
		final Properties properties = new Properties();
		try {
			properties.load(HCI.getInstance().getClass().getResourceAsStream(HCIUtils.PATH_PROPERTIES_FILE));
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		org.junit.Assert.assertNotNull("Properties test", properties);
		org.junit.Assert.assertNotNull("Properties test " + Initializator.KEY_VERSION, properties.getProperty(Initializator.KEY_VERSION));
		org.junit.Assert.assertNotNull("Properties test " + Initializator.KEY_SELENIUM, properties.getProperty(Initializator.KEY_SELENIUM));
		org.junit.Assert.assertNotNull("Properties test " + Initializator.KEY_SELENIUM_SERVER, properties.getProperty(Initializator.KEY_SELENIUM_SERVER));
		org.junit.Assert.assertNotNull("Properties test " + Initializator.KEY_GSON, properties.getProperty(Initializator.KEY_GSON));
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
