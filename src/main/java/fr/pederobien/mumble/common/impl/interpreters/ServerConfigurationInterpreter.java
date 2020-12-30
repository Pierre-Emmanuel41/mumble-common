package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class ServerConfigurationInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;

		// number of channels
		int numberOfChannels = (int) payload[currentIndex++];
		ByteWrapper wrapper = ByteWrapper.create().putInt(numberOfChannels);

		for (int i = 0; i < numberOfChannels; i++) {
			// Channel name
			wrapper.putString((String) payload[currentIndex++], true);

			// number of player in channel
			int numberOfPlayers = (int) payload[currentIndex++];
			wrapper.putInt(numberOfPlayers);

			for (int j = 0; j < numberOfPlayers; j++)
				// Player name
				wrapper.putString((String) payload[currentIndex++], true);
		}

		return wrapper.get();
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int numberOfChannels = wrapper.getInt(first);
		informations.add(numberOfChannels);
		first += 4;

		for (int i = 0; i < numberOfChannels; i++) {
			int channelNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, channelNameLength));
			first += channelNameLength;

			int numberOfPlayers = wrapper.getInt(first);
			informations.add(numberOfPlayers);
			first += 4;

			for (int j = 0; j < numberOfPlayers; j++) {
				int playerNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, playerNameLength));
				first += playerNameLength;
			}
		}
		return informations.toArray();
	}

}
