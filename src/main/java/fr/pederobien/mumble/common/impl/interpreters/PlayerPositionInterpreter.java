package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerPositionInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		switch (getHeader().getOid()) {
		case GET:
			// Player name
			wrapper.putString((String) payload[currentIndex++], true);

			if (payload.length > 1) {
				// X position
				wrapper.putDouble((double) payload[currentIndex++]);

				// Y position
				wrapper.putDouble((double) payload[currentIndex++]);

				// Z position
				wrapper.putDouble((double) payload[currentIndex++]);

				// Yaw position
				wrapper.putDouble((double) payload[currentIndex++]);

				// Pitch position
				wrapper.putDouble((double) payload[currentIndex++]);
			}

			return wrapper.get();
		case SET:
			// Player name
			wrapper.putString((String) payload[currentIndex++], true);

			// X position
			wrapper.putDouble((double) payload[currentIndex++]);

			// Y position
			wrapper.putDouble((double) payload[currentIndex++]);

			// Z position
			wrapper.putDouble((double) payload[currentIndex++]);

			// Yaw position
			wrapper.putDouble((double) payload[currentIndex++]);

			// Pitch position
			wrapper.putDouble((double) payload[currentIndex++]);

			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int playerNameLength;

		switch (getHeader().getOid()) {
		case GET:
			// Player name
			playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, playerNameLength));
			first += playerNameLength;

			if (first < payload.length) {
				// X position
				wrapper.getDouble(first);
				first += 8;

				// Y position
				wrapper.getDouble(first);
				first += 8;

				// Z position
				wrapper.getDouble(first);
				first += 8;

				// Yaw position
				wrapper.getDouble(first);

				// Pitch position
				wrapper.getDouble(first);
			}

			return informations.toArray();
		case SET:
			// Player name
			playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, playerNameLength));
			first += playerNameLength;

			// X position
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Y position
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Z position
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Yaw position
			informations.add(wrapper.getDouble(first));
			first += 8;

			// Pitch position
			informations.add(wrapper.getDouble(first));

			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
