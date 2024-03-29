package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class UnregisterPlayerFromServerV10 extends MumbleMessage {
	private String playerName;

	/**
	 * Creates a message in order to unregister a player from a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected UnregisterPlayerFromServerV10(IMumbleHeader header) {
		super(MumbleIdentifier.UNREGISTER_PLAYER_FROM_SERVER, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		playerName = wrapper.nextString(wrapper.nextInt());

		super.setProperties(playerName);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		playerName = (String) properties[0];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Payer's name
		wrapper.putString(playerName, true);

		return wrapper.get();
	}

	/**
	 * @return The name of the player to remove.
	 */
	public String getPlayerName() {
		return playerName;
	}
}
