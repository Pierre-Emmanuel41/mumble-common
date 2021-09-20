package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerInfoInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		switch (getHeader().getOid()) {
		case GET:
			// Player connected
			if ((boolean) payload[currentIndex++]) {
				// Player online
				wrapper.putInt(1);

				// Player name
				wrapper.putString((String) payload[currentIndex++], true);

				// Player admin
				wrapper.putInt((boolean) payload[currentIndex] ? 1 : 0);
			} else
				wrapper.putInt(0);

			return wrapper.get();
		case SET:
			// Player name
			wrapper.putString((String) payload[currentIndex++], true);

			// Player connected
			if ((boolean) payload[currentIndex++]) {
				// Player online
				wrapper.putInt(1);

				// Player IP address
				wrapper.putString((String) payload[currentIndex++], true);

				// Player game port number
				wrapper.putInt((int) payload[currentIndex++]);

				// Player admin
				wrapper.putInt((boolean) payload[currentIndex] ? 1 : 0);
			} else
				wrapper.putInt(0);

			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int connected;

		switch (getHeader().getOid()) {
		case GET:
			// Player connected
			connected = wrapper.getInt(first);
			first += 4;
			if (connected == 1) {
				// Player connected
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
		case SET:
			// Player name
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(new String(wrapper.getString(first, playerNameLength)));
			first += playerNameLength;

			// Player connected
			connected = wrapper.getInt(first);
			first += 4;
			if (connected == 1) {
				// Player connected
				informations.add(true);

				// Player IP address
				int addressLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, addressLength));
				first += addressLength;

				// Player game port number
				informations.add(wrapper.getInt(first));
				first += 4;

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
