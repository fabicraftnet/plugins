package net.fabicraft.paper.survival.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.TranslatableComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.Source;

import java.io.IOException;

public final class ReloadCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final TranslatableComponent COMPONENT_SUCCESS = Components.translatable(
			"fabicraft.paper.core.command.reload.success",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_FAILURE = Components.translatable(
			"fabicraft.paper.core.command.reload.failure",
			MessageType.ERROR
	);
	private static final String PERMISSION = "fabicraft.paper.core.command.reload";

	public ReloadCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("reload").permission(PERMISSION).handler(this::handle);
		super.manager.command(builder);
	}

	private void handle(CommandContext<Source> context) {
		try {
			super.plugin.load();
		} catch (IOException e) {
			context.sender().source().sendMessage(COMPONENT_FAILURE);
			return;
		}

		context.sender().source().sendMessage(COMPONENT_SUCCESS);
	}
}
