package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerInfoGetMessageV10 extends MumbleMessage {
	private String playerName;
	private boolean isOnline, isAdmin;

	/**
	 * Creates a message in order to get information about a specific player.
	 * 
	 * @param header The message header.
	 */
	protected PlayerInfoGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_INFO_GET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Player connected
		isOnline = wrapper.getInt(first) == 1;
		properties.add(isOnline);
		first += 4;

		if (isOnline) {
			// Player name
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			playerName = wrapper.getString(first, playerNameLength);
			properties.add(playerName);
			first += playerNameLength;

			// Player admin
			isAdmin = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isAdmin);
		}

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		isOnline = (boolean) properties[0];
		if (isOnline) {
			playerName = (String) properties[1];
			isAdmin = (boolean) properties[2];
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Player connected
		if (isOnline) {
			// Player online
			wrapper.putInt(1);

			// Player name
			wrapper.putString(playerName, true);

			// Player admin
			wrapper.putInt(isAdmin ? 1 : 0);
		} else
			wrapper.putInt(0);

		return wrapper.get();
	}

	/**
	 * @return The player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player online status.
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * @return The player admin status.
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
}
