package net.fabicraft.paper.survival.arena;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PlayerArena extends Arena {
	private final List<UUID> builders = new ArrayList<>();
	private Location spawn;

	public PlayerArena(String identifier) {
		super(identifier);
	}

	public Location spawn() {
		return this.spawn;
	}

	public void spawn(Location spawn) {
		if (spawn == null) {
			throw new IllegalStateException("Spawn location can't be null");
		}
		this.spawn = spawn;
	}
}
