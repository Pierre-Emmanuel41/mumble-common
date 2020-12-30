package fr.pederobien.mumble.common.impl;

import fr.pederobien.messenger.impl.MessageFactory;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleMessageFactory {
	private static final MessageFactory<Header> FACTORY = new MessageFactory<Header>(new MumbleInterpretersFactory());

	/**
	 * Create a message based on the given parameters.
	 * 
	 * @param idc       The message idc.
	 * @param oid       The message oid.
	 * @param errorCode The message errorCode.
	 * @param payload   The message payload.
	 * 
	 * @return The created message.
	 */
	public static IMessage<Header> create(Idc idc, Oid oid, ErrorCode errorCode, Object... payload) {
		return FACTORY.create(new Header(idc, oid, errorCode), payload);
	}

	/**
	 * Create a message based on the given parameters.
	 * 
	 * @param idc     The message idc.
	 * @param oid     The message oid.
	 * @param payload The message payload.
	 * 
	 * @return The created message.
	 */
	public static IMessage<Header> create(Idc idc, Oid oid, Object... payload) {
		return FACTORY.create(new Header(idc, oid), payload);
	}

	/**
	 * Create a message based on the given parameters.
	 * 
	 * @param idc     The message idc.
	 * @param payload The message payload.
	 * 
	 * @return The created message.
	 */
	public static IMessage<Header> create(Idc idc, Object... payload) {
		return FACTORY.create(new Header(idc), payload);
	}

	/**
	 * Parse the given buffer in order to create the associated header and the payload.
	 * 
	 * @param buffer The bytes array received from the remote.
	 * 
	 * @return A new message.
	 */
	public static IMessage<Header> parse(byte[] buffer) {
		return FACTORY.parse(new Header(), buffer);
	}

	public static IMessage<Header> answer(IMessage<Header> request, Idc idc, Oid oid, Object... payload) {
		return request.answer(new Header(idc, oid), payload);
	}

	public static IMessage<Header> answer(IMessage<Header> request, Idc idc, Object... payload) {
		return request.answer(new Header(idc), payload);
	}

	public static IMessage<Header> answer(IMessage<Header> request, ErrorCode errorCode) {
		return request.answer(new Header(request.getHeader().getIdc(), request.getHeader().getOid(), errorCode));
	}
}
