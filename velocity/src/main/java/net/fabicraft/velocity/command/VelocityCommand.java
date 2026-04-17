package net.fabicraft.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import net.fabicraft.velocity.FabiCraftVelocity;
import org.incendo.cloud.velocity.VelocityCommandManager;

public abstract class VelocityCommand {
	protected final VelocityCommandManager<CommandSource> manager;
	protected final FabiCraftVelocity plugin;

	public VelocityCommand(FabiCraftVelocity plugin) {
		this.plugin = plugin;
		this.manager = plugin.commandManager();
	}

	public abstract void register();
}
