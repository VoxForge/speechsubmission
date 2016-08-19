package speechrecorder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;


/*
 * see www.stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties--with-resourcebundle
 */
public class UTF8Control extends Control {

	public ResourceBundle newBundle(
			String baseName,
			Locale locale,
			String format,
			ClassLoader loader,
			boolean reload)
		throws IllegalAccessException,
		InstantiationException,
		IOException 
	{
		// The below is a copy of the default implementation
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");
		ResourceBundle bundle = null;
		InputStream stream = null;
		if (reload) 
		{
			URL url = loader.getResource(resourceName);
			if (url != null) 
			{
				URLConnection connection = url.openConnection();
				if (connection != null) 
				{
					// Disable caches to get fresh data for reloading.
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} 
		else 
		{
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) 
		{
			try 
			{
				// only this line chagned to make it read property files as UTF-8
				bundle = new PropertyResourceBundle( 
						new InputStreamReader (stream, "UTF-8")
				);
			} 
			finally
			{
				stream.close();
			}
		}
		return bundle;
	}

}
