package net.fabicraft.paper.common.command;

import net.fabicraft.common.command.ExceptionHandler;
import net.fabicraft.common.command.MinecraftCaptionProvider;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;

public final class CommandManagerProvider {
	public PaperCommandManager<Source> manager(Plugin plugin) {
		PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(plugin);
		commandManager.captionRegistry().registerProvider(new MinecraftCaptionProvider<>());
		new ExceptionHandler<>(plugin.getSLF4JLogger(), Source::source).register(commandManager);

		return commandManager;
	}
}
