package Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Enums.Commands;
import Enums.Permission;
import Main.Main;

public class cmdCraft implements CommandExecutor {
	String prefix = Main.prefix;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only Players can use this command!");

			return false;
		}
		
		Player player = (Player) sender;
		Permission permission;
		try {
			permission = Main.getInstance().getPermissionManager().getPermissionHandler(Commands.valueOf(command.getName().toUpperCase())).getPermission();
		} catch (Exception e) {
			player.sendMessage(prefix + ChatColor.RED + "Ups..., Something went wrong.");
			return false;
		}
		
		if(!permission.hasPermission(player, Commands.CRAFT)) {
			player.sendMessage(prefix + ChatColor.RED + "You don't have the permission to use this command!");
			return false;
		}
		
		player.openWorkbench(player.getLocation(), true);
		
		return false;
	}
}
