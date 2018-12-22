package net.theuniverscraft.PvpAchievementsListener;

import java.util.HashMap;

import net.theuniverscraft.PvpAchievements.AchievementType;
import net.theuniverscraft.PvpAchievements.Managers.DbManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class AchievementsListener implements Listener {
	private HashMap<String, Location> m_lastPos = new HashMap<String, Location>();
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
				
		if(!m_lastPos.containsKey(player.getName())) {
			m_lastPos.put(player.getName(), player.getLocation());
		}
		else {
			Location last = m_lastPos.get(player.getName());
			
			if(!last.getWorld().getName().equals(player.getWorld().getName())) {
				m_lastPos.put(player.getName(), player.getLocation());
			}
			else {
				if(last.distance(player.getLocation()) >= 1) {
					DbManager.getInstance().addAction(player, AchievementType.SPEEDER);
					m_lastPos.put(player.getName(), player.getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player dead = event.getEntity();
		Player killer = dead.getKiller();
		
		if(killer != null) {
			DbManager.getInstance().addAction(killer, AchievementType.KILLER);
			DbManager.getInstance().addAction(killer, AchievementType.KILL_ENNEMI);
		}
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		DbManager.getInstance().addAction(event.getPlayer(), AchievementType.MINEUR);
		if(event.getBlock().getType() == Material.IRON_ORE) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.BREAK_IRON);
		}
		else if(event.getBlock().getType() == Material.LOG) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.BREAK_LOG);
		}
		else if(event.getBlock().getType() == Material.OBSIDIAN) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.BREAK_OBSI);
		}
		else if(event.getBlock().getType() == Material.QUARTZ_ORE) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.BREAK_QUARTZ);
		}
		else if(event.getBlock().getType() == Material.DIAMOND_ORE) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.BREAK_DIAMOND);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.OBSIDIAN) {
			DbManager.getInstance().addAction(event.getPlayer(), AchievementType.PLACE_OBSI);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getItem() == null) return;
			if(event.getItem().getType() == Material.FURNACE || event.getItem().getType() == Material.BURNING_FURNACE) {
				DbManager.getInstance().addAction(event.getPlayer(), AchievementType.USE_FURNACE);
			}
			else if(event.getItem().getType() == Material.ANVIL) {
				DbManager.getInstance().addAction(event.getPlayer(), AchievementType.USE_ANVIL);
			}
		}
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		if(event.getCurrentItem().getType() == Material.GOLDEN_APPLE && event.getCurrentItem().getDurability() == 1) {
			Player player = (Player) event.getWhoClicked();
			DbManager.getInstance().addAction(player, AchievementType.CRAFT_APPLE_CHEAT);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		if(event.getInventory().getType() == InventoryType.BREWING) {
			if(event.getSlot() == 0 || event.getSlot() == 1 || event.getSlot() == 2) {
				if(event.getCurrentItem().getType() == Material.POTION) {
					if(event.getCurrentItem().getDurability() != 0) {
						DbManager.getInstance().addAction(player, AchievementType.CRAFT_POTION);
					}
				}
			}
		}
		else if(event.getInventory().getType() == InventoryType.ANVIL) {
			if(event.getSlot() == 2) {
				if(event.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
					if(event.getCurrentItem().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 4) {
						DbManager.getInstance().addAction(player, AchievementType.ENCHANTE_P4);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		if(event.getItemStack().getType() == Material.WATER_BUCKET) {
			DbManager.getInstance().addAction(player, AchievementType.WATER_BUCKET);
		}
		else if(event.getItemStack().getType() == Material.LAVA_BUCKET) {
			DbManager.getInstance().addAction(player, AchievementType.LAVA_BUCKET);
		}
	}
	
	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World to = player.getWorld();
		if(to.getEnvironment() == Environment.THE_END) {
			DbManager.getInstance().addAction(player, AchievementType.GO_THE_END);
		}
		if(to.getName().equalsIgnoreCase("build") || to.getName().equalsIgnoreCase("ressource")) {
			DbManager.getInstance().addAction(player, AchievementType.GO_OTHER_WORLD);
		}
	}
	
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		ItemStack is = event.getItem();
		if(is.getType() == Material.DIAMOND_CHESTPLATE) {
			if(event.getEnchantsToAdd().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
				if(event.getEnchantsToAdd().get(Enchantment.PROTECTION_ENVIRONMENTAL) == 4) {
					DbManager.getInstance().addAction(player, AchievementType.ENCHANTE_P4);
				}
			}
		}
	}
}
