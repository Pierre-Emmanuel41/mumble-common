package fr.pederobien.mumble.common.impl;

import java.util.function.Consumer;

import fr.pederobien.communication.ResponseCallbackArgs;
import fr.pederobien.communication.impl.RequestCallbackMessage;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleRequestMessage extends RequestCallbackMessage {
	private IMessage<Header> message;

	public MumbleRequestMessage(IMessage<Header> message, Consumer<ResponseCallbackArgs> callback, int timeout) {
		super(message.getBytes(), message.getIdentifier(), callback, timeout);
		this.message = message;
	}

	public MumbleRequestMessage(IMessage<Header> message, Consumer<ResponseCallbackArgs> callback) {
		super(message.getBytes(), message.getIdentifier(), callback);
		this.message = message;
	}

	@Override
	public String toString() {
		return message.toString();
	}
}
