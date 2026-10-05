package net.fabicraft.paper.survival;

import net.fabicraft.common.config.ConfigManager;
import net.fabicraft.paper.core.FabiCraftPaperCore;
import net.fabicraft.paper.core.FabiCraftPaperPlugin;
import net.fabicraft.paper.survival.arena.ArenaManager;
import net.fabicraft.paper.survival.command.SurvivalCommandPreProcessor;
import net.fabicraft.paper.survival.command.commands.FabiCraftCommand;
import net.fabicraft.paper.survival.command.commands.GatheringCommand;
import net.fabicraft.paper.survival.command.commands.RoleplayCommand;
import net.fabicraft.paper.survival.config.SurvivalConfig;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.fabicraft.paper.survival.hook.SurvivalHookManager;
import net.fabicraft.paper.survival.listener.EntityListener;
import net.fabicraft.paper.survival.listener.GatheringListener;
import net.fabicraft.paper.survival.listener.PlayerListener;
import net.fabicraft.paper.survival.locale.SurvivalTranslationManager;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class FabiCraftPaperSurvival extends JavaPlugin implements FabiCraftPaperPlugin {
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private final ConfigManager<SurvivalConfig> configManager;
	private final GatheringManager gatheringManager;
	private final ItemManager itemManager;
	private final StorageManager storageManager;
	private final PlayerDataManager playerDataManager;
	private final SurvivalHookManager hookManager;
	private final ArenaManager arenaManager;
	private FabiCraftPaperCore core;

	public FabiCraftPaperSurvival() {
		new SurvivalTranslationManager(getSLF4JLogger());
		this.configManager = new ConfigManager<>(SurvivalConfig.class, getDataPath(), "config.toml", getSLF4JLogger());
		this.hookManager = new SurvivalHookManager(this);
		this.itemManager = new ItemManager(this);
		this.storageManager = new StorageManager(getDataPath());
		this.gatheringManager = new GatheringManager(this);
		this.playerDataManager = new PlayerDataManager(this);
		this.arenaManager = new ArenaManager();
	}

	@Override
	public void onEnable() {
		this.core = getPlugin(FabiCraftPaperCore.class);
		this.core.register(this);

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

	public FabiCraftPaperCore core() {
		return this.core;
	}

	public GatheringManager gatheringManager() {
		return this.gatheringManager;
	}

	@Override
	public void load() throws IOException {
		this.configManager.load();
		this.storageManager.migrate();
		this.gatheringManager.load();
		this.itemManager.load();
	}

	@Override
	public String identifier() {
		return "survival";
	}

	public ItemManager itemManager() {
		return this.itemManager;
	}

	public PlayerDataManager playerDataManager() {
		return this.playerDataManager;
	}

	private void setupCommandManager() {
		this.core.commandManager().registerCommandPreProcessor(new SurvivalCommandPreProcessor<>(this));
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

	public ArenaManager arenaManager() {
		return this.arenaManager;
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
				new FabiCraftCommand(this),
				new GatheringCommand(this),
				new RoleplayCommand(this)
		).forEach(command -> command.register(this.core.commandManager()));
	}
}
