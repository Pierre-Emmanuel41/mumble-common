package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class RemovePlayerFromChannelV10 extends MumbleMessage {
	private String channelName, playerName;

	/**
	 * Creates a message to remove a player from a channel.
	 * 
	 * @param header The message header.
	 */
	protected RemovePlayerFromChannelV10(IMumbleHeader header) {
		super(Identifier.REMOVE_PLAYER_FROM_CHANNEL, header);
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
	 * @return The name of channel to which a player is removed.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return The removed player name.
	 */
	public String getPlayerName() {
		return playerName;
	}
}
