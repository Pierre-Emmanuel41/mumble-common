package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class SoundModifierInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		switch (getHeader().getOid()) {
		case GET:
			// Channel name
			wrapper.putString((String) payload[currentIndex++], true);

			// Get the sound modifier name for a channel
			if (payload.length > 1)
				wrapper.putString((String) payload[currentIndex], true).get();

			return wrapper.get();
		case SET:
			// Channel name
			wrapper.putString((String) payload[currentIndex++], true);

			// Modifier name
			wrapper.putString((String) payload[currentIndex], true);
			return wrapper.get();
		case INFO:
			// Get all sound modifier registered on the server
			int numberOfModifiers = (int) payload[currentIndex++];
			wrapper.putInt(numberOfModifiers);

			for (int i = 0; i < numberOfModifiers; i++)
				// Modifier's name
				wrapper.putString((String) payload[currentIndex++], true);

			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int first = 0;

		int channelNameLength, modifierNameLength;
		String channelName, modifierName;
		switch (getHeader().getOid()) {
		case GET:
			// Channel name
			channelNameLength = wrapper.getInt(first);
			first += 4;
			channelName = wrapper.getString(first, channelNameLength);
			informations.add(channelName);
			first += channelNameLength;

			// Modifier name
			if (first < payload.length) {
				modifierNameLength = wrapper.getInt(first);
				first += 4;
				modifierName = wrapper.getString(first, modifierNameLength);
				first += modifierNameLength;
				informations.add(modifierName);
			}
			return informations.toArray();

		case SET:
			// Channel name
			channelNameLength = wrapper.getInt(first);
			first += 4;
			channelName = wrapper.getString(first, channelNameLength);
			informations.add(channelName);
			first += channelNameLength;

			// Modifier name
			modifierNameLength = wrapper.getInt(first);
			first += 4;
			modifierName = wrapper.getString(first, modifierNameLength);
			informations.add(modifierName);
			first += modifierNameLength;

			return informations.toArray();

		case INFO:
			// Number of modifiers
			int numberOfModifiers = wrapper.getInt(first);
			informations.add(numberOfModifiers);
			first += 4;

			// Modifier name
			for (int i = 0; i < numberOfModifiers; i++) {
				modifierNameLength = wrapper.getInt(first);
				first += 4;
				modifierName = wrapper.getString(first, modifierNameLength);
				informations.add(modifierName);
				first += modifierNameLength;
			}

			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
