package net.fabicraft.paper.core.command;

import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public abstract class PaperCommand<P> {
	protected final P plugin;

	public PaperCommand(P plugin) {
		this.plugin = plugin;
	}

	public abstract void register(PaperCommandManager<Source> manager);
}
