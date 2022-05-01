package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GetPlayerInfoV10 extends MumbleMessage {
	private FullPlayerInfo playerInfo;

	/**
	 * Creates a message in order to get information about a specific player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerInfoV10(IMumbleHeader header) {
		super(Identifier.GET_PLAYER_INFO, header);
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
		String playerName = wrapper.getString(first, playerNameLength);
		properties.add(playerName);
		first += playerNameLength;

		UUID identifier = null;
		String gameAddress = null;
		int gamePort = 0;
		boolean isOnline = false, isAdmin = false, isMute = false, isDeafen = false;
		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;

		// When it is an answer
		if (first < payload.length) {

			// Player connected
			isOnline = wrapper.getInt(first) == 1;
			properties.add(isOnline);
			first += 4;

			if (isOnline) {
				// Player's identifier
				int identifierLength = wrapper.getInt(first);
				first += 4;
				identifier = UUID.fromString(wrapper.getString(first, identifierLength));
				properties.add(identifier);
				first += identifierLength;

				// Player's game address
				int addressLength = wrapper.getInt(first);
				first += 4;
				gameAddress = wrapper.getString(first, addressLength);
				properties.add(gameAddress);
				first += addressLength;

				// Player's game port
				gamePort = wrapper.getInt(first);
				properties.add(gamePort);
				first += 4;

				// Player's administrator status
				isAdmin = wrapper.getInt(first) == 1 ? true : false;
				properties.add(isAdmin);
				first += 4;

				// Player's mute status
				isMute = wrapper.getInt(first) == 1 ? true : false;
				properties.add(isMute);
				first += 4;

				// Player's deafen status
				isDeafen = wrapper.getInt(first) == 1 ? true : false;
				properties.add(isDeafen);
				first += 4;

				// Player's x coordinate
				x = wrapper.getDouble(first);
				properties.add(x);
				first += 8;

				// Player's y coordinate
				y = wrapper.getDouble(first);
				properties.add(y);
				first += 8;

				// Player's z coordinate
				z = wrapper.getDouble(first);
				properties.add(z);
				first += 8;

				// Player's yaw angle
				yaw = wrapper.getDouble(first);
				properties.add(yaw);
				first += 8;

				// Player's pitch angle
				pitch = wrapper.getDouble(first);
				properties.add(pitch);
				first += 8;
			}
		}

		playerInfo = new FullPlayerInfo(playerName, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Player's name
		String playerName = (String) properties[0];

		UUID identifier = null;
		String gameAddress = null;
		int gamePort = 0;
		boolean isOnline = false, isAdmin = false, isMute = false, isDeafen = false;
		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;

		// When it is an answer
		if (properties.length > 1) {
			// Player's online status
			isOnline = (boolean) properties[1];

			if (isOnline) {
				// Player's identifier
				identifier = (UUID) properties[2];

				// Player's game address
				gameAddress = (String) properties[3];

				// Player's game port
				gamePort = (int) properties[4];

				// Player's administrator status
				isAdmin = (boolean) properties[5];

				// Player's mute status
				isMute = (boolean) properties[6];

				// Player's deafen status
				isDeafen = (boolean) properties[7];

				// Player's x coordinate
				x = (double) properties[8];

				// Player's y coordinate
				y = (double) properties[9];

				// Player's z coordinate
				z = (double) properties[10];

				// Player's yaw angle
				yaw = (double) properties[11];

				// Player's pitch angle
				pitch = (double) properties[12];
			}
		}

		playerInfo = new FullPlayerInfo(playerName, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerInfo.getName(), true);

		if (getProperties().length > 1) {

			// Player's online status
			wrapper.putInt(playerInfo.isOnline() ? 1 : 0);

			// Player connected
			if (playerInfo.isOnline()) {
				// Player's identifier
				wrapper.putString(playerInfo.getIdentifier().toString(), true);

				// Player's game address
				wrapper.putString(playerInfo.getGameAddress().getAddress().getHostAddress(), true);

				// Player's game port
				wrapper.putInt(playerInfo.getGameAddress().getPort());

				// Player's administrator status
				wrapper.putInt(playerInfo.isAdmin() ? 1 : 0);

				// Player's mute status
				wrapper.putInt(playerInfo.isMute() ? 1 : 0);

				// Player's deafen status
				wrapper.putInt(playerInfo.isDeafen() ? 1 : 0);

				// Player's x coordinate
				wrapper.putDouble(playerInfo.getX());

				// Player's y coordinate
				wrapper.putDouble(playerInfo.getY());

				// Player's z coordinate
				wrapper.putDouble(playerInfo.getZ());

				// Player's yaw angle
				wrapper.putDouble(playerInfo.getYaw());

				// Player's pitch angle
				wrapper.putDouble(playerInfo.getPitch());
			}
		}

		return wrapper.get();
	}

	/**
	 * @return A complete description of a player.
	 */
	public FullPlayerInfo getPlayerInfo() {
		return playerInfo;
	}
}
