package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.utils.ByteWrapper;

public class GamePortInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		ByteWrapper wrapper = ByteWrapper.create();
		switch (getHeader().getOid()) {
		case GET:
			// Port number
			wrapper.putInt((int) payload[0]);
			return wrapper.get();
		case SET:
			// Port number
			wrapper.putInt((int) payload[0]);

			// Port used
			wrapper.putInt((boolean) payload[1] ? 1 : 0);
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
			// Port number
			informations.add(wrapper.getInt(first));
			return informations.toArray();
		case SET:
			// Port number
			informations.add(wrapper.getInt(first));
			first += 4;

			// Port used
			informations.add(wrapper.getInt(first) == 1);
			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
