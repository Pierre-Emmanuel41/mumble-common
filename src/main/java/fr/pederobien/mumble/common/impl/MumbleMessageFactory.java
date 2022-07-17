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
	 * 
	 * @param identifier The identifier of the request to create.
	 * @param mumbleErrorCode  The message errorCode.
	 * @param properties The message properties.
	 * 
	 * @return The created message.
	 */
	public IMumbleMessage create(Identifier identifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		return manager.create(identifier, mumbleErrorCode, properties);
	}

	/**
	 * Creates a message based on the given parameters associated to a specific version of the communication protocol.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param identifier The identifier of the request to create.
	 * @param mumbleErrorCode  The message errorCode.
	 * @param properties The message properties.
	 * 
	 * @return A message associated to the given protocol version.
	 */
	public IMumbleMessage create(float version, Identifier identifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		return manager.create(version, identifier, mumbleErrorCode, properties);
	}

	/**
	 * Parses the given buffer in order to create the associated header and the properties.
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
	 * @param identifier The identifier of the answer request.
	 * @param mumbleErrorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(IMumbleMessage message, Identifier identifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		return manager.answer(message.getHeader().getSequence(), identifier, mumbleErrorCode, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented. A specific
	 * version of the communication protocol is used to create the answer.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param message    The message to answer.
	 * @param identifier The identifier of the answer request.
	 * @param mumbleErrorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(float version, IMumbleMessage message, Identifier identifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		return manager.answer(version, message.getHeader().getSequence(), identifier, mumbleErrorCode, properties);
	}
}
