package net.fabicraft.common.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.messaging.MessagingService;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.util.Tristate;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class LuckPermsManager<P> {
	protected final LuckPerms luckPerms;
	protected final Logger logger;

	public LuckPermsManager(Logger logger) {
		this.luckPerms = LuckPermsProvider.get();
		this.logger = logger;
	}

	public boolean addGroup(P player, String groupName) {
		User user = user(player);

		if (!user.data().add(groupNode(groupName)).wasSuccessful()) {
			this.logger.warn("Could not add group '{}' for player {}", groupName, user.getUsername());
			return false;
		}

		this.luckPerms.getUserManager().saveUser(user);
		pushMessagingServiceUpdate();

		return true;
	}

	public void removeGroup(P player, String groupName) {
		User user = user(player);

		if (!user.data().remove(groupNode(groupName)).wasSuccessful()) {
			this.logger.warn("Could not remove group '{}' for player {}", groupName, user.getUsername());
			return;
		}

		this.luckPerms.getUserManager().saveUser(user);
		pushMessagingServiceUpdate();
	}

	public boolean hasGroup(P player, String groupName) {
		User user = user(player);

		Tristate tristate = user.data().contains(groupNode(groupName), NodeEqualityPredicate.ONLY_KEY);
		return tristate == Tristate.TRUE;
	}

	public CompletableFuture<Boolean> hasPermission(UUID uuid, String permission) {
		return this.luckPerms.getUserManager().loadUser(uuid).thenApply(user -> user.getCachedData().getPermissionData().checkPermission(permission).asBoolean());
	}

	private void pushMessagingServiceUpdate() {
		Optional<MessagingService> messagingServiceOptional = this.luckPerms.getMessagingService();
		if (messagingServiceOptional.isEmpty()) {
			this.logger.warn("LuckPerms messaging service is empty!");
			return;
		}

		MessagingService messagingService = messagingServiceOptional.get();
		messagingService.pushUpdate();
	}

	private Node groupNode(String groupName) {
		return Node.builder("group." + groupName.toLowerCase(Locale.ROOT)).build();
	}

	protected abstract User user(P player);
}
