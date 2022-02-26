package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GamePortGetMessageV10 extends MumbleMessage {
	private int port;

	/**
	 * Creates a message to check on client side a specific game port.
	 * 
	 * @param header The message header.
	 */
	protected GamePortGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.GAME_PORT_GET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		port = ByteWrapper.wrap(payload).getInt(0);

		super.setProperties(port);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		port = (int) properties[0];
	}

	@Override
	protected byte[] generateProperties() {
		if (getHeader().isError())
			return new byte[0];

		return ByteWrapper.create().putInt(port).get();
	}

	/**
	 * @return The port number to check on client side.
	 */
	public int getPort() {
		return port;
	}
}
