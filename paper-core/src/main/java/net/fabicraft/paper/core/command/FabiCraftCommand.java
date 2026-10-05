package net.fabicraft.paper.core.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.core.FabiCraftPaperPlugin;
import net.kyori.adventure.text.TranslatableComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public final class FabiCraftCommand extends PaperCommand<FabiCraftPaperCore> {
	private static final String PERMISSION_RELOAD = "fabicraft.paper.core.command.fabicraft.reload";
	private static final TranslatableComponent COMPONENT_RELOAD_ALL_SUCCESS = Components.translatable(
			"fabicraft.paper.core.command.fabicraft.reload.all.success",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_RELOAD_ALL_FAILURE = Components.translatable(
			"fabicraft.paper.core.command.fabicraft.reload.all.failure",
			MessageType.ERROR
	);

	public FabiCraftCommand(FabiCraftPaperCore plugin) {
		super(plugin);
	}

	@Override
	public void register(PaperCommandManager<Source> manager) {
		var builder = manager.commandBuilder("fabicraft");
		manager.command(builder.literal("reload").permission(PERMISSION_RELOAD).handler(this::handleReload));
	}

	//TODO Subcommand to only reload one of the plugins
	private void handleReload(CommandContext<Source> context) {
		try {
			for (FabiCraftPaperPlugin plugin : super.plugin.registered()) {
				plugin.load();
			}
		} catch (Exception exception) {
			super.plugin.getSLF4JLogger().error("Reload failed", exception);
			context.sender().source().sendMessage(COMPONENT_RELOAD_ALL_FAILURE);
			return;
		}

		context.sender().source().sendMessage(COMPONENT_RELOAD_ALL_SUCCESS);
	}
}
