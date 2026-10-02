package net.fabicraft.paper.survival.arena;

import java.util.ArrayList;
import java.util.List;

public final class ArenaManager {
	private final List<Arena> arenas = new ArrayList<>();

	public void load() {

	}

	public List<Arena> playerArenas() {
		return this.arenas;
	}
}
