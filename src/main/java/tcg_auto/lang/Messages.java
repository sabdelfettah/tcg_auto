package tcg_auto.lang;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	public static final String EN_BUNDLE = "tcg_auto.lang.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(EN_BUNDLE);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
