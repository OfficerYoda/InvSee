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
import Manager.EnderchestManager;
import Manager.PermissionManager;
import Utils.config;

public class cmdEc implements TabExecutor {
	String prefix = Main.prefix;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only Players can use this command!");
			return false;
		}

		Player player = (Player) sender;
		Player target = null;
		Permission permission;
		
		PermissionManager permissionManager = Main.getInstance().getPermissionManager();
		try {
//			permission = PermissionManager.getPermission(Commands.valueOf(command.getName().toUpperCase() + "_ALL"));
			permission = permissionManager.getPermissionHandler(Commands.valueOf(command.getName().toUpperCase() + "_ALL")).getPermission();
		} catch (Exception e) {
			player.sendMessage(prefix + ChatColor.RED + "Ups..., Something went wrong.");
			return false;
		}

		if(args.length == 0) {
			if(permissionManager.getPermissionHandler(Commands.EC_OWN).getPermission().hasPermission(player, Commands.EC_OWN) || permission.hasPermission(player, Commands.EC_ALL)) {
				player.openInventory(player.getEnderChest());
				return false;
			}
			player.sendMessage(prefix + ChatColor.RED + "You don't have the permission to use this command!");
			return true;
		}

		if(!permission.hasPermission(player, Commands.EC_ALL)) {
			player.sendMessage(prefix + ChatColor.RED + "You don't have the permission to use this command!");
			return false;
		}
		target = Bukkit.getPlayerExact(args[0]);
		if(!Bukkit.getOnlinePlayers().contains(target)) {
			if(!config.contains("enderchests." + args[0])) {
				player.sendMessage(prefix + ChatColor.GREEN + args[0] + ChatColor.GRAY + "'s enderchest is not known to the server.");
				return false;
			}
			try {
				player.openInventory(EnderchestManager.ecFromConfig("enderchests." + args[0], player));
				InventoryListener.setViewedInvPlayerName(player, args[0], ViewedInvType.ENDERCHEST);
				return true;
			} catch (IllegalArgumentException | IOException e) {}



			return true;
		}
		player.openInventory(target.getEnderChest());
		EnderchestManager.enderchests.add(target.getEnderChest());
		player.sendMessage(prefix + "Opening the enderchest of " + ChatColor.GREEN + target.getName() + ChatColor.GRAY + ".");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> arguments = new ArrayList<>();
		if(args.length == 1) {

			if(Main.getInstance().getPermissionManager().getPermissionHandler(Commands.EC_ALL).getPermission().hasPermission((Player) sender, Commands.EC_ALL)) {
				for(Player player : Bukkit.getOnlinePlayers())
					arguments.add(player.getName());
				if(config.contains("enderchests"))
					arguments.addAll(config.getConfig().getConfigurationSection("enderchests").getKeys(false));
			} else {
				arguments.add("");
			}

		} else {
			arguments.add("");
		}
		return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
	}
}
