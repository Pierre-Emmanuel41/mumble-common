package fr.pederobien.mumble.common.impl;

import fr.pederobien.communication.impl.RequestMessage;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleRequestMessage extends RequestMessage {
	private IMessage<Header> message;

	/**
	 * Create a request message to be send to a remote.
	 * 
	 * @param message The message that contains the bytes to send to the remote and the identifier.
	 */
	public MumbleRequestMessage(IMessage<Header> message) {
		super(message.getBytes(), message.getIdentifier());
		this.message = message;
	}

	@Override
	public String toString() {
		return message.toString();
	}
}
