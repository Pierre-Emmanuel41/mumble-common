package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class ServerJoinInfoMessageV10 extends MumbleMessage {

	/**
	 * Creates a message in order to join a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected ServerJoinInfoMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.SERVER_JOIN_SET, header);
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
