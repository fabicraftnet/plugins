package net.fabicraft.paper.core;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.fabicraft.common.command.TranslatableCaptionProvider;
import net.fabicraft.common.command.exception.ExceptionHandlers;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.core.command.BuilderCommand;
import net.fabicraft.paper.core.command.CrafterCommand;
import net.fabicraft.paper.core.command.SignCommand;
import net.fabicraft.paper.core.listener.HuskHomesListener;
import net.fabicraft.paper.core.listener.PlayerListener;
import net.fabicraft.paper.core.locale.CoreTranslationManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.List;

public final class FabiCraftPaperCore extends JavaPlugin {
	public static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver(),
					MiniPlaceholders.globalPlaceholders()
			))
			.build();
	private PaperCommandManager<Source> commandManager;
	private PaperLuckPermsManager luckPermsManager;

	@Override
	public void onEnable() {
		new CoreTranslationManager(getSLF4JLogger());

		this.luckPermsManager = new PaperLuckPermsManager(getSLF4JLogger());

		setupCommandManager();
		registerCommands();
		registerListeners();
	}

	public PaperCommandManager<Source> commandManager() {
		return this.commandManager;
	}

	public void reload() {

	}

	public PaperLuckPermsManager luckPermsManager() {
		return this.luckPermsManager;
	}

	private void setupCommandManager() {
		PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(this);

		commandManager.captionRegistry().registerProvider(new TranslatableCaptionProvider<>());
		commandManager.exceptionController().clearHandlers();
		new ExceptionHandlers<Source>(getSLF4JLogger()).register(commandManager, Source::source);

		this.commandManager = commandManager;
	}

	private void registerCommands() {
		List.of(
				new BuilderCommand(this),
				new CrafterCommand(this),
				new SignCommand(this)
		).forEach(PaperCommand::register);
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));

		if (manager.isPluginEnabled("HuskHomes")) {
			manager.registerEvents(new HuskHomesListener(), this);
		}
	}
}
