package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class SetServerLeaveV10 extends MumbleMessage {

	/**
	 * Creates a message in order to leave a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected SetServerLeaveV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_SERVER_LEAVE, header);
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
