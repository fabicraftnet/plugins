package net.fabicraft.paper.survival.command;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import org.incendo.cloud.execution.preprocessor.CommandPreprocessor;

public final class SurvivalCommandPreProcessor<C> implements CommandPreprocessor<C> {
	private final FabiCraftPaperSurvival plugin;

	public SurvivalCommandPreProcessor(FabiCraftPaperSurvival plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(@NonNull CommandPreprocessingContext<C> context) {
		CommandContext<C> commandContext = context.commandContext();
		commandContext.store(SurvivalCommandContextKeys.CUSTOM_ITEM_MANAGER_KEY, this.plugin.customItemManager());
	}
}
