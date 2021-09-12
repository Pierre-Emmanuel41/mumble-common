package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.utils.ByteWrapper;

public class ServerJoinInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		switch (getHeader().getOid()) {
		case SET:
			int currentIndex = 0;
			ByteWrapper wrapper = ByteWrapper.create();

			// UDP port number
			wrapper.putInt((int) payload[currentIndex++]);

			// Number of channels
			int numberOfChannels = (int) payload[currentIndex++];
			wrapper.putInt(numberOfChannels);

			for (int i = 0; i < numberOfChannels; i++) {
				// Channel's name
				wrapper.putString((String) payload[currentIndex++], true);

				// Channel's sound modifier name
				wrapper.putString((String) payload[currentIndex++], true);

				// Number of players
				int numberOfPlayers = (int) payload[currentIndex++];
				wrapper.putInt(numberOfPlayers);

				for (int j = 0; j < numberOfPlayers; j++) {
					// Player's name
					wrapper.putString((String) payload[currentIndex++], true);

					// Player's mute status
					wrapper.putInt(((boolean) payload[currentIndex++] ? 1 : 0));

					// Player's deafen status
					wrapper.putInt(((boolean) payload[currentIndex++] ? 1 : 0));
				}
			}

			// Number of sound modifier
			int numberOfModifiers = (int) payload[currentIndex++];
			wrapper.putInt(numberOfModifiers);

			for (int i = 0; i < numberOfModifiers; i++)
				// Modifier's name
				wrapper.putString((String) payload[currentIndex++], true);

			// Player identifier
			wrapper.putString(payload[currentIndex++].toString(), true);

			boolean playerConnected = (boolean) payload[currentIndex++];
			if (playerConnected) {
				// Player online
				wrapper.putInt(1);

				// Player name
				wrapper.putString((String) payload[currentIndex++], true);

				// Player admin
				wrapper.putInt((boolean) payload[currentIndex++] ? 1 : 0);
			} else
				// Player not connected
				wrapper.putInt(0);

			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		switch (getHeader().getOid()) {
		case SET:
			ByteWrapper wrapper = ByteWrapper.wrap(payload);
			List<Object> informations = new ArrayList<Object>();
			int first = 0;

			// UDP port number
			informations.add(wrapper.getInt(first));
			first += 4;

			// Number of channels
			int numberOfChannels = wrapper.getInt(first);
			informations.add(numberOfChannels);
			first += 4;

			for (int i = 0; i < numberOfChannels; i++) {
				// Channel's name
				int channelNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, channelNameLength));
				first += channelNameLength;

				// Channel's sound modifier name
				int soundModifierNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, soundModifierNameLength));
				first += soundModifierNameLength;

				// Number of players
				int numberOfPlayers = wrapper.getInt(first);
				informations.add(numberOfPlayers);
				first += 4;

				for (int j = 0; j < numberOfPlayers; j++) {
					// Player's name
					int playerNameLength = wrapper.getInt(first);
					first += 4;
					informations.add(wrapper.getString(first, playerNameLength));
					first += playerNameLength;

					// Player's mute status
					informations.add(wrapper.getInt(first) == 1);
					first += 4;

					// Player's deafen status
					informations.add(wrapper.getInt(first) == 1);
					first += 4;
				}
			}

			// Number of modifiers
			int numberOfModifiers = wrapper.getInt(first);
			informations.add(numberOfModifiers);
			first += 4;

			// Modifier name
			for (int i = 0; i < numberOfModifiers; i++) {
				int modifierNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, modifierNameLength));
				first += modifierNameLength;
			}

			// Player identifier
			int identifierLength = wrapper.getInt(first);
			first += 4;
			informations.add(UUID.fromString(wrapper.getString(first, identifierLength)));
			first += identifierLength;

			boolean playerConnected = wrapper.getInt(first) == 1;
			first += 4;
			if (playerConnected) {
				// Player online
				informations.add(true);

				// Player name
				int playerNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(new String(wrapper.getString(first, playerNameLength)));
				first += playerNameLength;

				// Player admin
				informations.add(wrapper.getInt(first) == 1 ? true : false);
			} else
				informations.add(false);

			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
