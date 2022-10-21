package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class RemovePlayerFromChannelV10 extends MumbleMessage {
	private String channelName, playerName;

	/**
	 * Creates a message to remove a player from a channel.
	 * 
	 * @param header The message header.
	 */
	protected RemovePlayerFromChannelV10(IMumbleHeader header) {
		super(MumbleIdentifier.REMOVE_PLAYER_FROM_CHANNEL, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Channel name
		channelName = wrapper.nextString(wrapper.nextInt());

		// Player name
		playerName = wrapper.nextString(wrapper.nextInt());

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
