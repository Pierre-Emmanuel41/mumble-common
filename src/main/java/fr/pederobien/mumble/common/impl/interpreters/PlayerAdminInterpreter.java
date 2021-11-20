package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class PlayerAdminInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();

		// Player's name
		wrapper.putString((String) payload[currentIndex++], true);

		// Player's admin
		wrapper.putInt((boolean) payload[currentIndex++] ? 1 : 0);

		return wrapper.get();
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();

		// Player's name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		informations.add(wrapper.getString(first, playerNameLength));
		first += playerNameLength;

		// PLayer's admin
		informations.add(wrapper.getInt(first) == 1);

		return informations.toArray();
	}
}
