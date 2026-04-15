package net.fabicraft.paper.survival.command.commands;

import net.draycia.carbon.api.CarbonChat;
import net.draycia.carbon.api.CarbonChatProvider;
import net.fabicraft.common.locale.Components;
import net.fabicraft.common.locale.MessageType;
import net.fabicraft.paper.common.command.PaperCommand;
import net.fabicraft.paper.common.luckperms.PaperLuckPermsManager;
import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.player.PlayerData;
import net.fabicraft.paper.survival.player.PlayerDataManager;
import net.fabicraft.paper.survival.player.PlayerHeightController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.parser.standard.IntegerParser;
import org.incendo.cloud.parser.standard.StringParser;

public final class RolePlayCommand extends PaperCommand<FabiCraftPaperSurvival> {
	public static final TranslatableComponent COMPONENT_ADD = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.add",
			MessageType.SUCCESS,
			"/rpchat"
	);
	public static final TranslatableComponent COMPONENT_REMOVE = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.remove",
			MessageType.SUCCESS
	);
	private static final String PERMISSION_BASE = "fabicraft.paper.survival.command.roleplay";
	private static final String PERMISSION_NAME = "fabicraft.paper.survival.command.roleplay.name";
	private static final String PERMISSION_HEIGHT = "fabicraft.paper.survival.command.roleplay.height";
	private static final String GROUP_NAME = "roleplay";
	private static final TranslatableComponent COMPONENT_NAME_UNSET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.unset",
			MessageType.INFO
	);
	private static final TranslatableComponent COMPONENT_NAME_RESET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.reset",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_NAME_RESET_UNSET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.unset",
			MessageType.ERROR
	);
	private static final TranslatableComponent COMPONENT_HEIGHT_RESET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.height.reset",
			MessageType.SUCCESS
	);
	private final PaperLuckPermsManager luckPermsManager;
	private final CarbonChat carbon = CarbonChatProvider.carbonChat();
	private final PlayerDataManager playerDataManager;
	private final PlayerHeightController playerHeightController = new PlayerHeightController();

	public RolePlayCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
		this.luckPermsManager = plugin.luckPermsManager();
		this.playerDataManager = plugin.playerDataManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("roleplay", "rp")
				.permission(PERMISSION_BASE)
				.senderType(PlayerSource.class)
				.handler(this::handle);
		super.manager.command(builder);

		var nameBuilder = builder.literal("name").permission(PERMISSION_NAME);
		super.manager.command(nameBuilder.handler(this::handleName));
		super.manager.command(nameBuilder.literal("reset").handler(this::handleNameReset));
		super.manager.command(nameBuilder.literal("set").required("name", StringParser.greedyStringParser()).handler(this::handleNameSet));

		var heightBuilder = builder.literal("height").permission(PERMISSION_HEIGHT);
		super.manager.command(heightBuilder.handler(this::handleHeight));
		super.manager.command(heightBuilder.literal("set").required("height", IntegerParser.integerParser(150, 200)).handler(this::handleHeightSet));
		super.manager.command(heightBuilder.literal("reset").handler(this::handleHeightReset));
	}

	private void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		if (this.luckPermsManager.hasGroup(player, GROUP_NAME)) {
			this.luckPermsManager.removeGroup(player, GROUP_NAME);

			this.carbon.userManager().user(player.getUniqueId())
					.thenAccept(user -> user.selectedChannel(this.carbon.channelRegistry().defaultChannel()));

			this.playerHeightController.reset(player);

			player.sendMessage(COMPONENT_REMOVE);
		} else {
			this.luckPermsManager.addGroup(player, GROUP_NAME);
			PlayerData data = this.playerDataManager.data(player);
			if (data == null) {
				throw new IllegalStateException("Player data is null");
			}
			this.playerHeightController.set(player, data.rolePlayHeight());
			player.sendMessage(COMPONENT_ADD);
		}
	}

	private void handleName(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		Component component = data.rolePlayName() == null ? COMPONENT_NAME_UNSET : Components.translatable(
				"fabicraft.paper.survival.command.roleplay.name",
				MessageType.INFO,
				data.rolePlayName()
		);
		player.sendMessage(component);
	}

	private void handleNameReset(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		Component component;
		if (data.rolePlayName() != null) {
			component = COMPONENT_NAME_RESET;
			data.rolePlayName(null);
			this.playerDataManager.save(player.getUniqueId());
		} else {
			component = COMPONENT_NAME_RESET_UNSET;
		}
		player.sendMessage(component);
	}

	private void handleNameSet(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		String nickname = context.get("nickname");
		data.rolePlayName(nickname);
		this.playerDataManager.save(player.getUniqueId());
		player.sendMessage(Components.translatable(
				"fabicraft.paper.survival.command.roleplay.name.set",
				MessageType.SUCCESS,
				nickname
		));
	}

	private void handleHeight(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		player.sendMessage(Components.translatable(
				"fabicraft.paper.survival.command.roleplay.height",
				MessageType.INFO,
				data.rolePlayHeight()
		));
	}

	private void handleHeightSet(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		int height = context.get("height");
		data.rolePlayHeight(height);
		this.playerDataManager.save(player.getUniqueId());
		this.playerHeightController.set(player, height);
		player.sendMessage(Components.translatable(
				"fabicraft.paper.survival.command.roleplay.height.set",
				MessageType.SUCCESS,
				height
		));
	}

	private void handleHeightReset(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player);
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		data.rolePlayHeight(null);
		this.playerDataManager.save(player.getUniqueId());
		player.sendMessage(COMPONENT_HEIGHT_RESET);
	}
}
