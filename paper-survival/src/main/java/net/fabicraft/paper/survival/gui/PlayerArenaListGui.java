package net.fabicraft.paper.survival.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public final class PlayerArenaListGui extends Gui {
	public PlayerArenaListGui(FabiCraftPaperSurvival plugin) {
		super(6, Component.translatable("fabicraft.paper.survival.gui.arena.list.player.title"));

		PaginatedPane paginatedPane = new PaginatedPane(9, 5);
		paginatedPane.populateWithGuiItems(plugin.arenaManager().playerArenas().stream()
				.map(arena -> new GuiItem(
						new ItemStack(arena.material()),
						event -> event.getWhoClicked().teleport(arena.spawn()))
				)
				.toList());
		this.gui.addPane(Slot.fromXY(0, 0), paginatedPane);
		this.gui.addPane(Slot.fromXY(5, 5), new PagingButtons(9, paginatedPane));
	}
}
