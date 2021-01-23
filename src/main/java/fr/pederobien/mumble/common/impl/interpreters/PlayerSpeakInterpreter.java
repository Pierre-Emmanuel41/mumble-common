package fr.pederobien.mumble.common.impl.interpreters;

public class PlayerSpeakInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		return (byte[]) payload[0];
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		return new Object[] { payload };
	}
}
