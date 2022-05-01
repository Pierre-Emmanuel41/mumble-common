package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SetPlayerDeafenStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isDeafen;

	/**
	 * Creates a message in order to set the deafen status of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerDeafenStatusV10(IMumbleHeader header) {
		super(Identifier.SET_PLAYER_DEAFEN, header);
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

		// Player's deafen status
		isDeafen = wrapper.getInt(first) == 1;

		super.setProperties(playerName, isDeafen);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
		isDeafen = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player's deafen status
		wrapper.putInt(isDeafen ? 1 : 0);
		return wrapper.get();
	}

	/**
	 * @return The player name whose the deafen status has changed.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return True if the player is deafen, false otherwise.
	 */
	public boolean isDeafen() {
		return isDeafen;
	}
}
