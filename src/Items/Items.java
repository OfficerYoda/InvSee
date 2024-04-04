package Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Enums.Commands;
import Enums.Permission;
import Main.Main;
import Manager.PermissionManager;

public class Items {

	public static ItemStack spaceholder;

	public static ItemStack perALL;
	public static ItemStack perOP;
	public static ItemStack perNON;
	public static ItemStack perCUSTOM;
	
	public static ItemStack cPerTrue;
	public static ItemStack cPerFalse;
	
	public static ItemStack changeAllALL;
	public static ItemStack changeAllOP;
	public static ItemStack changeAllNON;
	public static ItemStack changeAllCUSTOM;

	public static ItemStack cmdInvsee;
	public static ItemStack cmdEcOwn;
	public static ItemStack cmdEcAll;
	public static ItemStack cmdCraft;
	public static ItemStack cmdEditSign;

	public static ItemStack[] optionsGui;
	public static List<ItemStack> customPerItems;
	public static List<ItemStack> list;
	public static List<ItemStack> allItems = new ArrayList<>();
	
	static PermissionManager permissionManager = Main.getInstance().getPermissionManager();
	
	public static void initItems() {
		createSpaceholder();
		
		createPerAll();
		createPerOp();
		createPerNon();
		createPerCustom();
		
		createCperTrue();
		createCperFalse();
		
		createChangeAll();
		createChangeOp();
		createChangeNon();
		createChangeCustom();
		
		updateGui();
	}

	public static void updateGui() {
		createCmdInvsee();
		createCmdEcOwn();
		createCmdEcAll();
		createCmdCraft();
		createCmdEditSign();
		
		initGui();
	}
	
	private static void initGui() {
		list = new ArrayList<>();
		for(int i = 0; i < 18; i++) {
			list.add(spaceholder);
		}
		for(int i = 0; i < 9; i++) {
			switch (i) {
			case 0:
				list.set(i, cmdEcOwn);
				list.set(i + 9, permissionManager.getPermissionHandler(Commands.EC_OWN).getItem());
				break;
			case 1:
				list.set(i, cmdEcAll);
				list.set(i + 9, permissionManager.getPermissionHandler(Commands.EC_ALL).getItem());
				break;
			case 2:
				list.set(i, cmdInvsee);
				list.set(i + 9, permissionManager.getPermissionHandler(Commands.INVSEE).getItem());
				break;
			case 3:
				list.set(i, cmdCraft);
				list.set(i + 9, permissionManager.getPermissionHandler(Commands.CRAFT).getItem());
				break;
			case 4:
				list.set(i, cmdEditSign);
				list.set(i + 9, permissionManager.getPermissionHandler(Commands.EDITSIGN).getItem());
				break;
			case 7:
				list.set(i, changeAllOP);
				list.set(i + 9, changeAllCUSTOM);
				break;
			case 8:
				list.set(i, changeAllALL);
				list.set(i + 9, changeAllNON);
				break;
			default:
				break;
			}
		}
		optionsGui = list.toArray(new ItemStack[] {});
	}

	private static void createSpaceholder() {

		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(" ");

		item.setItemMeta(meta);
		
		allItems.add(item);
		spaceholder = item;
	}

	private static void createPerAll() {
		ItemStack item = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Permission.ALL.getColor() + "ALL");

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Everyone can use");
		/*LORE*/lore.add("this command");
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		item.setItemMeta(meta);
		
