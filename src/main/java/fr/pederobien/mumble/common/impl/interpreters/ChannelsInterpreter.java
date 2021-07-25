package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class ChannelsInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		switch (getHeader().getOid()) {
		case GET:
			int currentIndex = 0;
			ByteWrapper wrapper = ByteWrapper.create();

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
			return wrapper.get();
		case ADD:
		case REMOVE:
			return ByteWrapper.create().putString((String) payload[0]).get();
		case SET:
			return ByteWrapper.create().putString((String) payload[0], true).putString((String) payload[1], true).get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int first = 0;

		switch (getHeader().getOid()) {
		case GET:
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
			return informations.toArray();

		case ADD:
		case REMOVE:
			return Arrays.asList(wrapper.getString()).toArray();
		case SET:
			int oldChannelNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, oldChannelNameLength));
			first += oldChannelNameLength;

			int newChannelNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, newChannelNameLength));
			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
