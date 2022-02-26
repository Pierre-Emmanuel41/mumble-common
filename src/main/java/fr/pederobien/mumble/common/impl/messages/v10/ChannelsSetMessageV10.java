package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class ChannelsSetMessageV10 extends MumbleMessage {
	private String oldName, newName;

	/**
	 * Creates a message to change the name of a channel.
	 * 
	 * @param header The message header.
	 */
	protected ChannelsSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.CHANNELS_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Old channel name
		int oldChannelNameLength = wrapper.getInt(first);
		first += 4;
		oldName = wrapper.getString(first, oldChannelNameLength);
		first += oldChannelNameLength;

		// New channel name
		int newChannelNameLength = wrapper.getInt(first);
		first += 4;
		newName = wrapper.getString(first, newChannelNameLength);

		super.setProperties(oldName, newName);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		oldName = (String) properties[0];
		newName = (String) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		if (getHeader().isError())
			return new byte[0];

		return ByteWrapper.create().putString(oldName, true).putString(newName, true).get();
	}

	/**
	 * @return The name of the channel to rename.
	 */
	public String getOldName() {
		return oldName;
	}

	/**
	 * @return The new channel name.
	 */
	public String getNewName() {
		return newName;
	}
}
