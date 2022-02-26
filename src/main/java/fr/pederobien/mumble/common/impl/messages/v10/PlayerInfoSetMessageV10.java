package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerInfoSetMessageV10 extends MumbleMessage {
	private String playerName, ipAddress;
	private boolean isOnline, isAdmin;
	private int gamePort;

	/**
	 * Creates a message in order to set information about a specific player.
	 * 
	 * @param header The message header.
	 */
	protected PlayerInfoSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_INFO_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		playerName = wrapper.getString(first, playerNameLength);
		properties.add(playerName);
		first += playerNameLength;

		// Player connected
		isOnline = wrapper.getInt(first) == 1;
		properties.add(isOnline);
		first += 4;

		if (isOnline) {
			// Player IP address
			int addressLength = wrapper.getInt(first);
			first += 4;
			ipAddress = wrapper.getString(first, addressLength);
			properties.add(ipAddress);
			first += addressLength;

			// Player game port number
			gamePort = wrapper.getInt(first);
			properties.add(gamePort);
			first += 4;

			// Player admin
			isAdmin = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isAdmin);
		}

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
		isOnline = (boolean) properties[1];
		if (isOnline) {
			ipAddress = (String) properties[2];
			gamePort = (int) properties[3];
			isAdmin = (boolean) properties[4];
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player name
		wrapper.putString(playerName, true);

		// Player connected
		if (isOnline) {
			// Player online
			wrapper.putInt(1);

			// Player IP address
			wrapper.putString(ipAddress, true);

			// Player game port number
			wrapper.putInt(gamePort);

			// Player admin
			wrapper.putInt(isAdmin ? 1 : 0);
		} else
			wrapper.putInt(0);

		return wrapper.get();
	}

	/**
	 * @return The player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player online status.
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * @return The player admin status.
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @return The player IP address to play to the game.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return The port used by the player to player to the game.
	 */
	public int getGamePort() {
		return gamePort;
	}
}
