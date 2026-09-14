package net.fabicraft.paper.survival.arena;

import org.bukkit.Material;

import java.util.Locale;

public abstract class Arena {
	private final String identifier;
	private String name;
	private Material material = Material.GRASS_BLOCK;
	//TODO Region

	public Arena(String identifier) {
		this.identifier = identifier.toLowerCase(Locale.ROOT);
	}

	public String name() {
		return this.name;
	}

	public void name(String name) {
		this.name = name;
	}

	public Material material() {
		return this.material;
	}

	public void material(Material material) {
		if (material == null) {
			throw new IllegalStateException("Material can't be null");
		}
		this.material = material;
	}
}
