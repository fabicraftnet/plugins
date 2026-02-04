package net.fabicraft.paper.survival.command.commands;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.bukkit.data.Selector;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import java.io.IOException;
import java.util.Collection;

import static net.fabicraft.paper.survival.command.parser.CustomItemParser.customItemParser;
import static org.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser.multiplePlayerSelectorParser;

public final class FabiCraftSurvivalCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final TranslatableComponent COMPONENT_SUCCESS = Components.translatable(
			"fabicraft.paper.core.command.fabicraftsurvival.reload.success",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_FAILURE = Components.translatable(
			"fabicraft.paper.core.command.fabicraftsurvival.reload.failure",
			MessageType.ERROR
	);
	private static final String PERMISSION_ITEM = "fabicraft.paper.survival.command.fabicraftsurvival.item";
	private static final String PERMISSION_RELOAD = "fabicraft.paper.core.command.fabicraftsurvival.reload";

	public FabiCraftSurvivalCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
	}

	@Override
	public void register() {
		var builder = this.manager.commandBuilder("fabicraftsurvival");

		var itemBuilder = builder.literal("item").permission(PERMISSION_ITEM).required("item", customItemParser());
		super.manager.command(itemBuilder.senderType(PlayerSource.class).handler(this::handleItem));
		super.manager.command(itemBuilder.required("target", multiplePlayerSelectorParser(false)).handler(this::handleItemTarget));

		super.manager.command(builder.literal("reload").permission(PERMISSION_RELOAD).handler(this::handleReload));
	}

	private void handleItem(CommandContext<PlayerSource> ctx) {
		ItemStack itemStack = ctx.get("item");
		ctx.sender().source().getInventory().addItem(itemStack);
	}

	private void handleItemTarget(CommandContext<Source> ctx) {
		ItemStack itemStack = ctx.get("item");
		Selector<Player> playerSelector = ctx.get("target");
		Collection<Player> players = playerSelector.values();
		players.forEach(player -> player.getInventory().addItem(itemStack));

		Component message;
		if (players.size() > 1) {
			message = Components.translatable(
					"fabicraft.paper.survival.command.fabicraft.item.multiple",
					MessageType.INFO,
					itemStack.effectiveName(),
					Components.playerCount(players)
			);
		} else {
			Player player = players.iterator().next();
			message = Components.translatable(
					"fabicraft.paper.survival.command.fabicraft.item.single",
					MessageType.INFO,
					itemStack.effectiveName(),
					Components.player(player)
			);
		}

		ctx.sender().source().sendMessage(message);
	}

	private void handleReload(CommandContext<Source> context) {
		try {
			super.plugin.load();
		} catch (IOException e) {
			context.sender().source().sendMessage(COMPONENT_FAILURE);
			return;
		}

		context.sender().source().sendMessage(COMPONENT_SUCCESS);
	}
}
