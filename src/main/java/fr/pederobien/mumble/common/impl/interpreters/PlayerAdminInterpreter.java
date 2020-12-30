package fr.pederobien.mumble.common.impl.interpreters;

import fr.pederobien.utils.ByteWrapper;

public class PlayerAdminInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		return ByteWrapper.create().put((byte) ((boolean) payload[0] ? 1 : 0)).get();
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		return new Object[] { ByteWrapper.wrap(payload).get(0) == 1 };
	}
}
