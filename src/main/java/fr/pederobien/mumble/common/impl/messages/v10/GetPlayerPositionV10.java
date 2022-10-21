package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.CoordinatePlayerInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetPlayerPositionV10 extends MumbleMessage {
	private CoordinatePlayerInfo playerInfo;

	/**
	 * Creates a message in order to get the position of a player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerPositionV10(IMumbleHeader header) {
		super(MumbleIdentifier.GET_PLAYER_POSITION, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player name
		String playerName = wrapper.nextString(wrapper.nextInt());
		properties.add(playerName);

		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;
		try {
			// When it is a response

			// X position
			x = wrapper.nextDouble();
			properties.add(x);

			// Y position
			y = wrapper.nextDouble();
			properties.add(y);

			// Z position
			z = wrapper.nextDouble();
			properties.add(z);

			// Yaw position
			yaw = wrapper.nextDouble();
			properties.add(yaw);

			// Pitch position
			pitch = wrapper.nextDouble();
			properties.add(pitch);
		} catch (IndexOutOfBoundsException e) {
			// When it is a request
		}

		playerInfo = new CoordinatePlayerInfo(playerName, x, y, z, yaw, pitch);

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		String playerName = (String) properties[0];

		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;

		// When it is an answer
		if (properties.length > 1) {
			x = (double) properties[1];
			y = (double) properties[2];
			z = (double) properties[3];
			yaw = (double) properties[4];
			pitch = (double) properties[5];
		}

		playerInfo = new CoordinatePlayerInfo(playerName, x, y, z, yaw, pitch);
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player name
		wrapper.putString(playerInfo.getName(), true);

		// When it is an answer
		if (getProperties().length > 1) {
			// X position
			wrapper.putDouble(playerInfo.getX());

			// Y position
			wrapper.putDouble(playerInfo.getY());

			// Z position
			wrapper.putDouble(playerInfo.getZ());

			// Yaw position
			wrapper.putDouble(playerInfo.getYaw());

			// Pitch position
			wrapper.putDouble(playerInfo.getPitch());
		}

		return wrapper.get();
	}

	/**
	 * @return The player coordinates. It is not null when received from the server.
	 */
	public CoordinatePlayerInfo getPlayerInfo() {
		return playerInfo;
	}
}
