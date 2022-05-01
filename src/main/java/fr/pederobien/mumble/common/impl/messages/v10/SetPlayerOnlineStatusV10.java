package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SetPlayerOnlineStatusV10 extends MumbleMessage {
	private String playerName;
	private boolean isOnline;

	/**
	 * Creates a message in order to set the online status of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerOnlineStatusV10(IMumbleHeader header) {
		super(Identifier.SET_PLAYER_ONLINE_STATUS, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player's name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		playerName = wrapper.getString(first, playerNameLength);
		properties.add(playerName);
		first += playerNameLength;

		// Player's online status
		isOnline = wrapper.getInt(first) == 1;
		properties.add(isOnline);
		first += 4;

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
