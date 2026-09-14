package net.fabicraft.paper.survival.gui;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class Gui {
	protected final ChestGui gui;

	public Gui(int rows, Component title) {
		this.gui = new ChestGui(rows, ComponentHolder.of(title));
	}

	public void open(Player player) {
		this.gui.show(player);
	}
}
