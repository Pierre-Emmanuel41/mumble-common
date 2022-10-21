package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerNameV10 extends MumbleMessage {
	private String oldName, newName;

	/**
	 * Creates a message in order to set the name of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerNameV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_PLAYER_NAME, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Old player's name
		oldName = wrapper.nextString(wrapper.nextInt());

		// New player's name
		newName = wrapper.nextString(wrapper.nextInt());

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
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Old player's name
		wrapper.putString(oldName, true);

		// New player's name
		wrapper.putString(newName, true);

		return wrapper.get();
	}

	/**
	 * @return The name of the player to rename.
	 */
	public String getOldName() {
		return oldName;
	}

	/**
	 * @return The new player name.
	 */
	public String getNewName() {
		return newName;
	}
}
