package net.fabicraft.velocity.config.liaison;

import com.velocitypowered.api.network.ProtocolVersion;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.backend.Printable;
import space.arim.dazzleconf.engine.DeserializeInput;
import space.arim.dazzleconf.engine.SerializeDeserialize;
import space.arim.dazzleconf.engine.SerializeOutput;
import space.arim.dazzleconf.engine.TypeLiaison;
import space.arim.dazzleconf.reflect.TypeToken;

public final class ProtocolVersionLiaison implements TypeLiaison {
	@Override
	public @Nullable <V> Agent<V> makeAgent(@NonNull TypeToken<V> typeToken, @NonNull Handshake handshake) {
		return Agent.matchOnToken(typeToken, ProtocolVersion.class, ProtocolVersionAgent::new);
	}

	private static class ProtocolVersionAgent implements Agent<ProtocolVersion> {
		@Override
		public @NonNull SerializeDeserialize<ProtocolVersion> makeSerializer() {
			return new SerializeDeserialize<>() {

				@Override
				public @NonNull LoadResult<ProtocolVersion> deserialize(@NonNull DeserializeInput deser) {
					if (!(deser.object() instanceof Number number)) {
						return LoadResult.failure(deser.buildError(Printable.preBuilt("expected a number")));
					}
					return LoadResult.of(ProtocolVersion.getProtocolVersion(number.intValue()));
				}

				@Override
				public void serialize(@NotNull ProtocolVersion value, @NonNull SerializeOutput ser) {
					ser.outInt(value.getProtocol());
				}
			};
		}
	}
}
