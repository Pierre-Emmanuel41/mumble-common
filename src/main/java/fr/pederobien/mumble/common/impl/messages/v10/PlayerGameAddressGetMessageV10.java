package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerGameAddressGetMessageV10 extends MumbleMessage {
	private String playerName;
	private String gameAddress;
	private int gamePort;

	protected PlayerGameAddressGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_ONLINE_GET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
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

		// Player's game address
		int gameAddressLength = wrapper.getInt(first);
		first += 4;
		gameAddress = wrapper.getString(first, gameAddressLength);
		properties.add(gameAddress);
		first += 4;

		// Player's game port
		gamePort = wrapper.getInt(first);
		properties.add(gamePort);
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
		gameAddress = (String) properties[1];

		// Player's port number
		gamePort = (int) properties[2];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player's game address
		wrapper.putString(gameAddress, true);

		// Player's online status
		wrapper.putInt(gamePort);

		return wrapper.get();
	}

	/**
	 * @return The player's name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player's address used to play to the game.
	 */
	public String getGameAddress() {
		return gameAddress;
	}

	/**
	 * @return The player's port number used to play to the game.
	 */
	public int getGamePort() {
		return gamePort;
	}
}
