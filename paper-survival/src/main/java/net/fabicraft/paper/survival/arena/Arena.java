package net.fabicraft.paper.survival.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class Arena {
	private final List<Player> players = new ArrayList<>();
	private final String identifier;
	private final List<Location> spawnLocations = new ArrayList<>();
	private final List<UUID> builders = new ArrayList<>();
	private String name;
	private Material material = Material.GRASS_BLOCK;
	private Location lobbyLocation;
	private String region;

	public Arena(String identifier) {
		this.identifier = identifier.toLowerCase(Locale.ROOT);
	}

	public String name() {
		return this.name == null ? this.identifier : this.name;
	}

	public void name(String name) {
		this.name = name;
	}

	public Material material() {
		return this.material;
	}

	public void material(Material material) {
		this.material = material;
	}

	public void addSpawnLocation(Location location) {
		this.spawnLocations.add(location);
	}

	public void clearSpawnLocations() {
		this.spawnLocations.clear();
	}

	public void add(Player player) {

	}

	public void remove(Player player) {

	}

	public Location lobbyLocation() {
		return this.lobbyLocation;
	}
}
