package fr.pederobien.mumble.common.impl;

import fr.pederobien.communication.impl.RequestMessage;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleRequestMessage extends RequestMessage {

	public MumbleRequestMessage(IMessage<Header> message) {
		super(message.getBytes(), message.getIdentifier());
	}

}
