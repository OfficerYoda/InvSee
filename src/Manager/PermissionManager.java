package Manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import Enums.Commands;
import Enums.Permission;

public class PermissionManager {

	Map<Commands, PermissionHandler> handler;
	
	public PermissionManager() {
		handler = new HashMap<>();
		for(Commands command : Commands.values())
			handler.put(command, new PermissionHandler(command, Permission.OP));
	}
	
	public PermissionHandler getPermissionHandler(Commands command) {
		if(handler.get(command) == null) {
			Bukkit.broadcastMessage("new PermissionHandler");
			handler.put(command, new PermissionHandler(command, Permission.OP));
			return getPermissionHandler(command);
		} else {
			return handler.get(command);
		}
	}
	
	public void switchCustomPermission(Commands cmdName, Player player) {
		getPermissionHandler(cmdName).switchCustomPermission(player);
	}
	
	public void setAllPermissions(Permission permission) {
		for (Commands command : Commands.values())
			getPermissionHandler(command).setPermission(permission);
	}

	public void nextPermisions(Commands cmdName) {
		PermissionHandler pHandler = getPermissionHandler(cmdName);
		Permission activePer = pHandler.getPermission();
		if(activePer == Permission.OP)
			pHandler.setPermission(Permission.ALL);
		else if(activePer == Permission.ALL)
			pHandler.setPermission(Permission.NON);
		else if(activePer == Permission.NON)
			pHandler.setPermission(Permission.CUSTOM);
		else if(activePer == Permission.CUSTOM)
			pHandler.setPermission(Permission.OP);
	}
	
	public void save() {
		for(Commands commands : Commands.values())
			handler.get(commands).save();
	}
	
	public void reset() {
		for(Commands commands : Commands.values())
			handler.get(commands).reset();
	}
}
