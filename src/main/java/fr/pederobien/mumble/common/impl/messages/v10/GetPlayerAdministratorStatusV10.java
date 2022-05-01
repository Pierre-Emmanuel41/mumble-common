package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GetPlayerAdministratorStatusV10 extends MumbleMessage {
	private String name;
	private boolean isAdmin;

	/**
	 * Creates a message in order get the administrator status of a player.
	 * 
	 * @param header The message header.
	 */
	protected GetPlayerAdministratorStatusV10(IMumbleHeader header) {
		super(Identifier.GET_PLAYER_ADMINISTRATOR, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player's name
		int playerNameLength = wrapper.getInt(first);
		first += 4;
		name = wrapper.getString(first, playerNameLength);
		first += playerNameLength;

		// When it is an answer
		if (payload.length > 1)
			// Player's administrator status
			isAdmin = wrapper.getInt(first) == 1;

		super.setProperties(name, isAdmin);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Player's name
		name = (String) properties[0];

		// When it is an answer
		if (properties.length > 1)
			// Player's administrator status
			isAdmin = (boolean) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(name, true);

		// When it is an answer
		if (getProperties().length > 1)
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
