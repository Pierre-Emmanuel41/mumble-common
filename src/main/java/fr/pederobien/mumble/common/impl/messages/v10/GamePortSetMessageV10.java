package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GamePortSetMessageV10 extends MumbleMessage {
	private int port;
	private boolean isUsed;

	/**
	 * Creates a message to specify if a specific game port is used on client side.
	 * 
	 * @param header The message header.
	 */
	protected GamePortSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.GAME_PORT_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Port number
		port = wrapper.getInt(first);
		first += 4;

		// Port used
		isUsed = wrapper.getInt(first) == 1;

		super.setProperties(port, isUsed);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		port = (int) properties[0];
		isUsed = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Port number
		wrapper.putInt(port);

		// Port used
		wrapper.putInt(isUsed ? 1 : 0);

		return wrapper.get();
	}

	/**
	 * @return The checked port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return True if the port is used on client side, false otherwise.
	 */
	public boolean isUsed() {
		return isUsed;
	}
}
