package net.fabicraft.paper.survival.player;

import net.fabicraft.paper.survival.FabiCraftPaperSurvival;
import net.fabicraft.paper.survival.StorageManager;
import net.fabicraft.paper.survival.util.UUIDUtils;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public final class PlayerDataManager {
	@Language("SQLite")
	private static final String STATEMENT_SELECT = """
			SELECT roleplay_name, roleplay_height
			FROM fcps_playerdata
			WHERE uuid = ?
			""";
	@Language("SQLite")
	private static final String STATEMENT_UPSERT = """
			INSERT INTO fcps_playerdata (uuid, roleplay_name, roleplay_height)
			VALUES (?, ?, ?)
			ON CONFLICT(uuid) DO UPDATE SET
				roleplay_name = excluded.roleplay_name,
				roleplay_height = excluded.roleplay_height
			""";
	private final Map<UUID, PlayerData> data = new ConcurrentHashMap<>();
	private final Executor executor;
	private final StorageManager storageManager;
	private final Logger logger;

	public PlayerDataManager(FabiCraftPaperSurvival plugin) {
		this.executor = plugin.executor();
		this.storageManager = plugin.storageManager();
		this.logger = plugin.getSLF4JLogger();
	}

	public PlayerData data(UUID uuid) {
		return data.get(uuid);
	}

	public void load(UUID uuid) {
		this.executor.execute(() -> {
			try (
					Connection connection = this.storageManager.connection();
					PreparedStatement statement = connection.prepareStatement(STATEMENT_SELECT)
			) {
				statement.setBytes(1, UUIDUtils.uuidToBytes(uuid));
				try (ResultSet set = statement.executeQuery()) {
					PlayerData data = new PlayerData();
					if (!set.next()) {
						this.data.put(uuid, data);
						return;
					}
					data.rolePlayName(set.getString("roleplay_name"));
					data.rolePlayHeight(set.getObject("roleplay_height", Integer.class));
					this.data.put(uuid, data);
				}
			} catch (SQLException exception) {
				this.logger.error("Couldn't load data for UUID {}", uuid, exception);
			}
		});
	}

	public void unload(UUID uuid) {
		this.data.remove(uuid);
	}

	public void save(UUID uuid) {
		this.executor.execute(() -> {
			PlayerData data = this.data.get(uuid);
			if (data == null) {
				throw new IllegalStateException("Player data is null");
			}
			try (
					Connection connection = this.storageManager.connection();
					PreparedStatement statement = connection.prepareStatement(STATEMENT_UPSERT)
			) {
				statement.setBytes(1, UUIDUtils.uuidToBytes(uuid));
				statement.setString(2, data.rolePlayName());
				statement.setObject(3, data.rolePlayHeight());
				statement.executeUpdate();
			} catch (SQLException exception) {
				this.logger.error("Couldn't save data for UUID {}", uuid, exception);
			}
		});
	}
}
