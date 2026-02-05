package net.fabicraft.paper.survival.gathering;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public final class Gathering {
	private final String identifier;
	private final BlockPosition containerPosition;
	private final Material gatherableMaterial;
	private final int gatheredGoal;
	private int gatheredAmount;
	private Component displayName;
	private transient InventoryHolder inventoryHolder;

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

	public Material material() {
		return gatherableMaterial;
	}

	public int collected() {
		return gatheredAmount;
	}

	public int goal() {
		return gatheredGoal;
	}

	public boolean isCompleted() {
		return this.gatheredAmount >= this.gatheredGoal;
	}

	public void displayName(Component displayName) {
		this.displayName = displayName;
	}

	public Component displayName() {
		return this.displayName == null ? Component.text(this.identifier) : this.displayName;
	}

	public InventoryHolder inventoryHolder() {
		if (this.inventoryHolder == null) {
			this.inventoryHolder = new GatheringInventoryHolder(this);
		}
		return this.inventoryHolder;
	}
}
