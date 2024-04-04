package Manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Utils.config;

public class KnownPlayerManager {
	static List<String> knownPlayers;

	public KnownPlayerManager() {
		knownPlayers = new ArrayList<>();

		load();
	}

	public static void addPlayer(String playername) {
		if(!knownPlayers.contains(playername))
			knownPlayers.add(playername);
	}

	public static void removePlayer(String playername) {
		if(knownPlayers.contains(playername))
			knownPlayers.remove(playername);
	}

	public static List<String> getKnownPlayers() {
		return knownPlayers;
	}
	
	public static boolean knowsPlayer(Player player) {
		return knownPlayers.contains(player.getName());
	}
	
	public static boolean knowsPlayer(String playername) {
		return knownPlayers.contains(playername);
	}
	
	public static Player getPlayer(String playername) {
		if(knowsPlayer(playername))
			return Bukkit.getPlayerExact(playername);
		return null;
	}
	
	public static void reset() {
		knownPlayers = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	private static void load() {
		knownPlayers = (List<String>) config.get("knownplayer", new ArrayList<>());
		Bukkit.getOnlinePlayers().forEach(player -> addPlayer(player.getName()));
	}
	
	public static void save() throws IOException {
//		List<String> playernames = new ArrayList<>();
//		knownPlayers.forEach(playername -> playernames.add(playername));

		config.put("knownplayer", knownPlayers, "");
	}
}
