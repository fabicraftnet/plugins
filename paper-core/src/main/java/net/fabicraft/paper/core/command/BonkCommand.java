package net.fabicraft.paper.core.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.Collection;
import java.util.Random;

import static org.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser.multiplePlayerSelectorParser;
import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;

public final class BonkCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION = "fabicraft.paper.core.command.bonk";
	private static final String PERMISSION_BROADCAST = "fabicraft.paper.core.command.bonk.broadcast";
	private static final String PERMISSION_BROADCAST_SENDER = "fabicraft.paper.core.command.bonk.broadcast.sender";
	private static final Component COMPONENT_BONKED = Components.translatable(
			"fabicraft.paper.core.command.bonk.bonked",
			MessageType.WARNING
	);

	public BonkCommand(FabiCraftPaperCore plugin) {
		super(plugin);
	}

	@Override
	public void register(PaperCommandManager<Source> manager) {
		Command.Builder<Source> builder = manager.commandBuilder("bonk")
				.permission(PERMISSION)
				.required("players", multiplePlayerSelectorParser(false))
				.optional("amount", integerParser(1, 10000))
				.optional("radius", integerParser(0, 100))
				.handler(this::execute);

		manager.command(builder);
	}

	private void execute(CommandContext<Source> context) {
		CommandSender sender = context.sender().source();
		MultiplePlayerSelector selector = context.get("players");
		Collection<Player> players = selector.values();

		int amount = context.getOrDefault("amount", 20);
		int radius = context.getOrDefault("radius", 3);

		players.forEach(player -> {
			playBellSound(player, amount, radius);
			player.damage(0.01);
			player.setVelocity(Vector.getRandom());
			player.sendMessage(COMPONENT_BONKED);
		});

		String singleMultipleKey;
		Component targetComponent;
		if (players.size() > 1) {
			singleMultipleKey = "multiple";
			targetComponent = Components.playerCount(players);
		} else {
			singleMultipleKey = "single";
			targetComponent = Components.player(players.iterator().next());
		}

		sender.sendMessage(Components.translatable(
				"fabicraft.paper.core.command.bonk." + singleMultipleKey,
				MessageType.SUCCESS,
				targetComponent
		));

		Component broadcastComponent = Components.translatable(
				"fabicraft.paper.core.command.bonk." + singleMultipleKey + ".broadcast",
				MessageType.INFO,
				targetComponent
		);
		Component broadcastSenderComponent = Components.translatable(
				"fabicraft.paper.core.command.bonk." + singleMultipleKey + ".broadcast.sender",
				MessageType.INFO,
				Components.player(sender),
				targetComponent
		);
		this.plugin.getServer().getOnlinePlayers().forEach(player -> {
			if (player.equals(sender) || players.contains(player)) {
				return;
			}
			if (player.hasPermission(PERMISSION_BROADCAST_SENDER)) {
				player.sendMessage(broadcastSenderComponent);
				return;
			}
			if (player.hasPermission(PERMISSION_BROADCAST)) {
				player.sendMessage(broadcastComponent);
			}
		});
	}

	private void playBellSound(Player target, int points, int radius) {
		Location centerLocation = target.getLocation();
		double angleStep = 2 * Math.PI / points;

		Random random = new Random();
		for (int i = 0; i < points; i++) {
			double angle = i * angleStep;
			Location location = centerLocation.clone().add(radius * Math.cos(angle), 0, radius * Math.sin(angle));
			//location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 1);
			location.getWorld().playSound(location, Sound.BLOCK_BELL_USE, 1, random.nextFloat(2));
		}
	}
}
