package net.fabicraft.paper.survival.afk;

public final class AfkStatus {
	private long afkSince;
	private AfkState state = AfkState.NOT_AFK;

	public AfkStatus() {
		this.afkSince = System.nanoTime();
	}

	public void markAsActive() {
		this.afkSince = System.nanoTime();
		this.state = AfkState.NOT_AFK;
	}

	public long hasBeenAfkFor() {
		return System.nanoTime() - this.afkSince;
	}

	public AfkState state() {
		return this.state;
	}

	public void state(AfkState state) {
		this.state = state;
	}
}
