package net.fabicraft.paper.core;

import java.io.IOException;

public interface FabiCraftPaperPlugin {
	void load() throws IOException;

	String identifier();
}
