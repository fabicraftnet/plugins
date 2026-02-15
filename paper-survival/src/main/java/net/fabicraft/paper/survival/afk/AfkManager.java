package net.fabicraft.paper.survival.afk;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.config.SurvivalConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class AfkManager {
	private static final TranslatableComponent COMPONENT_KICK = Components.translatable(
			"fabicraft.paper.survival.afk.kick",
			MessageType.INFO
	);
	private static final TranslatableComponent COMPONENT_INFO = Component.translatable(
			"fabicraft.paper.survival.afk.info"
	);
	private final Map<UUID, AfkStatus> afkStatusMap = new ConcurrentHashMap<>();
	private final FabiCraftPaperSurvival plugin;
	private long afkMarkNanos;
	private long kickNanos;
	private long warnNanos;
	private TranslatableComponent warnComponent;
	private ScheduledFuture<?> scheduledFuture;

	public AfkManager(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	public void load() {
		SurvivalConfig config = this.plugin.config();

		this.afkMarkNanos = TimeUnit.SECONDS.toNanos(this.plugin.config().afkMarkSeconds());
		this.kickNanos = TimeUnit.SECONDS.toNanos(this.plugin.config().afkKickSeconds());
		this.warnNanos = this.kickNanos - TimeUnit.SECONDS.toNanos(config.afkWarnBeforeKickSeconds());

		this.warnComponent = Components.translatable(
				"fabicraft.paper.survival.afk.warn",
				MessageType.WARNING,
				config.afkWarnBeforeKickSeconds()
		);

		if (this.scheduledFuture == null) {
			this.scheduledFuture = this.plugin.executor().scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(this.plugin, this::tick), 0, 5, TimeUnit.SECONDS);
		}
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

		return status.state() != AfkState.NOT_AFK;
	}

	public void remove(UUID uuid) {
		this.afkStatusMap.remove(uuid);
	}

	private void tick() {
		this.afkStatusMap.entrySet().removeIf(entry -> {
			Player player = this.plugin.getServer().getPlayer(entry.getKey());
			if (player == null) {
				return true;
			}

			AfkStatus status = entry.getValue();

			switch (status.state()) {
				case NOT_AFK -> {
					if (status.hasBeenAfkFor() < afkMarkNanos) {
						return false;
					}
					status.state(AfkState.AFK);
					player.sendMessage(COMPONENT_INFO);
				}
				case AFK -> {
					if (status.hasBeenAfkFor() < this.warnNanos) {
						return false;
					}
					status.state(AfkState.WARNED);
					player.sendMessage(this.warnComponent);
				}
				case WARNED -> {
					if (status.hasBeenAfkFor() < kickNanos || player.hasPermission("fabicraft.paper.survival.afk.kick.bypass")) {
						return false;
					}
					player.kick(COMPONENT_KICK, PlayerKickEvent.Cause.IDLING);
				}
			}
			return false;
		});
	}
}
