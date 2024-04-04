package Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import Enums.Commands;
import Enums.Permission;
import Enums.ViewedInvType;
import Listener.GUI.InventoryListener;
import Main.Main;
import Manager.InventoryManager;
import Manager.KnownPlayerManager;
import Manager.OptionManager;
import Manager.PermissionManager;
import Utils.config;

public class cmdInvSee implements TabExecutor {
	String prefix = Main.prefix;

	List<Player> reset = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only Players can use this command!");
			return false;
		}
		Player player = (Player) sender;
		String target = "";
		Permission permission;

		PermissionManager permissionManager = Main.getInstance().getPermissionManager();
		
		if(args.length != 0 && player.isOp()) {
			switch (args[0].toLowerCase()) {
			case "options":
				OptionManager.openOptions(player);
				player.sendMessage(prefix + "Opening options.");
				return true;

			case "reset": 
				player.sendMessage(prefix + ChatColor.RED + "Use " + ChatColor.ITALIC + "/invsee confirm" + ChatColor.RED + " to confirm" + ChatColor.GRAY + " (Time: 5 seconds).");
				reset.add(player);
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
					@Override
					public void run() {
						reset.remove(player);
					}
				}, 5 * 20);
				return true;

			case "confirm": 
				if(!reset.contains(player))
					break;
				reset.remove(player);
				player.sendMessage(prefix + "Resetting conifg.");
				try {
					config.reset();
					permissionManager.reset();
					KnownPlayerManager.reset();
				} catch (IOException e) {}
				player.sendMessage(prefix + "Config reset.");
				return true;
			default:
				break;
			}
		}
		
		try {
			permission = permissionManager.getPermissionHandler(Commands.valueOf(command.getName().toUpperCase())).getPermission();
		} catch (Exception e) {
			player.sendMessage(prefix + ChatColor.RED + "Ups..., Something went wrong.");
			return false;
		}
		
		if(!permission.hasPermission(player, Commands.INVSEE)) {
			player.sendMessage(prefix + ChatColor.RED + "You don't have the permission to use this command!");
			return false;
		}

		if(args.length == 0) {
			player.sendMessage(prefix + "Please add a playername");
			return false;
		}

		target = args[0];
		if(target == null) {
			player.sendMessage(prefix + "Unknown Player: " + args[0]);
			return false;
		}
		
		if(target.equals(player.getName())) {
			player.sendMessage(prefix + "You can't open your own inventory this way.");
			return false;
		}

		if(!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayerExact(target))) {
			if(!config.contains("inventories." + args[0])) {
				player.sendMessage(prefix + ChatColor.GREEN + args[0] + ChatColor.GRAY + "'s inventory is not known to the server.");
				return false;
			}
			try {
				player.openInventory(InventoryManager.guiFromConfig("inventories." + args[0], player));
				InventoryListener.setViewedInvPlayerName(player, args[0], ViewedInvType.INVENTORY);
			} catch (IllegalArgumentException | IOException e) {}

			return false;
		}

		player.sendMessage(prefix + "Opening the inventory of " + ChatColor.GREEN + target + ChatColor.GRAY + ".");
		player.openInventory(InventoryManager.guiFromPlayerInventory(Bukkit.getPlayerExact(target), player));
		InventoryListener.setViewedInvPlayerName(player, target, ViewedInvType.INVENTORY);

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only Players can use this command!");
			return new ArrayList<>();
		}
		
		List<String> arguments = new ArrayList<>();
		if(args.length == 1) {

			if(Main.getInstance().getPermissionManager().getPermissionHandler(Commands.valueOf(command.getName().toUpperCase())).getPermission().hasPermission((Player) sender, Commands.INVSEE)) {
				for(Player player : Bukkit.getOnlinePlayers())
					arguments.add(player.getName());
				if(config.contains("inventories"))
					arguments.addAll(config.getConfig().getConfigurationSection("inventories").getKeys(false));
				arguments.remove(sender.getName());
			} else {
				arguments.add("");
			}
			if(sender.isOp()) {
				arguments.add("options");
				arguments.add("reset");
			}
		} else {
			arguments.add("");
		}
		return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
	}
}
