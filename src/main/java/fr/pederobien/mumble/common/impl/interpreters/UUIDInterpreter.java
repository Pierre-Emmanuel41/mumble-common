package fr.pederobien.mumble.common.impl.interpreters;

import java.util.UUID;

import fr.pederobien.utils.ByteWrapper;

public class UUIDInterpreter extends AbstractInterpreter {

	@Override
	public byte[] internalGenerate(Object[] payload) {
		return ByteWrapper.create().putString((String) payload[0]).get();
	}

	@Override
	public Object[] internalInterprete(byte[] payload) {
		return new Object[] { UUID.fromString((String) ByteWrapper.wrap(payload).getString()) };
	}
}
