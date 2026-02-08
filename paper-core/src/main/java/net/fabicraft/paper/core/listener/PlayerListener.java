package net.fabicraft.paper.core.listener;

import io.papermc.paper.event.player.PlayerServerFullCheckEvent;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerListener implements Listener {
	private final FabiCraftPaperCore plugin;

	public PlayerListener(FabiCraftPaperCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoinFullServer(PlayerServerFullCheckEvent event) {
		boolean hasPermission = this.plugin.luckPermsManager().hasPermission(event.getPlayerProfile().getId(), "fabicraft.paper.core.join.bypass").join();
		if (!hasPermission) {
			return;
		}

		event.allow(true);
	}
}
