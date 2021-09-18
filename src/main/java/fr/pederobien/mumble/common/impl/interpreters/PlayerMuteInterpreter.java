package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerMuteInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		switch (getHeader().getOid()) {
		case SET:
			wrapper.putString((String) payload[currentIndex++], true);
			wrapper.putInt((boolean) payload[currentIndex] ? 1 : 0);
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
		case SET:
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(new String(wrapper.getString(first, playerNameLength)));
			first += playerNameLength;
			informations.add(wrapper.getInt(first) == 1 ? true : false);
			return informations.toArray();
		default:
			return informations.toArray();
		}
	}
}
