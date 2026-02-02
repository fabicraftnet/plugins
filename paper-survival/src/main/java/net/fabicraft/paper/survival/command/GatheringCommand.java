package net.fabicraft.paper.survival.command;

import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.gathering.Gathering;
import net.fabicraft.paper.survival.gathering.GatheringManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.MaterialParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.incendo.cloud.parser.standard.StringParser;

import java.util.Locale;
import java.util.StringJoiner;

public final class GatheringCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final String PERMISSION_ADD = "fabicraft.paper.survival.command.gathering.add";
	private static final String PERMISSION_REMOVE = "fabicraft.paper.survival.command.gathering.remove";
	private static final String PERMISSION_LIST = "fabicraft.paper.survival.command.gathering.list";
	private static final TranslatableComponent COMPONENT_ADD_BLOCK_NOT_CONTAINER = Components.translatable(
			"fabicraft.paper.survival.command.gathering.add.block-not-container",
			MessageType.ERROR
	);
	private final GatheringManager gatheringManager;

	public GatheringCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
		this.gatheringManager = plugin.gatheringManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("gathering");
		super.manager.command(builder
				.literal("add")
				.senderType(PlayerSource.class)
				.permission(PERMISSION_ADD)
				.required("name", StringParser.stringParser())
				.required("material", MaterialParser.materialParser())
				.required("goal", IntegerParser.integerParser(1))
				.handler(this::handleAdd)
		);
		super.manager.command(builder
				.literal("remove")
				.permission(PERMISSION_REMOVE)
				.required("name", StringParser.stringParser())
				.handler(this::handleRemove)
		);
		super.manager.command(builder
				.literal("list")
				.permission(PERMISSION_LIST)
				.handler(this::handleList)
		);
	}

	private void handleAdd(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		Block block = player.getTargetBlockExact(10);
		if (block == null || !(block.getState() instanceof Container)) {
			player.sendMessage(COMPONENT_ADD_BLOCK_NOT_CONTAINER);
			return;
		}

		String name = context.get("name");
		Material material = context.get("material");
		int goal = context.get("goal");

		Gathering gathering = new Gathering(name.toLowerCase(Locale.ROOT), block.getLocation(), material, 0, goal);
		this.gatheringManager.add(gathering);

		TranslatableComponent component = Components.translatable(
				"fabicraft.paper.survival.command.gathering.add",
				MessageType.SUCCESS,
				name
		);
		player.sendMessage(component);
	}

	private void handleRemove(CommandContext<Source> context) {
		String name = context.get("name");
		Gathering gathering = this.gatheringManager.gathering(name);
		if (gathering == null) {
			TranslatableComponent component = Components.translatable(
					"fabicraft.paper.survival.command.gathering.remove.unknown",
					MessageType.ERROR,
					name
			);
			context.sender().source().sendMessage(component);
			return;
		}
		this.gatheringManager.remove(gathering);

		TranslatableComponent component = Components.translatable(
				"fabicraft.paper.survival.command.gathering.remove",
				MessageType.SUCCESS,
				name
		);
		context.sender().source().sendMessage(component);
	}

	private void handleList(CommandContext<Source> context) {
		StringJoiner joiner = new StringJoiner(", ");
		this.gatheringManager.gatherings().forEach(gathering -> joiner.add(gathering.identifier()));
		context.sender().source().sendMessage(Component.text(joiner.toString()));
	}
}
