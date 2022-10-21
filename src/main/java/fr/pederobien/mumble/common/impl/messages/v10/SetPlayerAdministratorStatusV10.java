package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerAdministratorStatusV10 extends MumbleMessage {
	private String name;
	private boolean isAdmin;

	/**
	 * Creates a message in order set the administrator status of a player.
	 * 
	 * @param header The message header.
	 */
	protected SetPlayerAdministratorStatusV10(IMumbleHeader header) {
		super(MumbleIdentifier.SET_PLAYER_ADMINISTRATOR, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		name = wrapper.nextString(wrapper.nextInt());

		// Player's administrator status
		isAdmin = wrapper.nextInt() == 1;

		super.setProperties(name, isAdmin);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		name = (String) properties[0];
		isAdmin = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(name, true);

		// Player's administrator status
		wrapper.putInt(isAdmin ? 1 : 0);

		return wrapper.get();
	}

	/**
	 * @return The player name whose the administrator status has changed.
	 */
	public String getPlayerName() {
		return name;
	}

	/**
	 * @return Whether or not the player is administrator.
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
}
