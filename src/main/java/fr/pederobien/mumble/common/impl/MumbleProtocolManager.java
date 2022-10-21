package fr.pederobien.mumble.common.impl;

import java.util.Optional;

import fr.pederobien.messenger.impl.ProtocolManager;
import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.messenger.interfaces.IProtocol;
import fr.pederobien.mumble.common.impl.messages.v10.ProtocolV10;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.mumble.common.interfaces.IMumbleMessage;

public class MumbleProtocolManager {
	private ProtocolManager manager;
	private float version;
	private IProtocol protocol;

	/**
	 * Creates a protocol manager associated to the mumble communication protocol.
	 * 
	 * @param beginIdentifier The first identifier from which next messages identifier are incremented by 1.
	 */
	public MumbleProtocolManager(int beginIdentifier) {
		manager = new ProtocolManager(beginIdentifier, version -> new MumbleHeader(version), header -> ((IMumbleHeader) header).getIdentifier().name());

		initializeProtocols();

		version = 1.0f;
		protocol = manager.getProtocol(version).get();
	}

	/**
	 * Create a message based on the given parameters associated to the latest version of the communication protocol.
	 * 
	 * @param mumbleIdentifier The identifier of the request to create.
	 * @param mumbleErrorCode  The message errorCode.
	 * @param properties The message properties.
	 * 
	 * @return The created message.
	 */
	public IMumbleMessage create(MumbleIdentifier mumbleIdentifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		IMumbleMessage message = (IMumbleMessage) protocol.get(mumbleIdentifier.name());
		if (message == null)
			return null;

		message.getHeader().setProperties(mumbleIdentifier, mumbleErrorCode);
		message.setProperties(properties);
		return message;
	}

	/**
	 * Create a message based on the given parameters associated to a specific version of the communication protocol.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param mumbleIdentifier The identifier of the request to create.
	 * @param mumbleErrorCode  The message errorCode.
	 * @param properties The message properties.
	 * 
	 * @return A message associated to the given protocol version.
	 */
	public IMumbleMessage create(float version, MumbleIdentifier mumbleIdentifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		Optional<IProtocol> optProtocol = manager.getProtocol(version);
		if (!optProtocol.isPresent())
			return null;

		IMumbleMessage message = (IMumbleMessage) optProtocol.get().get(mumbleIdentifier.name());
		if (message == null)
			return null;

		message.getHeader().setProperties(mumbleIdentifier, mumbleErrorCode);
		message.setProperties(properties);
		return message;
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. Neither the identifier nor the header are
	 * modified. The latest version of the communication protocol is used to create the answer.
	 * 
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage answer(IMessage message, Object... properties) {
		return (IMumbleMessage) protocol.answer(message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. Neither the identifier nor the header are
	 * modified. A specific version of the communication protocol is used to create the answer.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMumbleMessage answer(float version, IMessage message, Object... properties) {
		Optional<IProtocol> optProtocol = manager.getProtocol(version);
		if (!optProtocol.isPresent())
			return null;

		return (IMumbleMessage) optProtocol.get().answer(message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented. The latest
	 * version of the communication protocol is used to create the returned message.
	 * 
	 * @param sequence   The sequence number of the answer request.
	 * @param mumbleIdentifier The identifier of the request to create.
	 * @param mumbleErrorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(int sequence, MumbleIdentifier mumbleIdentifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		IMumbleMessage message = (IMumbleMessage) protocol.get(mumbleIdentifier.name());
		if (message == null)
			return null;

		message.getHeader().setSequence(sequence);
		message.getHeader().setProperties(mumbleIdentifier, mumbleErrorCode);
		message.setProperties(properties);
		return message;
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented. A specific
	 * version of the communication protocol is used to create the answer.
	 * 
	 * @param version    The protocol version to use for the returned message.
	 * @param sequence   The sequence number of the answer request.
	 * @param mumbleIdentifier The identifier of the request to create.
	 * @param mumbleErrorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMumbleMessage answer(float version, int sequence, MumbleIdentifier mumbleIdentifier, MumbleErrorCode mumbleErrorCode, Object... properties) {
		Optional<IProtocol> optProtocol = manager.getProtocol(version);
		if (!optProtocol.isPresent())
			return null;

		IMumbleMessage message = (IMumbleMessage) optProtocol.get().get(mumbleIdentifier.name());
		if (message == null)
			return null;

		message.getHeader().setSequence(sequence);
		message.getHeader().setProperties(mumbleIdentifier, mumbleErrorCode);
		message.setProperties(properties);
		return message;
	}

	/**
	 * @return The protocol manager in order to generate/parse messages.
	 */
	public ProtocolManager getManager() {
		return manager;
	}

	/**
	 * @return The latest protocol version.
	 */
	public IProtocol getProtocol() {
		return protocol;
	}

	private void initializeProtocols() {
		IProtocol protocolV10 = new ProtocolV10(this);
		manager.register(protocolV10.getVersion(), protocolV10);
	}
}
