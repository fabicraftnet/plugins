package net.fabicraft.paper.survival.afk;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class AfkManager {
	private static final TranslatableComponent COMPONENT_KICK = Components.translatable(
			"fabicraft.paper.survival.afk.kick",
			MessageType.INFO
	);
	private final Map<UUID, Long> afkSinceMap = new ConcurrentHashMap<>();
	private final FabiCraftPaperSurvival plugin;

	public AfkManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	public void start() {
		plugin.executor().scheduleAtFixedRate(() -> {
			long timeoutNanos = TimeUnit.SECONDS.toNanos(plugin.config().afkTimeoutSeconds());
			Long nanoTime = System.nanoTime();
			this.afkSinceMap.forEach((key, value) -> {
				if (nanoTime - value < timeoutNanos) {
					return;
				}
				Player player = plugin.getServer().getPlayer(key);
				if (player == null || player.hasPermission("fabicraft.paper.survival.afk.kick.bypass")) {
					return;
				}
				Bukkit.getScheduler().runTask(this.plugin, () -> player.kick(COMPONENT_KICK, PlayerKickEvent.Cause.IDLING));
			});
		}, 0, 5, TimeUnit.SECONDS);
	}

	public void update(UUID uuid) {
		this.afkSinceMap.put(uuid, System.nanoTime());
	}

	public boolean afk(UUID uuid) {
		Long afkSince = this.afkSinceMap.get(uuid);
		if (afkSince == null) {
			return false;
		}

		return System.nanoTime() - afkSince > TimeUnit.SECONDS.toNanos(this.plugin.config().afkTimeoutSeconds());
	}

	public void remove(UUID uuid) {
		this.afkSinceMap.remove(uuid);
	}
}