		allItems.add(item);
		perALL = item;
	}

	private static void createPerOp() {
		ItemStack item = new ItemStack(Material.RED_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Permission.OP.getColor() + "OPERATORS");

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Only Operators can");
		/*LORE*/lore.add("use this command");
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		item.setItemMeta(meta);
		
		allItems.add(item);
		perOP = item;
	}

	private static void createPerNon() {
		ItemStack item = new ItemStack(Material.GRAY_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Permission.NON.getColor() + "NOBODY");

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Nobody can use");
		/*LORE*/lore.add("this command");
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		item.setItemMeta(meta);
		
		allItems.add(item);
		perNON = item;
	}

	private static void createPerCustom() {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Permission.CUSTOM.getColor() + "CUSTOM");

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("right click to set");
		/*LORE*/lore.add("permissions per player");
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		item.setItemMeta(meta);
		
		allItems.add(item);
		perCUSTOM = item;
	}

	private static void createChangeAll() {
		ItemStack item = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Permission.ALL.getColor() + "ALL");

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Click to change all");
		/*LORE*/lore.add("permissions to " + perALL.getItemMeta().getDisplayName());
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);

		item.setItemMeta(meta);
		
		allItems.add(item);
		changeAllALL = item;
	}

	private static void createChangeOp() {
		ItemStack item = new ItemStack(Material.RED_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(perOP.getItemMeta().getDisplayName());

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Click to change all");
		/*LORE*/lore.add("permissions to " + perOP.getItemMeta().getDisplayName());
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);

		item.setItemMeta(meta);
		
		allItems.add(item);
		changeAllOP = item;
	}

	private static void createChangeNon() {
		ItemStack item = new ItemStack(Material.GRAY_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(perNON.getItemMeta().getDisplayName());

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Click to change all");
		/*LORE*/lore.add("permissions to " + perNON.getItemMeta().getDisplayName());
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);

		item.setItemMeta(meta);
		
		allItems.add(item);
		changeAllNON = item;
	}

	private static void createChangeCustom() {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(perCUSTOM.getItemMeta().getDisplayName());

		/*LORE*/ArrayList<String> lore = new ArrayList<>();
		/*LORE*/lore.add("Click to change all");
		/*LORE*/lore.add("permissions to " + perCUSTOM.getItemMeta().getDisplayName());
		/*LORE*/meta.setLore(lore);

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);

		item.setItemMeta(meta);
		
		allItems.add(item);
		changeAllCUSTOM = item;
	}

	private static void createCmdInvsee() {

		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(permissionManager.getPermissionHandler(Commands.INVSEE).getColor() + "/invsee");

		item.setItemMeta(meta);
		
		allItems.add(item);
		cmdInvsee = item;
	}

	private static void createCmdEcOwn() {
		ItemStack item = new ItemStack(Material.ENDER_CHEST, 1);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(permissionManager.getPermissionHandler(Commands.EC_OWN).getColor() + "/ec" + ChatColor.ITALIC + " (Own enderchest)");

		item.setItemMeta(meta);
		
		allItems.add(item);
		cmdEcOwn = item;
	}

	private static void createCmdEcAll() {
		ItemStack item = new ItemStack(Material.ENDER_CHEST, 64);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(permissionManager.getPermissionHandler(Commands.EC_ALL).getColor() + "/ec" + ChatColor.ITALIC + " (All enderchests)");

		item.setItemMeta(meta);

		cmdEcAll = item;
	}

	private static void createCmdCraft() {
		ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(permissionManager.getPermissionHandler(Commands.CRAFT).getColor() + "/craft");

		item.setItemMeta(meta);
		
		allItems.add(item);
		cmdCraft = item;
	}

	private static void createCmdEditSign() {
		ItemStack item = new ItemStack(Material.OAK_SIGN);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(permissionManager.getPermissionHandler(Commands.EDITSIGN).getColor() + "/editsign" + ChatColor.GRAY + " (or right click)");

		item.setItemMeta(meta);
		
		allItems.add(item);
		cmdEditSign = item;
	}
	
	private static void createCperTrue() {
		ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GREEN + "has Permission");

		item.setItemMeta(meta);

		cPerTrue = item;
	}
	
	private static void createCperFalse() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.RED + "no Permission");

		item.setItemMeta(meta);
		
		allItems.add(item);
		cPerFalse = item;
	}
	
	public static void setCustomPerItems(List<ItemStack> customPerItems) {
		Items.customPerItems = new ArrayList<>();
		Items.customPerItems.addAll(customPerItems);
	}
	
	public static List<ItemStack> getCustomPerItems() {
		return customPerItems == null ? new ArrayList<>() : customPerItems;
	}
}