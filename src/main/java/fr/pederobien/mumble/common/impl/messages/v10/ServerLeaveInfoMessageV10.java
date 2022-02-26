package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class ServerLeaveInfoMessageV10 extends MumbleMessage {

	/**
	 * Creates a mumble message to leave a server.
	 * 
	 * @param header The message header.
	 */
	protected ServerLeaveInfoMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.SERVER_LEAVE_INFO, header);
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
