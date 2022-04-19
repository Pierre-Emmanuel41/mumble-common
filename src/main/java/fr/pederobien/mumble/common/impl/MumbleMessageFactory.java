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
	 * Creates a message based on the given parameters associated to the latest version of the communication protocol.
	 * 
	 * @param idc       The message IDC.
	 * @param oid       The message OID.
	 * @param errorCode The message errorCode.
	 * @param payload   The message payload.
	 * 
	 * @return The created message.
	 */
	public IMumbleMessage create(Idc idc, Oid oid, ErrorCode errorCode, Object... payload) {
		return manager.create(idc, oid, errorCode, payload);
	}

	/**
	 * Creates a message based on the given parameters associated to a specific version of the communication protocol.
	 * 
	 * @param version   The protocol version to use for the returned message.
	 * @param idc       The message IDC.
	 * @param oid       The message OID.
	 * @param errorCode The message errorCode.
	 * @param payload   The message payload.
	 * 
	 * @return A message associated to the given protocol version.
	 */
	public IMumbleMessage create(float version, Idc idc, Oid oid, ErrorCode errorCode, Object... payload) {
		return manager.create(version, idc, oid, errorCode, payload);
	}

	/**
	 * Parses the given buffer in order to create the associated header and the payload.
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
	 * modified. The latest version of the communication protocol is used to create the returned message.
	 * 
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Object... properties) {
		return manager.answer(message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. Neither the identifier nor the header are
	 * modified. A specific version of the communication protocol is used to create the returned message.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage answer(float version, IMumbleMessage message, Object... properties) {
		return manager.answer(version, message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented. The latest
	 * version of the communication protocol is used to create the answer.
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
		return manager.answer(message.getHeader().getSequence(), idc, oid, errorCode, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented. A specific
	 * version of the communication protocol is used to create the answer.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param message    The message to answer.
	 * @param idc        The response IDC.
	 * @param oid        The response OID.
	 * @param errorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(float version, IMumbleMessage message, Idc idc, Oid oid, ErrorCode errorCode, Object... properties) {
		return manager.answer(version, message.getHeader().getSequence(), idc, oid, errorCode, properties);
	}
}
