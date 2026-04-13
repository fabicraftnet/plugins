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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.entity.Player;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.parser.standard.StringParser;

public final class RolePlayCommand extends PaperCommand<FabiCraftPaperSurvival> {
	private static final String PERMISSION = "fabicraft.paper.survival.command.roleplay";
	private static final String GROUP_NAME = "roleplay";
	private static final TranslatableComponent COMPONENT_ADD = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.add",
			MessageType.SUCCESS,
			"/rpchat",
			"/nickname"
	);
	private static final TranslatableComponent COMPONENT_REMOVE = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.remove",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_NAME_UNSET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.unset",
			MessageType.INFO
	);
	private static final TranslatableComponent COMPONENT_NAME_CLEAR = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.clear",
			MessageType.SUCCESS
	);
	private static final TranslatableComponent COMPONENT_NAME_CLEAR_UNSET = Components.translatable(
			"fabicraft.paper.survival.command.roleplay.name.unset",
			MessageType.ERROR
	);
	private final PaperLuckPermsManager luckPermsManager;
	private final CarbonChat carbon = CarbonChatProvider.carbonChat();
	private final PlayerDataManager playerDataManager;

	public RolePlayCommand(FabiCraftPaperSurvival plugin) {
		super(plugin, plugin.commandManager());
		this.luckPermsManager = plugin.luckPermsManager();
		this.playerDataManager = plugin.playerDataManager();
	}

	@Override
	public void register() {
		var builder = super.manager.commandBuilder("roleplay", "rp")
				.permission(PERMISSION)
				.senderType(PlayerSource.class)
				.handler(this::handle);
		super.manager.command(builder);

		var nameBuilder = builder.literal("name");
		super.manager.command(nameBuilder.handler(this::handleName));
		super.manager.command(nameBuilder.literal("clear").handler(this::handleNameClear));
		super.manager.command(nameBuilder.literal("set").required("name", StringParser.greedyStringParser()).handler(this::handleNameSet));
		super.manager.command(builder.literal("scale").handler(this::handleScale));
	}

	private void handle(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		if (this.luckPermsManager.hasGroup(player, GROUP_NAME)) {
			this.luckPermsManager.removeGroup(player, GROUP_NAME);

			this.carbon.userManager().user(player.getUniqueId())
					.thenAccept(user -> user.selectedChannel(this.carbon.channelRegistry().defaultChannel()));


			player.sendMessage(COMPONENT_REMOVE);
		} else {
			this.luckPermsManager.addGroup(player, GROUP_NAME);
			player.sendMessage(COMPONENT_ADD);
		}
	}

	private void handleName(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player.getUniqueId());
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		Component component = data.nickname() == null ? COMPONENT_NAME_UNSET : Components.translatable(
				"fabicraft.paper.survival.command.roleplay.name",
				MessageType.INFO,
				data.nickname()
		);
		player.sendMessage(component);
	}

	private void handleNameClear(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player.getUniqueId());
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		Component component = data.nickname() == null ? COMPONENT_NAME_CLEAR_UNSET : COMPONENT_NAME_CLEAR;
		player.sendMessage(component);
	}

	private void handleNameSet(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
		PlayerData data = this.playerDataManager.data(player.getUniqueId());
		if (data == null) {
			throw new IllegalStateException("Player data is null");
		}
		String nickname = context.get("nickname");
		data.nickname(nickname);
		this.playerDataManager.save(player.getUniqueId());
		player.sendMessage(Components.translatable(
				"fabicraft.paper.survival.command.roleplay.name.set",
				MessageType.SUCCESS,
				nickname
		));
	}

	private void handleScale(CommandContext<PlayerSource> context) {
		Player player = context.sender().source();
	}
}
