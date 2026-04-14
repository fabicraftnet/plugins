package net.fabicraft.paper.survival.player;

public final class PlayerData {
	private String rolePlayName;
	private Integer rolePlayHeight;

	public String rolePlayName() {
		return this.rolePlayName;
	}

	public void rolePlayName(String name) {
		this.rolePlayName = name;
	}

	public Integer rolePlayHeight() {
		return this.rolePlayHeight;
	}

	public void rolePlayHeight(Integer height) {
		this.rolePlayHeight = height;
	}
}
