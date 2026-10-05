package net.fabicraft.paper.core;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.fabicraft.common.command.ExceptionHandler;
import net.fabicraft.common.command.MinecraftCaptionProvider;
import net.fabicraft.common.config.ConfigManager;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.core.command.*;
import net.fabicraft.paper.core.config.CoreConfig;
import net.fabicraft.paper.core.hook.HuskHomesHook;
import net.fabicraft.paper.core.hook.PaperLuckPermsManager;
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
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FabiCraftPaperCore extends JavaPlugin implements FabiCraftPaperPlugin {
	private final MiniMessage miniMessage = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver(),
					MiniPlaceholders.globalPlaceholders()
			)).build();
	private final ConfigManager<CoreConfig> configManager;
	private final List<FabiCraftPaperPlugin> registered = new ArrayList<>();
	private PaperCommandManager<Source> commandManager;
	private PaperLuckPermsManager luckPermsManager;
	private HuskHomesHook huskHomesHook;

	public FabiCraftPaperCore() {
		this.registered.add(this);
		new CoreTranslationManager(getSLF4JLogger());
		this.configManager = new ConfigManager<>(CoreConfig.class, getDataPath(), "config.toml", getSLF4JLogger());
	}

	@Override
	public void onEnable() {
		this.luckPermsManager = new PaperLuckPermsManager(getSLF4JLogger());
		this.commandManager = createCommandManager();

		PluginManager pluginManager = getServer().getPluginManager();
		if (pluginManager.isPluginEnabled("HuskHomes")) {
			this.huskHomesHook = new HuskHomesHook(this);
		}

		load();

		registerCommands();
		registerListeners();
	}

	public void register(FabiCraftPaperPlugin plugin) {
		this.registered.add(plugin);
	}

	public List<FabiCraftPaperPlugin> registered() {
		return this.registered;
	}

	public PaperCommandManager<Source> commandManager() {
		return this.commandManager;
	}

	@Override
	public void load() {
		this.configManager.load();
		if (this.huskHomesHook != null) {
			this.huskHomesHook.load();
		}
	}

	@Override
	public String identifier() {
		return "core";
	}

	public PaperLuckPermsManager luckPermsManager() {
		return this.luckPermsManager;
	}

	public MiniMessage miniMessage() {
		return this.miniMessage;
	}

	@Override
	public @NotNull Path getDataPath() {
		return getServer().getPluginsFolder().toPath().resolve("FabiCraft/core");
	}

	public CoreConfig config() {
		return this.configManager.config();
	}

	public HuskHomesHook huskHomesHook() {
		return this.huskHomesHook;
	}

	private PaperCommandManager<Source> createCommandManager() {
		PaperCommandManager<Source> manager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(this);
		manager.captionRegistry().registerProvider(new MinecraftCaptionProvider<>());
		new ExceptionHandler<>(getSLF4JLogger(), Source::source).register(manager);
		return manager;
	}

	private void registerCommands() {
		List.of(
				new BonkCommand(this),
				new BuilderCommand(this),
				new CrafterCommand(this),
				new FabiCraftCommand(this),
				new SignCommand(this)
		).forEach(command -> command.register(this.commandManager));
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(
				new PlayerListener(this)
		).forEach(listener -> manager.registerEvents(listener, this));
		if (this.huskHomesHook != null) {
			this.huskHomesHook.registerListeners();
		}
	}
}
