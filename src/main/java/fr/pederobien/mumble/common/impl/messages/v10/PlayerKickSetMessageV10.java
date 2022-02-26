package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerKickSetMessageV10 extends MumbleMessage {
	private String kickingPlayer, kickedPlayer;

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

		// Player Name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		kickingPlayer = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// Player kick name
		int playerKickedNameLength = wrapper.getInt(first);
		first += 4;
		kickedPlayer = wrapper.getString(first, playerKickedNameLength);
		first += playerKickedNameLength;

		super.setProperties(kickingPlayer, kickedPlayer);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		kickingPlayer = (String) properties[0];
		kickedPlayer = (String) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Kicking player name
		wrapper.putString(kickingPlayer, true);

		// Kicked player name
		wrapper.putString(kickedPlayer, true);

		return wrapper.get();
	}

	/**
	 * @return The kicking player name.
	 */
	public String getKickingPlayer() {
		return kickingPlayer;
	}

	/**
	 * @return The kicked player name.
	 */
	public String getKickedPlayer() {
		return kickedPlayer;
	}
}
