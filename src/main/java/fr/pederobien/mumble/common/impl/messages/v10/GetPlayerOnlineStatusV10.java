package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetPlayerOnlineStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isOnline;

	/**
	 * Creates a message in order to get the online status of a specific player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerOnlineStatusV10(IMumbleHeader header) {
		super(Identifier.GET_PLAYER_ONLINE_STATUS, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		properties.add(playerName = wrapper.nextString(wrapper.nextInt()));

		try {
			// When it is a response

			// Player's online status
			properties.add(isOnline = wrapper.nextInt() == 1);
		} catch (IndexOutOfBoundsException e) {
			// When it is a request
		}

		super.setProperties(properties.toArray());
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
			// Player's online status
			isOnline = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// When it is an answer
		if (getProperties().length > 1)
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
