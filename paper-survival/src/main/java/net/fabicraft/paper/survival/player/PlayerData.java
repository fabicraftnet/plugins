package net.fabicraft.paper.survival.player;

public final class PlayerData {
	private String nickname;
	private float scale;

	public String nickname() {
		return this.nickname;
	}

	public void nickname(String nickname) {
		this.nickname = nickname;
	}

	public float scale() {
		return this.scale;
	}

	public void scale(float scale) {
		this.scale = scale;
	}
}
