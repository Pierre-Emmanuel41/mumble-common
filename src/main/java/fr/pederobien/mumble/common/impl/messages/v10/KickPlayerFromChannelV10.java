package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class KickPlayerFromChannelV10 extends MumbleMessage {
	private String kicked, kicking;

	/**
	 * Creates a message to kick a player from a channel.
	 * 
	 * @param header The message header
	 */
	protected KickPlayerFromChannelV10(IMumbleHeader header) {
		super(MumbleIdentifier.KICK_PLAYER_FROM_CHANNEL, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Kicked player's name
		kicked = wrapper.nextString(wrapper.nextInt());

		// Player kick name
		kicking = wrapper.nextString(wrapper.nextInt());

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
