package net.fabicraft.velocity.command.commands;

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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class HubCommand extends VelocityCommand {
	private static final TranslatableComponent COMPONENT_FAILED = Components.translatable("fabicraft.velocity.command.hub.failed", MessageType.ERROR);
	private static final TranslatableComponent COMPONENT_CONNECTION_FAILED = Components.translatable("fabicraft.velocity.command.hub.connection-failed", MessageType.ERROR);
	private static final TranslatableComponent COMPONENT_REASON_UNKNOWN = Component.translatable("fabicraft.velocity.command.hub.reason.unknown");
	private static final TranslatableComponent COMPONENT_ALREADY_CONNECTED = Components.translatable("fabicraft.velocity.command.hub.already-connected", MessageType.ERROR);

	public HubCommand(FabiCraftVelocity plugin) {
		super(plugin);
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("hub");
		super.manager.command(builder.senderType(Player.class).permission("fabicraft.velocity.command.hub").handler(this::handle));
	}

	private void handle(CommandContext<Player> context) {
		Player player = context.sender();
		List<String> attemptConnectionOrder = super.plugin.server().getConfiguration().getAttemptConnectionOrder();

		Optional<ServerConnection> currentServerOptional = player.getCurrentServer();
		if (currentServerOptional.isPresent() && currentServerOptional.get().getServerInfo().getName().equalsIgnoreCase(attemptConnectionOrder.getFirst())) {
			player.sendMessage(COMPONENT_ALREADY_CONNECTED);
			return;
		}

		connectToNext(player, super.plugin.server().getConfiguration().getAttemptConnectionOrder().iterator());
	}

	private void connectToNext(Player player, Iterator<String> iterator) {
		if (!iterator.hasNext()) {
			this.plugin.logger().error("{} was unable to connect to a Hub server", player.getUsername());
			player.sendMessage(COMPONENT_FAILED);
			return;
		}

		String serverName = iterator.next();

		Optional<RegisteredServer> optional = this.plugin.server().getServer(serverName);

		if (optional.isEmpty()) {
			super.plugin.logger().warn("Can not connect {} to {} as registered server could not be found", player.getUsername(), serverName);
			connectToNext(player, iterator);
			return;
		}

		player.createConnectionRequest(optional.get())
				.connect()
				.thenAccept(result -> {
					if (!result.isSuccessful()) {
						Component reason = result.getReasonComponent().orElse(COMPONENT_REASON_UNKNOWN);
						super.plugin.logger().warn("Can not connect {} to {}. Reason: {}", player.getUsername(), serverName, PlainTextComponentSerializer.plainText().serialize(reason));
						player.sendMessage(COMPONENT_CONNECTION_FAILED.arguments(reason));
						connectToNext(player, iterator);
					}
				});
	}
}
