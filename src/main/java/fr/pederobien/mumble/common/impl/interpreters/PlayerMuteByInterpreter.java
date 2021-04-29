package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerMuteByInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		// Player name
		wrapper.putString((String) payload[currentIndex++], true);

		// Player muted/unmuted name
		wrapper.putString((String) payload[currentIndex++], true);

		// Player mute or unmute
		wrapper.put((byte) ((boolean) payload[currentIndex] ? 1 : 0));

		return wrapper.get();
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();

		// Player Name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		String playerName = wrapper.getString(first, playerNameLength);
		informations.add(playerName);
		first += playerNameLength;

		// Player muted/unmuted name
		int playerMutedOrUnmutedNameLength = wrapper.getInt(first);
		first += 4;
		String playerMutedOrUnmutedName = wrapper.getString(first, playerMutedOrUnmutedNameLength);
		informations.add(playerMutedOrUnmutedName);
		first += playerMutedOrUnmutedNameLength;

		// Player mute or unmute
		informations.add(wrapper.get(first) == 1 ? true : false);
		return informations.toArray();
	}
}
