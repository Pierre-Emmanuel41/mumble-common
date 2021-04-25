package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerDeafenInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		switch (getHeader().getOid()) {
		case GET:
			return wrapper.put((byte) ((boolean) payload[currentIndex] ? 1 : 0)).get();
		case SET:
			wrapper.putString((String) payload[currentIndex++], true);
			wrapper.put((byte) ((boolean) payload[currentIndex] ? 1 : 0));
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

		switch (getHeader().getOid()) {
		case GET:
			informations.add(wrapper.get(first) == 1 ? true : false);
			return informations.toArray();
		case SET:
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(new String(wrapper.getString(first, playerNameLength)));
			first += playerNameLength;
			informations.add(wrapper.get(first) == 1 ? true : false);
			return informations.toArray();
		default:
			return informations.toArray();
		}
	}
}
