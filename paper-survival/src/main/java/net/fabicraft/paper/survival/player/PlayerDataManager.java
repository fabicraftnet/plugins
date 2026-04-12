package net.fabicraft.paper.survival.player;

public final class PlayerDataManager {
	private static final String STATEMENT_SELECT_BY_UUID = """
			SELECT nickname, scale
			FROM fcps_playerdata
			WHERE uuid = ?
			""";

	private static final String STATEMENT_UPSERT = """
			INSERT INTO fcps_playerdata (uuid, nickname, scale)
			VALUES (?, ?, ?)
			ON DUPLICATE KEY UPDATE
				nickname = VALUES(nickname),
				scale = VALUES(scale)
			""";
}
