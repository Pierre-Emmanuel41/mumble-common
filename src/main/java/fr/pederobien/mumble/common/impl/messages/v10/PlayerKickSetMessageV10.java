package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerKickSetMessageV10 extends MumbleMessage {
	private String kicked, kicking;

	/**
	 * Creates a message to kick a player from a channel.
	 * 
	 * @param header The message header
	 */
	protected PlayerKickSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_KICK_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Kicked player's name
		int kickedPlayerNameLength = wrapper.getInt(first);
		first += 4;
		kicked = wrapper.getString(first, kickedPlayerNameLength);
		first += kickedPlayerNameLength;

		// Player kick name
		int kickingPlayerNameLength = wrapper.getInt(first);
		first += 4;
		kicking = wrapper.getString(first, kickingPlayerNameLength);
		first += kickingPlayerNameLength;

		super.setProperties(kicked, kicking);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		kicked = (String) properties[0];
		kicking = (String) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Kicked player name
		wrapper.putString(kicked, true);

		// Kicking player name
		wrapper.putString(kicking, true);

		return wrapper.get();
	}

	/**
	 * @return The kicked player name.
	 */
	public String getKicked() {
		return kicked;
	}

	/**
	 * @return The kicking player name.
	 */
	public String getKicking() {
		return kicking;
	}
}
