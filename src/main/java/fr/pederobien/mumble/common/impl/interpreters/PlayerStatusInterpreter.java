package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerStatusInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		// Player connected
		if ((boolean) payload[currentIndex++]) {
			wrapper.put((byte) 1);
			wrapper.putString((String) payload[currentIndex++], true);

			// Player admin
			wrapper.put((byte) ((boolean) payload[currentIndex] ? 1 : 0));
		} else
			wrapper.put((byte) 0);

		return wrapper.get();
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();

		// Player connected
		byte connected = wrapper.get(first);
		first += 1;
		if (connected == 1) {
			informations.add(true);
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(new String(wrapper.getString(first, playerNameLength)));
			first += playerNameLength;

			// Player admin
			informations.add(wrapper.get(first) == 1 ? true : false);
		} else
			informations.add(false);

		return informations.toArray();
	}
}
