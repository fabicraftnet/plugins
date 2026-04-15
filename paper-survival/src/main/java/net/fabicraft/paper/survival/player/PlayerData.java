package net.fabicraft.paper.survival.player;

public final class PlayerData {
	private String characterName;
	private Integer characterHeight;

	public String characterName() {
		return this.characterName;
	}

	public void characterName(String name) {
		this.characterName = name;
	}

	public Integer characterHeight() {
		return this.characterHeight;
	}

	public void characterHeight(Integer height) {
		this.characterHeight = height;
	}
}
