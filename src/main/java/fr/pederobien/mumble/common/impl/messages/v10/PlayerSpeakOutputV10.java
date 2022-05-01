package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerSpeakOutputV10 extends MumbleMessage {
	private String playerName;
	private byte[] data;
	private double global, left, right;

	/**
	 * Creates a message to play an audio sample on client side.
	 * 
	 * @param header The message header.
	 */
	protected PlayerSpeakOutputV10(IMumbleHeader header) {
		super(Identifier.PLAYER_SPEAK_OUTPUT, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		playerName = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// Player data
		int playerDataLength = wrapper.getInt(first);
		first += 4;
		data = wrapper.extract(first, playerDataLength);
		first += playerDataLength;

		// Global volume
		global = wrapper.getDouble(first);
		first += 8;

		// Left volume
		left = wrapper.getDouble(first);
		first += 8;

		// Right volume
		right = wrapper.getDouble(first);

		super.setProperties(playerName, data, global, left, right);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
		data = (byte[]) properties[1];
		global = (double) properties[2];
		left = (double) properties[3];
		right = (double) properties[4];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player data
		wrapper.put(data, true);

		// Global volume
		wrapper.putDouble(global);

		// Left volume
		wrapper.putDouble(left);

		// Right volume
		wrapper.putDouble(right);

		return wrapper.get();
	}

	/**
	 * @return The name of the speaking player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The bytes array corresponding to one sample.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return The global volume of the sample.
	 */
	public double getGlobal() {
		return global;
	}

	/**
	 * @return The volume of the right channel.
	 */
	public double getRight() {
		return right;
	}

	/**
	 * @return The volume of the left channel.
	 */
	public double getLeft() {
		return left;
	}
}
