package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerSpeakInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		switch (getHeader().getOid()) {
		case GET:
			return (byte[]) payload[0];
		case SET:
			ByteWrapper wrapper = ByteWrapper.create();
			wrapper.putString((String) payload[0], true);
			wrapper.put((byte[]) payload[1], true);
			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		switch (getHeader().getOid()) {
		case GET:
			return new Object[] { payload };
		case SET:
			ByteWrapper wrapper = ByteWrapper.wrap(payload);
			List<Object> informations = new ArrayList<Object>();
			int first = 0;

			// Player name
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			String playerName = wrapper.getString(first, playerNameLength);
			informations.add(playerName);
			first += playerNameLength;

			// Player data
			int playerDataLength = wrapper.getInt(first);
			first += 4;
			byte[] playerData = wrapper.extract(first, playerDataLength);
			informations.add(playerData);
			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
