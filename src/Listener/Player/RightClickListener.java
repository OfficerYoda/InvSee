package Listener.Player;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import Enums.Commands;
import Main.Main;
import Manager.PermissionManager;
import de.officeryoda.SignApi;

public class RightClickListener implements Listener {
	String prefix = Main.prefix;
	PermissionManager permissionManager = Main.getInstance().getPermissionManager();

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Player player = event.getPlayer();
		if(!permissionManager.getPermissionHandler(Commands.EDITSIGN).getPermission().hasPermission(player, Commands.EDITSIGN)) return;

		Block block = player.getTargetBlock(null, player.getGameMode() == GameMode.CREATIVE ? 5 : 4);
		Sign sign = null;
		
		try {
			sign = (Sign) block.getState();
			SignApi.openExistingSign(player, sign);
		} catch (Exception e) {}
	}
}
