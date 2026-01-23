package net.fabicraft.paper.luckperms;

import net.fabicraft.common.luckperms.LuckPermsManager;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

public final class PaperLuckPermsManager extends LuckPermsManager<Player> {
	public PaperLuckPermsManager(Logger logger) {
		super(logger);
	}

	@Override
	protected User user(Player player) {
		return super.luckPerms.getPlayerAdapter(Player.class).getUser(player);
	}
}
