package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerSpeakInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		ByteWrapper wrapper = ByteWrapper.create();
		int currentIndex = 0;
		switch (getHeader().getOid()) {
		case GET:
			// Player's name
			wrapper.putString((String) payload[currentIndex++], true);

			// Player data
			wrapper.put((byte[]) payload[currentIndex++], true);

			return wrapper.get();
		case SET:
			// Player's name
			wrapper.putString((String) payload[currentIndex++], true);

			// Player data
			wrapper.put((byte[]) payload[currentIndex++], true);

			// Global volume
			wrapper.putDouble((double) payload[currentIndex++]);

			// Left volume
			wrapper.putDouble((double) payload[currentIndex++]);

			// Right volume
			wrapper.putDouble((double) payload[currentIndex++]);

			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int first = 0, playerNameLength, playerDataLength;

		switch (getHeader().getOid()) {
		case GET:
			// Player name
			playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, playerNameLength));
			first += playerNameLength;

			// Player data
			playerDataLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.extract(first, playerDataLength));
			first += playerDataLength;

			return informations.toArray();
		case SET:
			// Player name
			playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, playerNameLength));
			first += playerNameLength;

			// Player data
			playerDataLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.extract(first, playerDataLength));
			first += playerDataLength;

			// Global volume
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Left volume
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Right volume
			informations.add(wrapper.getDouble(first));

			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
