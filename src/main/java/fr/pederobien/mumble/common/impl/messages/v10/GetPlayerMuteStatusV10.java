package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GetPlayerMuteStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isMute;

	/**
	 * Creates a message in order to get the mute status of a player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerMuteStatusV10(IMumbleHeader header) {
		super(Identifier.GET_PLAYER_MUTE, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player's name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		playerName = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// When it is an answer
		if (first < payload.length)
			// Player's mute status
			isMute = wrapper.getInt(first) == 1;

		super.setProperties(playerName, isMute);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Player's name
		playerName = (String) properties[0];

		// When it is an answer
		if (properties.length > 1)
			// Player's mute status
			isMute = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// When it is an answer
		if (getProperties().length > 1)
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
