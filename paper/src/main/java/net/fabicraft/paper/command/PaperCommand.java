package net.fabicraft.paper.command;

import net.fabicraft.paper.FabiCraftPaper;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public abstract class PaperCommand {
	protected final PaperCommandManager<Source> manager;
	protected final FabiCraftPaper plugin;

	public PaperCommand(FabiCraftPaper plugin) {
		this.plugin = plugin;
		this.manager = plugin.commandManager();
	}

	public abstract void register();
}
