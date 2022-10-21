package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetChannelNameV10 extends MumbleMessage {
	private String oldName, newName;

	/**
	 * Creates a message in order to set the name of a channel.
	 * 
	 * @param header The message header.
	 */
	protected SetChannelNameV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_CHANNEL_NAME, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Old channel name
		oldName = wrapper.nextString(wrapper.nextInt());

		// New channel name
		newName = wrapper.nextString(wrapper.nextInt());

		super.setProperties(oldName, newName);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Old channel name
		oldName = (String) properties[0];

		// New channel name
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
