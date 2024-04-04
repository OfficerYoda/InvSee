package Commands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import Main.Main;
import de.officeryoda.SignApi;

public class cmdEditSign implements CommandExecutor, Listener {
	String prefix = Main.prefix;
	//	static Map<Location, Material> signs;

	public cmdEditSign() {
		//		signs = new HashMap<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only Players can use this command!");

			return false;
		}

		Player player = (Player) sender;
		Block block = player.getTargetBlock(null, 10);
		Sign sign = null;
		
		if(args.length == 0) {
			try {
				sign = (Sign) block.getState();
				player.sendMessage(prefix + "Opening sign.");
				SignApi.openExistingSign(player, sign);
				return true;
			} catch (Exception e) {
				player.sendMessage(prefix + ChatColor.RED + "You aren't looking at a sign.");
				return false;
			}
		}
		
		return false;
	}
}
