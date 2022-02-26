package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class ChannelsRemoveMessageV10 extends MumbleMessage {
	private String channelName;

	/**
	 * Creates a message to remove a channel from a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected ChannelsRemoveMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.CHANNELS_REMOVE, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		channelName = ByteWrapper.wrap(payload).getString();
		super.setProperties(channelName);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		channelName = (String) properties[0];
	}

	@Override
	protected byte[] generateProperties() {
		if (getHeader().isError())
			return new byte[0];

		return ByteWrapper.create().putString(channelName).get();
	}

	/**
	 * @return The channel name to remove.
	 */
	public String getChannelName() {
		return channelName;
	}
}
