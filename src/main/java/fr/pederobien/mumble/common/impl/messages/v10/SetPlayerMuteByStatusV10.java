package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SetPlayerMuteByStatusV10 extends MumbleMessage {
	private String target, source;
	private boolean isMute;

	/**
	 * Creates a message to set the mute status of a player for another player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerMuteByStatusV10(IMumbleHeader header) {
		super(Identifier.SET_PLAYER_MUTE_BY, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Target player name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		target = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// Source player name
		int playerMutedOrUnmutedNameLength = wrapper.getInt(first);
		first += 4;
		source = wrapper.getString(first, playerMutedOrUnmutedNameLength);
		first += playerMutedOrUnmutedNameLength;

		// Player mute or unmute
		isMute = wrapper.get(first) == 1;

		super.setProperties(target, source, isMute);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		target = (String) properties[0];
		source = (String) properties[1];
		isMute = (boolean) properties[2];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Target player name
		wrapper.putString(target, true);

		// Source player name
		wrapper.putString(source, true);

		// Player's mute status
		wrapper.put((byte) (isMute ? 1 : 0));

		return wrapper.get();
	}

	/**
	 * @return The name of the player to mute or unmute for another player
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return The name of the player for which the target player is mute or unmute.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return True to mute, false to unmute.
	 */
	public boolean isMute() {
		return isMute;
	}
}
