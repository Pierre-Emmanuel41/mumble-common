package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerPositionV10 extends MumbleMessage {
	private String playerName;
	private double x, y, z, yaw, pitch;

	/**
	 * Creates a message to set the position of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerPositionV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_PLAYER_POSITION, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player name
		playerName = wrapper.nextString(wrapper.nextInt());

		// X position
		x = wrapper.nextDouble();

		// Y position
		y = wrapper.nextDouble();

		// Z position
		z = wrapper.nextDouble();

		// Yaw position
		yaw = wrapper.nextDouble();

		// Pitch position
		pitch = wrapper.nextDouble();

		super.setProperties(playerName, x, y, z, yaw, pitch);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
		x = (double) properties[1];
		y = (double) properties[2];
		z = (double) properties[3];
		yaw = (double) properties[4];
		pitch = (double) properties[5];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player name
		wrapper.putString(playerName, true);

		// X position
		wrapper.putDouble(x);

		// Y position
		wrapper.putDouble(y);

		// Z position
		wrapper.putDouble(z);

		// Yaw position
		wrapper.putDouble(yaw);

		// Pitch position
		wrapper.putDouble(pitch);

		return wrapper.get();
	}

	/**
	 * @return The player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player X coordinate.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The player X coordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return The player Y coordinate.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return The player yaw.
	 */
	public double getYaw() {
		return yaw;
	}

	/**
	 * @return The player pitch.
	 */
	public double getPitch() {
		return pitch;
	}
}
