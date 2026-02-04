package net.fabicraft.paper.survival.gathering;

import io.papermc.paper.math.BlockPosition;
import org.bukkit.Material;

public final class Gathering {
	private final String identifier;
	private final BlockPosition containerPosition;
	private final Material gatherableMaterial;
	private final int gatheredGoal;
	private int gatheredAmount;

	public Gathering(String identifier, BlockPosition containerPosition, Material gatherableMaterial, int gatheredAmount, int gatheredGoal) {
		this.identifier = identifier;
		this.containerPosition = containerPosition;
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

	public BlockPosition containerPosition() {
		return containerPosition;
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
