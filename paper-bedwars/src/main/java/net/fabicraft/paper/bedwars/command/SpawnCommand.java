package net.fabicraft.paper.bedwars.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.bedwars.FabiCraftPaperBedwars;
import net.fabicraft.paper.core.command.PaperCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import org.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.Collection;

public final class SpawnCommand extends PaperCommand<FabiCraftPaperBedwars> {
	private static final String PERMISSION = "fabicraft.paper.bedwars.command.spawn";
	private static final String PERMISSION_OTHER = "fabicraft.paper.bedwars.command.spawn.other";

	public SpawnCommand(FabiCraftPaperBedwars plugin) {
		super(plugin);
	}

	@Override
	public void register(PaperCommandManager<Source> manager) {
		var builder = manager.commandBuilder("spawn");
		manager.command(builder.senderType(PlayerSource.class).permission(PERMISSION).handler(this::handle));
		manager.command(builder.required("selector", MultiplePlayerSelectorParser.multiplePlayerSelectorParser()).permission(PERMISSION_OTHER).handler(this::handleOther));
	}

	private void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		super.plugin.api().sendToHub(player);
	}

	private void handleOther(CommandContext<Source> context) {
		MultiplePlayerSelector selector = context.get("selector");
		Collection<Player> players = selector.values();
		players.forEach(player -> super.plugin.api().sendToHub(player));

		CommandSender sender = context.sender().source();
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
				"fabicraft.paper.bedwars.command.spawn.other." + singleMultipleKey,
				MessageType.SUCCESS,
				targetComponent
		));
	}
}
