package net.fabicraft.paper.core;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.fabicraft.common.command.ExceptionHandler;
import net.fabicraft.common.command.MinecraftCaptionProvider;
import net.fabicraft.common.locale.BrandColor;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.core.command.BonkCommand;
import net.fabicraft.paper.core.command.BuilderCommand;
import net.fabicraft.paper.core.command.CrafterCommand;
import net.fabicraft.paper.core.command.SignCommand;
import net.fabicraft.paper.core.config.ConfigManager;
import net.fabicraft.paper.core.config.CoreConfig;
import net.fabicraft.paper.core.hook.HuskHomesHook;
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
import java.util.List;

public final class FabiCraftPaperCore extends JavaPlugin {
	private final MiniMessage miniMessage = MiniMessage.builder()
			.tags(TagResolver.resolver(
					StandardTags.defaults(),
					BrandColor.resolver(),
					MiniPlaceholders.globalPlaceholders()
			))
			.build();
	private final ConfigManager configManager;
	private PaperCommandManager<Source> commandManager;
	private PaperLuckPermsManager luckPermsManager;
	private HuskHomesHook huskHomesHook;

	public FabiCraftPaperCore() {
		new CoreTranslationManager(getSLF4JLogger());
		this.configManager = new ConfigManager(this);
	}

	@Override
	public void onEnable() {
		this.luckPermsManager = new PaperLuckPermsManager(getSLF4JLogger());

		setupCommandManager();

		PluginManager pluginManager = getServer().getPluginManager();
		if (pluginManager.isPluginEnabled("HuskHomes")) {
			this.huskHomesHook = new HuskHomesHook(this);
		}

		load();

		registerCommands();
		registerListeners();
	}

	public PaperCommandManager<Source> commandManager() {
		return this.commandManager;
	}

	public void load() {
		this.configManager.load();
		this.huskHomesHook.load();
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

	private void setupCommandManager() {
		PaperCommandManager<Source> commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.simpleCoordinator())
				.buildOnEnable(this);

		commandManager.captionRegistry().registerProvider(new MinecraftCaptionProvider<>());
		new ExceptionHandler<>(getSLF4JLogger(), Source::source).register(commandManager);

		this.commandManager = commandManager;
	}

	private void registerCommands() {
		List.of(
				new BonkCommand(this),
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
		if (this.huskHomesHook != null) {
			this.huskHomesHook.registerListeners();
		}
	}
}
