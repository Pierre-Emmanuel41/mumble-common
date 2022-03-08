package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerAddMessageV10 extends MumbleMessage {
	private FullPlayerInfo playerInfo;

	/**
	 * Creates a message in order to get information about a specific player.
	 * 
	 * @param header The message header.
	 */
	protected PlayerAddMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_ADD, header);
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

		// Player's identifier
		int identifierLength = wrapper.getInt(first);
		first += 4;
		UUID identifier = UUID.fromString(wrapper.getString(first, identifierLength));
		properties.add(identifier);
		first += identifierLength;

		// Player's game address
		int addressLength = wrapper.getInt(first);
		first += 4;
		String gameAddress = wrapper.getString(first, addressLength);
		properties.add(gameAddress);
		first += addressLength;

		// Player's game port
		int gamePort = wrapper.getInt(first);
		properties.add(gamePort);
		first += 4;

		// Player's administrator status
		boolean isAdmin = wrapper.getInt(first) == 1 ? true : false;
		properties.add(isAdmin);
		first += 4;

		// Player's mute status
		boolean isMute = wrapper.getInt(first) == 1 ? true : false;
		properties.add(isMute);
		first += 4;

		// Player's deafen status
		boolean isDeafen = wrapper.getInt(first) == 1 ? true : false;
		properties.add(isDeafen);
		first += 4;

		// Player's x coordinate
		double x = wrapper.getDouble(first);
		properties.add(x);
		first += 8;

		// Player's y coordinate
		double y = wrapper.getDouble(first);
		properties.add(y);
		first += 8;

		// Player's z coordinate
		double z = wrapper.getDouble(first);
		properties.add(z);
		first += 8;

		// Player's yaw angle
		double yaw = wrapper.getDouble(first);
		properties.add(yaw);
		first += 8;

		// Player's pitch angle
		double pitch = wrapper.getDouble(first);
		properties.add(pitch);
		first += 8;

		playerInfo = new FullPlayerInfo(playerName, true, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
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

		// Player's identifier
		UUID identifier = (UUID) properties[2];

		// Player's game address
		String gameAddress = (String) properties[3];

		// Player's game port
		int gamePort = (int) properties[4];

		// Player's administrator status
		boolean isAdmin = (boolean) properties[5];

		// Player's mute status
		boolean isMute = (boolean) properties[6];

		// Player's deafen status
		boolean isDeafen = (boolean) properties[7];

		// Player's x coordinate
		double x = (double) properties[8];

		// Player's y coordinate
		double y = (double) properties[9];

		// Player's z coordinate
		double z = (double) properties[10];

		// Player's yaw angle
		double yaw = (double) properties[11];

		// Player's pitch angle
		double pitch = (double) properties[12];

		playerInfo = new FullPlayerInfo(playerName, true, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerInfo.getName(), true);

		// Player's identifier
		wrapper.putString(playerInfo.getIdentifier().toString(), true);

		// Player's game address
		wrapper.putString(playerInfo.getGameAddress(), true);

		// Player's game port
		wrapper.putInt(playerInfo.getGamePort());

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

		return wrapper.get();
	}

	/**
	 * @return A complete description of a player.
	 */
	public FullPlayerInfo getPlayerInfo() {
		return playerInfo;
	}
}