package net.fabicraft.paper.survival;

import net.fabicraft.common.command.TranslatableCaptionProvider;
import net.fabicraft.common.command.exception.ExceptionHandlers;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.command.SurvivalCommandPreProcessor;
import net.fabicraft.paper.survival.command.commands.FabiCraftCommand;
import net.fabicraft.paper.survival.command.commands.GatheringCommand;
import net.fabicraft.paper.survival.command.commands.ReloadCommand;
import net.fabicraft.paper.survival.command.commands.RolePlayCommand;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.items.CustomItemManager;
import net.fabicraft.paper.survival.listener.GatheringListener;
import net.fabicraft.paper.survival.listener.PlayerListener;
import net.fabicraft.paper.survival.locale.SurvivalTranslationManager;
import net.fabicraft.paper.survival.placeholder.MiniPlaceholders;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class FabiCraftPaperSurvival extends JavaPlugin {
	private PaperCommandManager<Source> commandManager;
	private PaperLuckPermsManager luckPermsManager;
	private GatheringManager gatheringManager;
	private CustomItemManager customItemManager;

	@Override
	public void onEnable() {
		new SurvivalTranslationManager(getSLF4JLogger());

		this.gatheringManager = new GatheringManager(this);
		this.customItemManager = new CustomItemManager(this);

		this.luckPermsManager = new PaperLuckPermsManager(getSLF4JLogger());

		setupCommandManager();
		registerCommands();

		registerListeners();

		new MiniPlaceholders(this).register();

		try {
			load();
		} catch (IOException e) {
			getSLF4JLogger().error("Couldn't load plugin", e);
		}
	}

	public PaperCommandManager<Source> commandManager() {
		return this.commandManager;
	}

	public GatheringManager gatheringManager() {
		return this.gatheringManager;
	}

	public PaperLuckPermsManager luckPermsManager() {
		return this.luckPermsManager;
	}

	public void load() throws IOException {
		this.gatheringManager.load();
		this.customItemManager.load();
	}

	public CustomItemManager customItemManager() {
		return this.customItemManager;
	}

	private void setupCommandManager() {
		PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(this);

		commandManager.registerCommandPreProcessor(new SurvivalCommandPreProcessor<>(this));

		commandManager.captionRegistry().registerProvider(new TranslatableCaptionProvider<>());
		commandManager.exceptionController().clearHandlers();
		new ExceptionHandlers<Source>(getSLF4JLogger()).register(commandManager, Source::source);

		this.commandManager = commandManager;
	}

	@Override
	public @NotNull Path getDataPath() {
		return getServer().getPluginsFolder().toPath().resolve("FabiCraft/survival");
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new GatheringListener(this),
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	private void registerCommands() {
		List.of(
				new FabiCraftCommand(this),
				new GatheringCommand(this),
				new ReloadCommand(this),
				new RolePlayCommand(this)
		).forEach(PaperCommand::register);
	}
}
