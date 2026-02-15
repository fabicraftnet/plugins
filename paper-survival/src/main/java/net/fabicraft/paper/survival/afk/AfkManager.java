package net.fabicraft.paper.survival.afk;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class AfkManager {
	private static final TranslatableComponent COMPONENT_KICK = Components.translatable(
			"fabicraft.paper.survival.afk.kick",
			MessageType.INFO
	);
	private final Map<UUID, AfkStatus> afkStatusMap = new ConcurrentHashMap<>();
	private final FabiCraftPaperSurvival plugin;

	public AfkManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	public void start() {
		this.plugin.executor().scheduleAtFixedRate(() -> {
			int afkKickSeconds = this.plugin.config().afkKickSeconds();
			int afkWarnBeforeKickSeconds = this.plugin.config().afkWarnBeforeKickSeconds();

			long kickNanos = TimeUnit.SECONDS.toNanos(afkKickSeconds);
			long warnNanos = kickNanos - TimeUnit.SECONDS.toNanos(afkWarnBeforeKickSeconds);

			TranslatableComponent warnComponent = Components.translatable(
					"fabicraft.paper.survival.afk.warn",
					MessageType.WARNING,
					TimeUnit.SECONDS.toMinutes(afkKickSeconds - afkWarnBeforeKickSeconds),
					afkWarnBeforeKickSeconds
			);

			List<UUID> playersToRemove = new ArrayList<>();

			this.afkStatusMap.forEach((uuid, status) -> {
				Player player = this.plugin.getServer().getPlayer(uuid);
				if (player == null) {
					playersToRemove.add(uuid);
					return;
				}

				if (status.hasBeenAfkFor() >= warnNanos && !status.warned()) {
					player.sendMessage(warnComponent);
					status.warned(true);
					return;
				}

				if (status.hasBeenAfkFor() < kickNanos || player.hasPermission("fabicraft.paper.survival.afk.kick.bypass")) {
					return;
				}

				Bukkit.getScheduler().runTask(this.plugin, () -> player.kick(COMPONENT_KICK, PlayerKickEvent.Cause.IDLING));
			});
			playersToRemove.forEach(this.afkStatusMap::remove);
		}, 0, 5, TimeUnit.SECONDS);
	}

	public void markAsActive(UUID uuid) {
		this.afkStatusMap.compute(uuid, (uuid1, status) -> {
			if (status == null) {
				return new AfkStatus();
			}
			status.markAsActive();
			return status;
		});
	}

	public boolean afk(UUID uuid) {
		AfkStatus status = this.afkStatusMap.get(uuid);
		if (status == null) {
			return false;
		}

		return status.hasBeenAfkFor() > TimeUnit.SECONDS.toNanos(this.plugin.config().afkMarkSeconds());
	}

	public void remove(UUID uuid) {
		this.afkStatusMap.remove(uuid);
	}
}
