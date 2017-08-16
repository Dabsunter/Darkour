package fr.dabsunter.darkour.util;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Pattern;

public class Trein {
	private static final Properties DEFAULT_TREINS = new Properties();
	private static final Properties TREINS = new Properties(DEFAULT_TREINS);
	private static final Pattern LINE_PATTERN = Pattern.compile("\\{NEW_LINE}");

	private Trein() {}

	public static void load(Plugin plugin) throws IOException {
		if (DEFAULT_TREINS.isEmpty())
			DEFAULT_TREINS.load(new InputStreamReader(plugin.getResource("en.lang"), StandardCharsets.UTF_8));

		File langFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("lang") + ".lang");
		TREINS.load(new InputStreamReader(new FileInputStream(langFile), StandardCharsets.UTF_8));
	}

	public static String format(Enum key, Object... placeholders) {
		return format(key.name().toLowerCase().replace('_', '.'), placeholders);
	}

	public static String format(String key, Object... placeholders) {
		if ((placeholders.length & 1) == 1)
			throw new IllegalArgumentException("A value is missing in placeholder list");
		String trein = TREINS.getProperty(key);
		if (trein != null) {
			for (int i = 0; i < placeholders.length;)
				trein = trein.replace('{' + (String) placeholders[i++] + '}',
						String.valueOf(placeholders[i++]));
		} else {
			StringBuilder sb = new StringBuilder(key);
			if (placeholders.length > 0) {
				sb.append(':').append('{');
				for (int i = 0;;) {
					sb.append((String) placeholders[i++]).append('=').append(placeholders[i++]);
					if (i < placeholders.length) {
						sb.append(',').append(' ');
						continue;
					}
					break;
				}
				sb.append('}');
			}
			trein = sb.toString();
		}
		return trein;
	}

	public static String[] multiline(String trein) {
		return LINE_PATTERN.split(trein);
	}
}
