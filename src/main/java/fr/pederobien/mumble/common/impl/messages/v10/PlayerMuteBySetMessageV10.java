package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerMuteBySetMessageV10 extends MumbleMessage {
	private String mutingPlayer, mutedPlayer;
	private boolean isMute;

	/**
	 * Creates a message to mute a player for another player.
	 * 
	 * @param header The message header.
	 */
	public PlayerMuteBySetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_MUTE_BY_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Muting player name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		mutingPlayer = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// Muted player name
		int playerMutedOrUnmutedNameLength = wrapper.getInt(first);
		first += 4;
		mutedPlayer = wrapper.getString(first, playerMutedOrUnmutedNameLength);
		first += playerMutedOrUnmutedNameLength;

		// Player mute or unmute
		isMute = wrapper.get(first) == 1;

		super.setProperties(mutingPlayer, mutedPlayer, isMute);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		mutingPlayer = (String) properties[0];
		mutedPlayer = (String) properties[1];
		isMute = (boolean) properties[2];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player name
		wrapper.putString(mutingPlayer, true);

		// Player muted/unmuted name
		wrapper.putString(mutedPlayer, true);

		// Player mute or unmute
		wrapper.put((byte) (isMute ? 1 : 0));

		return wrapper.get();
	}

	/**
	 * @return The muting player name.
	 */
	public String getMutingPlayer() {
		return mutingPlayer;
	}

	/**
	 * @return The muted player name.
	 */
	public String getMutedPlayer() {
		return mutedPlayer;
	}

	/**
	 * @return True to mute, false to unmute.
	 */
	public boolean isMute() {
		return isMute;
	}
}
