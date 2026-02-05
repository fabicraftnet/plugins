package net.fabicraft.paper.survival.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;

import java.io.IOException;

public final class BlockPositionTypeAdapter extends TypeAdapter<BlockPosition> {
	@Override
	public void write(JsonWriter out, BlockPosition value) throws IOException {
		out.beginObject();
		out.name("x").value(value.blockX());
		out.name("y").value(value.blockY());
		out.name("z").value(value.blockZ());
		out.endObject();
	}

	@Override
	public BlockPosition read(JsonReader in) throws IOException {
		int x = 0;
		int y = 0;
		int z = 0;

		in.beginObject();
		while (in.hasNext()) {
			String field = in.nextName();

			switch (field) {
				case "x" -> x = in.nextInt();
				case "y" -> y = in.nextInt();
				case "z" -> z = in.nextInt();
				default -> in.skipValue();
			}
		}
		in.endObject();

		return Position.block(x, y, z);
	}
}
