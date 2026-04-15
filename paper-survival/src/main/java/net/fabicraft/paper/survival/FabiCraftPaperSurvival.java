package net.fabicraft.paper.survival;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.fabicraft.common.command.TranslatableCaptionProvider;
import net.fabicraft.common.command.exception.ExceptionHandlers;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.command.SurvivalCommandPreProcessor;
import net.fabicraft.paper.survival.command.commands.FabiCraftSurvivalCommand;
import net.fabicraft.paper.survival.command.commands.GatheringCommand;
import net.fabicraft.paper.survival.command.commands.RoleplayCommand;
import net.fabicraft.paper.survival.config.ConfigManager;
import net.fabicraft.paper.survival.config.SurvivalConfig;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.hook.HookManager;
import net.fabicraft.paper.survival.listener.EntityListener;
import net.fabicraft.paper.survival.listener.GatheringListener;
import net.fabicraft.paper.survival.listener.PlayerListener;
import net.fabicraft.paper.survival.locale.SurvivalTranslationManager;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class FabiCraftPaperSurvival extends JavaPlugin {
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final ConfigManager configManager;
	private final GatheringManager gatheringManager;
	private final ItemManager itemManager;
	private final StorageManager storageManager;
	private final PlayerDataManager playerDataManager;
	private final HookManager hookManager;
	private final MiniMessage miniMessage = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver(),
					MiniPlaceholders.globalPlaceholders()
			))
			.build();
	private PaperLuckPermsManager luckPermsManager;
	private PaperCommandManager<Source> commandManager;

	public FabiCraftPaperSurvival() {
		new SurvivalTranslationManager(getSLF4JLogger());
		this.configManager = new ConfigManager(this);
		this.hookManager = new HookManager(this);
		this.itemManager = new ItemManager(this);
		this.storageManager = new StorageManager(getDataPath());
		this.gatheringManager = new GatheringManager(this);
		this.playerDataManager = new PlayerDataManager(this);
	}

	@Override
	public void onEnable() {
		this.luckPermsManager = new PaperLuckPermsManager(getSLF4JLogger());
		setupCommandManager();

		try {
			load();
		} catch (IOException e) {
			getSLF4JLogger().error("Couldn't load plugin", e);
		}

		registerCommands();
		registerListeners();
		this.hookManager.register();
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
		this.configManager.load();
		this.storageManager.migrate();
		this.gatheringManager.load();
		this.itemManager.load();
	}

	public ItemManager itemManager() {
		return this.itemManager;
	}

	public PlayerDataManager playerDataManager() {
		return this.playerDataManager;
	}

	public MiniMessage miniMessage() {
		return this.miniMessage;
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

	public ScheduledExecutorService executor() {
		return this.executor;
	}

	public SurvivalConfig config() {
		return this.configManager.config();
	}

	public StorageManager storageManager() {
		return this.storageManager;
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new EntityListener(this),
				new GatheringListener(this),
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
	}

	private void registerCommands() {
		List.of(
				new FabiCraftSurvivalCommand(this),
				new GatheringCommand(this),
				new RoleplayCommand(this)
		).forEach(PaperCommand::register);
	}
}
