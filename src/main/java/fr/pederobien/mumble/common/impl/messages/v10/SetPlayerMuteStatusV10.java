package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerMuteStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isMute;

	/**
	 * Creates a message in order to set the mute status of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerMuteStatusV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_PLAYER_MUTE, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		playerName = wrapper.nextString(wrapper.nextInt());

		// Player's mute status
		isMute = wrapper.nextInt() == 1;

		super.setProperties(playerName, isMute);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
		isMute = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player's mute status
		wrapper.putInt(isMute ? 1 : 0);
		return wrapper.get();
	}

	/**
	 * @return The player name whose the mute status has changed.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return True if the player is mute, false otherwise.
	 */
	public boolean isMute() {
		return isMute;
	}
}
