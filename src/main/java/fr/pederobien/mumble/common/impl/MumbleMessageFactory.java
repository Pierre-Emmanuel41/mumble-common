package fr.pederobien.mumble.common.impl;

import fr.pederobien.mumble.common.interfaces.IMumbleMessage;

public class MumbleMessageFactory {
	private MumbleProtocolManager manager;

	/**
	 * Creates a message factory associated to the mumble communication protocol.
	 * 
	 * @param beginIdentifier The first identifier from which next messages identifier are incremented by 1.
	 */
	private MumbleMessageFactory(int beginIdentifier) {
		manager = new MumbleProtocolManager(beginIdentifier);
	}

	/**
	 * Creates a message factory associated to the mumble communication protocol.
	 * 
	 * @param beginIdentifier The first identifier from which next messages identifier are incremented by 1.
	 */
	public static MumbleMessageFactory getInstance(int beginIdentifier) {
		return new MumbleMessageFactory(beginIdentifier);
	}

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
	public IMumbleMessage create(Idc idc, Oid oid, ErrorCode errorCode, Object... payload) {
		return (IMumbleMessage) manager.create(idc, oid, errorCode, payload);
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
	public IMumbleMessage create(Idc idc, Oid oid, Object... payload) {
		return create(idc, oid, ErrorCode.NONE, payload);
	}

	/**
	 * Create a message based on the given parameters.
	 * 
	 * @param idc     The message idc.
	 * @param payload The message payload.
	 * 
	 * @return The created message.
	 */
	public IMumbleMessage create(Idc idc, Object... payload) {
		return create(idc, Oid.GET, payload);
	}

	/**
	 * Parse the given buffer in order to create the associated header and the payload.
	 * 
	 * @param buffer The bytes array received from the remote.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage parse(byte[] buffer) {
		return (IMumbleMessage) manager.getManager().parse(buffer);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. Neither the identifier nor the header are
	 * modified.
	 * 
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Object... properties) {
		return (IMumbleMessage) manager.answer(message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented.
	 * 
	 * @param message    The message to answer.
	 * @param idc        The response IDC.
	 * @param oid        The response OID.
	 * @param errorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Idc idc, Oid oid, ErrorCode errorCode, Object... properties) {
		return (IMumbleMessage) manager.answer(message.getHeader().getSequence(), idc, oid, errorCode, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented.
	 * 
	 * @param request The request to answer.
	 * @param idc     The Idc of the response.
	 * @param payload The payload of the response.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Idc idc, Oid oid, Object... properties) {
		return answer(message, idc, oid, ErrorCode.NONE, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented.
	 * 
	 * @param request The request to answer.
	 * @param idc     The Idc of the response.
	 * @param payload The payload of the response.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Idc idc, Object... properties) {
		return answer(message, idc, Oid.GET, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented.
	 * 
	 * @param request   The request to answer.
	 * @param errorCode The error code of the response.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(IMumbleMessage message, ErrorCode errorCode) {
		return answer(message, message.getHeader().getIdc(), message.getHeader().getOid(), errorCode);
	}
}
