package com.Gamerboy59.bukkit.rocketboots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.bukkit.ChatColor;

public class Language {

	private final RocketBoots plugin;
	private final RBConfiguration config;

    public Language(RocketBoots plugin, RBConfiguration config) {
    	this.plugin = plugin;
        this.config = config;
    }

	private static final String BUNDLE_LOCATION = "language.messages"; //$NON-NLS-1$
	private static final Locale defaultLocale = Locale.ENGLISH;
	private static final String missingTranslationKeyMessage = "Missing translation key \"%s\" in translation file %s";
	
	private ResourceBundle loadResourceBundle(Locale lang) {
		ResourceBundle resourceBundle;
		try {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_LOCATION, lang, new FileResClassLoader(getClass().getClassLoader()), new UTF8PropertiesControl());
        } catch (final MissingResourceException e) {
        	try {
                resourceBundle = ResourceBundle.getBundle(BUNDLE_LOCATION, lang, new UTF8PropertiesControl());
            } catch (final MissingResourceException ex) {
            	resourceBundle = ResourceBundle.getBundle(BUNDLE_LOCATION, defaultLocale, new UTF8PropertiesControl());
            }
        }
		return resourceBundle;
	}
	
	private Locale getLocale(String locale) {
		Locale currentLocale = Locale.ENGLISH;
		if (locale != null && !locale.isEmpty()) {
            final String[] parts = locale.split("[_\\.]");
			if (parts.length == 1) {
                currentLocale = new Locale(parts[0]);
            }
            if (parts.length == 2) {
                currentLocale = new Locale(parts[0], parts[1]);
            }
            if (parts.length == 3) {
                currentLocale = new Locale(parts[0], parts[1], parts[2]);
            }
        }
        //ResourceBundle.clearCache();
		return currentLocale;
	}
	
	private String translateAndReplace(String message, String player) {
		String string = message;
		string = string.replace("%prefix%", config.prefix());
		if(player != null && !player.isEmpty()) {
			string = string.replace("%player%", player);
		}
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public String getString(String key, String lang) {
		try {
            return translateAndReplace(loadResourceBundle(getLocale(lang)).getString(key), null);
        } catch (final MissingResourceException e) {
            if (config.debug()) {
                plugin.getLogger().log(Level.WARNING, String.format(missingTranslationKeyMessage, e.getKey(), defaultLocale.toString()), e);
            }
            return "&cNo system message found for: " + key;
        }
	}
	
	public String getString(String key, String lang, String player) {
		try {
            return translateAndReplace(loadResourceBundle(getLocale(lang)).getString(key), player);
        } catch (final MissingResourceException e) {
            if (config.debug()) {
                plugin.getLogger().log(Level.WARNING, String.format(missingTranslationKeyMessage, e.getKey(), defaultLocale.toString()), e);
            }
            return "&cNo system message found for: " + key;
        }
	}

	/**
     * Reads .properties files as UTF-8 instead of ISO-8859-1, which is the default on Java 8/below.
     * Java 9 fixes this by defaulting to UTF-8 for .properties files.
     * See: https://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle
     */
	private class UTF8PropertiesControl extends ResourceBundle.Control {
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {
            final String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                final URL url = loader.getResource(resourceName);
                if (url != null) {
                    final URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // use UTF-8 here, this is the important bit
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
	}
	
	/**
     * Attempts to load properties files from the plugin directory before falling back to the jar.
     */
    private class FileResClassLoader extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(final ClassLoader classLoader) {
            super(classLoader);
            this.dataFolder = plugin.getDataFolder();
        }

        @Override
        public URL getResource(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                } catch (final MalformedURLException ignored) {
                }
            }
            return null;
        }

        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (final FileNotFoundException ignored) {
                }
            }
            return null;
        }
    }
}
