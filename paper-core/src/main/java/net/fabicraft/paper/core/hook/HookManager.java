package net.fabicraft.paper.core.hook;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class HookManager<P extends JavaPlugin> {
	protected final P plugin;
	private final PluginManager pluginManager;
	private final List<Hook> hooks = new ArrayList<>();

	public HookManager(P plugin) {
		this.plugin = plugin;
		this.pluginManager = plugin.getServer().getPluginManager();
	}

	public abstract void register();

	protected <H extends Hook> H register(String pluginName, Supplier<H> supplier) {
		if (!this.pluginManager.isPluginEnabled(pluginName)) {
			return null;
		}
		try {
			this.plugin.getSLF4JLogger().info("Enabling {} hook", pluginName);
			H hook = supplier.get();
			hook.register();
			this.hooks.add(hook);
			return hook;
		} catch (Exception | LinkageError e) { // NoClassDefFoundError is a LinkageError
			this.plugin.getSLF4JLogger().warn("Failed to enable {} hook", pluginName, e);
			return null;
		}
	}

	protected void logSummary() {
		this.plugin.getSLF4JLogger().info("Total of {} hooks registered", this.hooks.size());
	}
}
