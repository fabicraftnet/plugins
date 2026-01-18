package net.fabicraft.paper;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.fabicraft.common.command.TranslatableCaptionProvider;
import net.fabicraft.common.command.exception.ExceptionHandlers;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.command.PaperCommand;
import net.fabicraft.paper.command.commands.BuilderCommand;
import net.fabicraft.paper.locale.PaperTranslationManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public final class FabiCraftPaper extends JavaPlugin {
	public static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver(),
					MiniPlaceholders.globalPlaceholders()
			))
			.build();
	private PaperCommandManager<Source> commandManager;

	@Override
	public void onEnable() {
		new PaperTranslationManager(getSLF4JLogger());

		setupCommandManager();
		registerCommands();
		registerListeners();
	}

	public PaperCommandManager<Source> commandManager() {
		return this.commandManager;
	}

	public void reload() {

	}

	private void setupCommandManager() {
		PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(this);

		commandManager.captionRegistry().registerProvider(new TranslatableCaptionProvider<>());
		this.commandManager.exceptionController().clearHandlers();
		new ExceptionHandlers<Source>().register(commandManager, Source::source);

		this.commandManager = commandManager;
	}

	private void registerCommands() {
		List.of(
				new BuilderCommand(this)
		).forEach(PaperCommand::register);
	}

	private void registerListeners() {

	}

	@Override
	public @NotNull Path getDataPath() {
		return getServer().getPluginsFolder().toPath().resolve("FabiCraft");
	}
}
