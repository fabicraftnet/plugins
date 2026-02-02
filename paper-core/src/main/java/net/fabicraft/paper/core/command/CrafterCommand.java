package net.fabicraft.paper.core.command;

import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.command.parser.MenuTypeParser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;

public final class CrafterCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION = "fabicraft.paper.core.command.crafter";

	public CrafterCommand(FabiCraftPaperCore plugin) {
		super(plugin, plugin.commandManager());
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("crafter")
				.senderType(PlayerSource.class)
				.permission(PERMISSION);

		super.manager.command(builder
				.required("crafter", MenuTypeParser.crafters())
				.handler(this::handle)
		);
	}

	public void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		MenuType.Typed<?, ?> crafter = context.get("crafter");
		player.openInventory(crafter.create(player));
	}
}
