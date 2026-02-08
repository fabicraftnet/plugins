package net.fabicraft.paper.survival.gathering;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public final class Gathering {
	private final String identifier;
	private final BlockPosition containerPosition;
	private final Material material;
	private int goal;
	private int collected;
	private Component displayName;
	private transient InventoryHolder inventoryHolder;

	public Gathering(String identifier, BlockPosition containerPosition, Material material, int collected, int goal) {
		this.identifier = identifier;
		this.containerPosition = containerPosition;
		this.material = material;
		this.collected = collected;
		this.goal = goal;
	}

	public boolean add(int amount) {
		this.collected = Math.min(this.collected + amount, this.goal);
		return isCompleted();
	}

	public String identifier() {
		return identifier;
	}

	public BlockPosition containerPosition() {
		return containerPosition;
	}

	public Material material() {
		return material;
	}

	public int collected() {
		return collected;
	}

	public int goal() {
		return goal;
	}

	public boolean isCompleted() {
		return this.collected >= this.goal;
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

	public void goal(int goal) {
		this.goal = goal;
	}
}
