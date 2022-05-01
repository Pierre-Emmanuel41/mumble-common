package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class SetServerJoinV10 extends MumbleMessage {

	/**
	 * Creates a message in order to join a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected SetServerJoinV10(IMumbleHeader header) {
		super(Identifier.SET_SERVER_JOIN, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		return this;
	}

	@Override
	protected byte[] generateProperties() {
		return new byte[0];
	}
}
