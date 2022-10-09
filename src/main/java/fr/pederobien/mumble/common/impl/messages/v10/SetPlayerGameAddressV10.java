package fr.pederobien.mumble.common.impl.messages.v10;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetPlayerGameAddressV10 extends MumbleMessage {
	private String playerName;
	private InetSocketAddress gameAddress;

	protected SetPlayerGameAddressV10(IMumbleHeader header) {
		super(Identifier.SET_PLAYER_GAME_ADDRESS, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Player's name
		playerName = wrapper.nextString(wrapper.nextInt());
		properties.add(playerName);

		// Player's game address
		String address = wrapper.nextString(wrapper.nextInt());
		properties.add(address);

		// Player's game port
		int port = wrapper.nextInt();
		properties.add(port);

		try {
			gameAddress = new InetSocketAddress(InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Player's name
		playerName = (String) properties[0];

		// Player's online status
		String address = (String) properties[1];

		// Player's port number
		int port = (int) properties[2];

		try {
			gameAddress = new InetSocketAddress(InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Player's name
		wrapper.putString(playerName, true);

		// Player's game address
		wrapper.putString(gameAddress.getAddress().getHostAddress(), true);

		// Player's online status
		wrapper.putInt(gameAddress.getPort());

		return wrapper.get();
	}

	/**
	 * @return The player's name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The player's address used to play to the game.
	 */
	public InetSocketAddress getGameAddress() {
		return gameAddress;
	}
}
