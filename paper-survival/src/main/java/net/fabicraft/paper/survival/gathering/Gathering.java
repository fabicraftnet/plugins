package net.fabicraft.paper.survival.gathering;

import org.bukkit.Location;
import org.bukkit.Material;

public final class Gathering {
	private final String identifier;
	private final Location containerLocation;
	private final Material gatherableMaterial;
	private final int gatheredGoal;
	private int gatheredAmount;

	public Gathering(String identifier, Location containerLocation, Material gatherableMaterial, int gatheredAmount, int gatheredGoal) {
		this.identifier = identifier;
		this.containerLocation = containerLocation;
		this.gatherableMaterial = gatherableMaterial;
		this.gatheredAmount = gatheredAmount;
		this.gatheredGoal = gatheredGoal;
	}

	public boolean add(int amount) {
		this.gatheredAmount = Math.min(this.gatheredAmount + amount, this.gatheredGoal);
		return isCompleted();
	}

	public String identifier() {
		return identifier;
	}

	public Location containerLocation() {
		return containerLocation;
	}

	public Material gatherableMaterial() {
		return gatherableMaterial;
	}

	public int gatheredAmount() {
		return gatheredAmount;
	}

	public int gatheredGoal() {
		return gatheredGoal;
	}

	public boolean isCompleted() {
		return this.gatheredAmount >= this.gatheredGoal;
	}
}
