package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class ChannelsPlayerInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		switch (getHeader().getOid()) {
		case ADD:
		case REMOVE:
			int currentIndex = 0;
			ByteWrapper wrapper = ByteWrapper.create();

			// Channel name
			wrapper.putString((String) payload[currentIndex++], true);

			// Player name
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

		switch (getHeader().getOid()) {
		case ADD:
		case REMOVE:
			// Channel name
			int channelNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, channelNameLength));
			first += channelNameLength;

			// Player name
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, playerNameLength));
			first += playerNameLength;
			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
