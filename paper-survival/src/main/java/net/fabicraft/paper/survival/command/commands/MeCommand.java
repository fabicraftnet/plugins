package net.fabicraft.paper.survival.command.commands;

import net.fabicraft.common.luckperms.LuckPermsManager;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.parser.standard.StringParser;

public final class MeCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final String PERMISSION = "fabicraft.paper.survival.command.me";
	private final LuckPermsManager<Player> luckPermsManager;

	public MeCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
		this.luckPermsManager = plugin.luckPermsManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("me")
				.permission(PERMISSION)
				.senderType(PlayerSource.class)
				.required("action", StringParser.greedyStringParser())
				.handler(this::handle);
		super.manager.command(builder);
	}

	private void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		String action = context.get("action");

		Component component = super.plugin.miniMessage().deserialize(
				super.plugin.config().roleplay().meFormat(),
				Placeholder.unparsed("name", super.plugin.playerDataManager().data(player).characterName()),
				Placeholder.unparsed("action", action)
		);

		int chatRadius = super.plugin.config().roleplay().meRadius();
		player.getLocation().getNearbyPlayers(chatRadius).forEach(other -> {
			if (this.luckPermsManager.hasGroup(other, "roleplay")) {
				other.sendMessage(component);
			}
		});
	}
}
