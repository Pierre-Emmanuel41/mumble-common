package fr.pederobien.mumble.common.impl;

import java.net.InetSocketAddress;

import fr.pederobien.communication.impl.AddressMessage;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleAddressMessage extends AddressMessage {
	private IMessage<Header> message;

	public MumbleAddressMessage(IMessage<Header> message, InetSocketAddress address) {
		super(message.getBytes(), message.getIdentifier(), address);
		this.message = message;
	}

	@Override
	public String toString() {
		return message.toString();
	}

}
