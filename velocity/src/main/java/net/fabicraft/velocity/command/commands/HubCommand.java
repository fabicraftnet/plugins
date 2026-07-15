package net.fabicraft.velocity.command.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.velocity.FabiCraftVelocity;
import net.fabicraft.velocity.command.VelocityCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.velocity.parser.PlayerParser;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class HubCommand extends VelocityCommand {
	private static final TranslatableComponent COMPONENT_ALREADY_CONNECTED = Components.translatable("fabicraft.velocity.command.hub.already-connected", MessageType.ERROR);
	private static final TranslatableComponent COMPONENT_ALREADY_CONNECTED_OTHER = Components.translatable("fabicraft.velocity.command.hub.already-connected.other", MessageType.ERROR);

	public HubCommand(FabiCraftVelocity plugin) {
		super(plugin);
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("hub");
		super.manager.command(builder.senderType(Player.class).permission("fabicraft.velocity.command.hub").handler(this::handle));
		super.manager.command(builder.permission("fabicraft.velocity.command.hub.other").required("player", PlayerParser.playerParser()).handler(this::handleOther));
	}

	private void handle(CommandContext<Player> context) {
		Player player = context.sender();

		List<String> attemptConnectionOrder = super.plugin.server().getConfiguration().getAttemptConnectionOrder();
		if (attemptConnectionOrder.isEmpty()) {
			//TODO log message and notify player
			return;
		}

		if (isConnectedToServerNamed(player, attemptConnectionOrder.getFirst())) {
			player.sendMessage(COMPONENT_ALREADY_CONNECTED);
			return;
		}

		connectToNext(player, attemptConnectionOrder.iterator());
	}

	private void handleOther(CommandContext<CommandSource> context) {
		Player player = context.get("player");
		List<String> attemptConnectionOrder = super.plugin.server().getConfiguration().getAttemptConnectionOrder();
		if (attemptConnectionOrder.isEmpty()) {
			//TODO log message and notify player
			return;
		}

		if (isConnectedToServerNamed(player, attemptConnectionOrder.getFirst())) {
			context.sender().sendMessage(COMPONENT_ALREADY_CONNECTED_OTHER.arguments(Components.player(player)));
			return;
		}

		connectToNext(player, attemptConnectionOrder.iterator());
	}

	private CompletableFuture<Boolean> connectToNext(Player player, Iterator<String> iterator) {
		if (!iterator.hasNext()) {
			this.plugin.logger().error("Unable to find a Hub server for {}", player.getUsername());
			player.sendMessage(Component.translatable("velocity.error.no-available-servers"));
			return CompletableFuture.completedFuture(false);
		}

		String serverName = iterator.next();

		Optional<RegisteredServer> optional = this.plugin.server().getServer(serverName);

		if (optional.isEmpty()) {
			super.plugin.logger().warn("Unable to connect {} to Hub. Server {} does not exist. Please check the servers.try section in the Velocity configuration", player.getUsername(), serverName);
			player.sendMessage(Component.translatable("velocity.error.cant-connect").arguments(Component.translatable()));
			return connectToNext(player, iterator);
		}

		return player.createConnectionRequest(optional.get()).connect().thenCompose(result -> {
			if (result.isSuccessful()) {
				return CompletableFuture.completedFuture(true);
			}

			Optional<Component> reasonOptional = result.getReasonComponent();
			if (reasonOptional.isPresent()) {
				Component reason = reasonOptional.get();
				player.sendMessage(Component.translatable("velocity.error.cant-connect").arguments(reason));
				super.plugin.logger().warn("Unable to connect {} to {}: {}", player.getUsername(), serverName, PlainTextComponentSerializer.plainText().serialize(reason));
			} else {
				player.sendMessage(Component.translatable("velocity.error.connecting-server-error"));
				super.plugin.logger().warn("Unable to connect {} to {}", player.getUsername(), serverName);
			}
			return connectToNext(player, iterator);
		});
	}

	private boolean isConnectedToServerNamed(Player player, String name) {
		Optional<ServerConnection> currentServerOptional = player.getCurrentServer();
		return currentServerOptional.isPresent() && currentServerOptional.get().getServerInfo().getName().equalsIgnoreCase(name);
	}
}
