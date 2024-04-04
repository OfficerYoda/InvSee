package Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class config {

	private static File file = null;
	private static YamlConfiguration config = new YamlConfiguration();

	private final String FOLDERNAME = "InvSee";

	public config() {

		File dir = new File("./plugins/" + FOLDERNAME + "/");

		if (!dir.exists()) {
			dir.mkdirs();
		}

		file = new File(dir, "config.yml");
		
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		config = YamlConfiguration.loadConfiguration(file);
	}

	public File getFile() {
		return file;
	}	

	public static YamlConfiguration getConfig() {
		return config;
	}	

	public static boolean contains(String path) {
		return config.contains(path);
	}

	public static void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reset() throws IOException {
		for(String key : getConfig().getKeys(false))
			getConfig().set(key,null);
		config.save(file);
	}

	public static void put(String path, Object value, Object errorValue) {
		if(value != null)
			config.set(path, value);
		else if(errorValue != null)
			config.set(path, errorValue);
		else
			return;
		save();
	}

	public static Object get(String path, Object errorValue) {
		return contains(path) ? config.get(path) : errorValue;
	}
}