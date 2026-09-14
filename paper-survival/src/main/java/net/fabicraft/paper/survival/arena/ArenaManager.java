package net.fabicraft.paper.survival.arena;

import java.util.ArrayList;
import java.util.List;

public final class ArenaManager {
	private final List<PlayerArena> playerArenas = new ArrayList<>();

	public void load() {

	}

	public List<PlayerArena> playerArenas() {
		return this.playerArenas;
	}
}
