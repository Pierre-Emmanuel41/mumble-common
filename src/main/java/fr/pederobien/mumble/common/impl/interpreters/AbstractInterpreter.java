package fr.pederobien.mumble.common.impl.interpreters;

import java.util.function.Function;

import fr.pederobien.messenger.interfaces.IMessageInterpreter;
import fr.pederobien.mumble.common.impl.Header;

public abstract class AbstractInterpreter implements Function<Header, IMessageInterpreter>, IMessageInterpreter {
	private Header header;

	@Override
	public final byte[] generate(Object[] payload) {
		if (payload.length == 0)
			return new byte[0];

		return internalGenerate(payload);
	}

	@Override
	public final Object[] interprete(byte[] payload) {
		if (getHeader() != null && getHeader().isError() || payload.length == 0)
			return new Object[0];

		return internalInterprete(payload);
	}

	@Override
	public IMessageInterpreter apply(Header header) {
		this.header = header;
		return this;
	}

	/**
	 * @return The header used to selected this interpreter.
	 */
	protected Header getHeader() {
		return header;
	}

	/**
	 * Generates the bytes array associated to the given objects array. No need to check the array length, this has already been done.
	 * 
	 * @param payload An array that contains informations to send to the remote.
	 * 
	 * @return The associated byte array.
	 */
	protected abstract byte[] internalGenerate(Object[] payload);

	/**
	 * Interpret the byte array in order to recreate the payload. No need to check the array length or if the header contains error,
	 * this has already been done.
	 * 
	 * @param payload An array that contains informations received from the remote.
	 * 
	 * @return The associated objects array.
	 */
	protected abstract Object[] internalInterprete(byte[] payload);
}
