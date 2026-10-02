package net.fabicraft.paper.common.command;

import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public abstract class PaperCommand<P> {
	protected final PaperCommandManager<Source> manager;
	protected final P plugin;

	public PaperCommand(P plugin, PaperCommandManager<Source> manager) {
		this.plugin = plugin;
		this.manager = manager;
	}

	public abstract void register();
}
