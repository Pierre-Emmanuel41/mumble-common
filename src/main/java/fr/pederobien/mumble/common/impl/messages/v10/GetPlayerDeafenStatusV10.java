package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetPlayerDeafenStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isDeafen;

	/**
	 * Creates a message in order to get the deafen status of a player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerDeafenStatusV10(IMumbleHeader header) {
		super(Identifier.GET_PLAYER_DEAFEN, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		playerName = wrapper.nextString(wrapper.nextInt());

		try {
			// When it is a response

			// Player's deafen status
			isDeafen = wrapper.nextInt() == 1;
		} catch (IndexOutOfBoundsException e) {
			// When it is a request
		}

		super.setProperties(playerName, isDeafen);
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
			// Player's deafen status
			isDeafen = (boolean) properties[1];
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
