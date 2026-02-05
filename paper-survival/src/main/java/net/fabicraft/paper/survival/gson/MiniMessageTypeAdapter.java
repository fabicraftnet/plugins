package net.fabicraft.paper.survival.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;

public final class MiniMessageTypeAdapter extends TypeAdapter<Component> {
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

	@Override
	public void write(JsonWriter out, Component value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		out.value(MINI_MESSAGE.serialize(value));
	}

	@Override
	public Component read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return Component.empty();
		}

		return MINI_MESSAGE.deserialize(in.nextString());
	}
}
