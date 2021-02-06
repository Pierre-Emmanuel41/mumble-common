package fr.pederobien.mumble.common.impl.interpreters;

import fr.pederobien.utils.ByteWrapper;

public class UdpPortInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		return ByteWrapper.create().putInt((int) payload[0]).get();
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		return new Object[] { ByteWrapper.wrap(payload).getInt(0) };
	}
}
