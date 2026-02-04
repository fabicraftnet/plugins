package net.fabicraft.paper.survival.listener;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class PlayerListener implements Listener {
	private final FabiCraftPaperSurvival plugin;

	public PlayerListener(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onFirstJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPlayedBefore()) {
			return;
		}

		Inventory inventory = player.getInventory();
		ItemStack claimShovel = this.plugin.customItemManager().itemStack("tonttilapio");
		if (claimShovel != null) {
			inventory.addItem(claimShovel);
		} else {
			this.plugin.getSLF4JLogger().warn("Couldn't find custom item named tonttilapio. Giving normal golden shovel instead. Please check items.json!");
			inventory.addItem(ItemStack.of(Material.GOLDEN_SHOVEL));
		}
		inventory.addItem(ItemStack.of(Material.BREAD, 16));
	}
}
