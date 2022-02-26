package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerAdminSetMessageV10 extends MumbleMessage {
	private String name;
	private boolean isAdmin;

	/**
	 * Creates a message in order set the admin status of a player.
	 * 
	 * @param header The message header.
	 */
	protected PlayerAdminSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_ADMIN_SET, header);
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

		// Player's admin
		isAdmin = wrapper.getInt(first) == 1;

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

		// Player's admin
		wrapper.putInt(isAdmin ? 1 : 0);

		return wrapper.get();
	}

	/**
	 * @return The player name whose the admin status has changed.
	 */
	public String getPlayerName() {
		return name;
	}

	/**
	 * @return whether or not the player is admin.
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
}
