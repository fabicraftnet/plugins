package net.fabicraft.paper.core.listener;

import io.papermc.paper.event.player.PlayerServerFullCheckEvent;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.joinMessage(null);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.quitMessage(null);
	}
}
