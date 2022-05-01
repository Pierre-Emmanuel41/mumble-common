package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class AddPlayerToChannelV10 extends MumbleMessage {
	private String channelName, playerName;

	/**
	 * Creates a message to add a player to a channel.
	 * 
	 * @param header The message header.
	 */
	protected AddPlayerToChannelV10(IMumbleHeader header) {
		super(Identifier.ADD_PLAYER_TO_CHANNEL, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Channel name
		int channelNameLength = wrapper.getInt(first);
		first += 4;
		channelName = wrapper.getString(first, channelNameLength);
		first += channelNameLength;

		// Player name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		playerName = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		super.setProperties(channelName, playerName);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		channelName = (String) properties[0];
		playerName = (String) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Channel name
		wrapper.putString(channelName, true);

		// Player name
		wrapper.putString(playerName, true);
		return wrapper.get();
	}

	/**
	 * @return The name of channel to which a player is added.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return The added player name.
	 */
	public String getPlayerName() {
		return playerName;
	}
}
