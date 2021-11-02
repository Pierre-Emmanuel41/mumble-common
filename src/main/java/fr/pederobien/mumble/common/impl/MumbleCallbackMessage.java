package fr.pederobien.mumble.common.impl;

import java.util.function.Consumer;

import fr.pederobien.communication.ResponseCallbackArgs;
import fr.pederobien.communication.impl.RequestCallbackMessage;
import fr.pederobien.messenger.interfaces.IMessage;

public class MumbleCallbackMessage extends RequestCallbackMessage {
	private IMessage<Header> message;

	/**
	 * Create a request message to be send to a remote.
	 * 
	 * @param message  The message that contains the bytes to send to the remote and the identifier.
	 * @param callback The callback to run when a response has been received before the timeout.
	 * @param timeout  The request timeout.
	 */
	public MumbleCallbackMessage(IMessage<Header> message, Consumer<ResponseCallbackArgs> callback, int timeout) {
		super(message.getBytes(), message.getIdentifier(), callback, timeout);
		this.message = message;
	}

	/**
	 * Create a request message to be send to a remote.
	 * 
	 * @param message  The message that contains the bytes to send to the remote and the identifier.
	 * @param callback The callback to run when a response has been received before the timeout.
	 */
	public MumbleCallbackMessage(IMessage<Header> message, Consumer<ResponseCallbackArgs> callback) {
		super(message.getBytes(), message.getIdentifier(), callback);
		this.message = message;
	}

	/**
	 * Create a request message to be send to a remote.
	 * 
	 * @param message  The message that contains the bytes to send to the remote and the identifier.
	 * @param callback The callback to run when a response has been received before the timeout.
	 */
	public MumbleCallbackMessage(IMessage<Header> message) {
		super(message.getBytes(), message.getIdentifier());
		this.message = message;
	}

	@Override
	public String toString() {
		return message.toString();
	}
}
