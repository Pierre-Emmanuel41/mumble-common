package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerOnlineStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isOnline;

	/**
	 * Creates a message in order to set the online status of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerOnlineStatusV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_PLAYER_ONLINE_STATUS, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		playerName = wrapper.nextString(wrapper.nextInt());

		// Player's online status
		isOnline = wrapper.nextInt() == 1;

		super.setProperties(playerName, isOnline);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Player's name
		playerName = (String) properties[0];

		// Player's online status
		isOnline = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player's online status
		wrapper.putInt(isOnline ? 1 : 0);

		return wrapper.get();
	}

	/**
	 * @return The player's name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player's online status.
	 */
	public boolean isOnline() {
		return isOnline;
	}
}
