package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class AddPlayerToChannelV10 extends MumbleMessage {
	private String channelName;
	private StatusPlayerInfo playerInfo;

	/**
	 * Creates a message to add a player to a channel.
	 * 
	 * @param header The message header.
	 */
	protected AddPlayerToChannelV10(IMumbleHeader header) {
		super(MumbleIdentifier.ADD_PLAYER_TO_CHANNEL, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> informations = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Channel's name
		channelName = wrapper.nextString(wrapper.nextInt());
		informations.add(channelName);

		// Player's name
		String playerName = wrapper.nextString(wrapper.nextInt());
		informations.add(playerName);

		// Player's mute status
		boolean isMute = wrapper.nextInt() == 1;
		informations.add(isMute);

		// Player's deafen status
		boolean isDeafen = wrapper.nextInt() == 1;
		informations.add(isDeafen);

		// Player's muteBy status
		boolean isMuteByMainPlayer = wrapper.nextInt() == 1;
		informations.add(isMuteByMainPlayer);

		playerInfo = new StatusPlayerInfo(playerName, isMute, isDeafen, isMuteByMainPlayer);
		super.setProperties(informations.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		int currentIndex = 0;
		// Channel's name
		channelName = (String) properties[currentIndex++];

		// Player's name
		String playerName = (String) properties[currentIndex++];

		// Player's mute status
		boolean isMute = (boolean) properties[currentIndex++];

		// Player's deafen status
		boolean isDeafen = (boolean) properties[currentIndex++];

		// Player's muteBy status
		boolean isMuteByMainPlayer = (boolean) properties[currentIndex++];

		playerInfo = new StatusPlayerInfo(playerName, isMute, isDeafen, isMuteByMainPlayer);
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Channel's name
		wrapper.putString(channelName, true);

		// Player's name
		wrapper.putString(playerInfo.getName(), true);

		// Player's mute status
		wrapper.putInt(playerInfo.isMute() ? 1 : 0);

		// Player's deafen status
		wrapper.putInt(playerInfo.isDeafen() ? 1 : 0);

		// Player's muteBy status
		wrapper.putInt(playerInfo.isMuteByMainPlayer() ? 1 : 0);

		return wrapper.get();
	}

	/**
	 * @return The name of channel to which a player is added.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return The description of the player to add.
	 */
	public StatusPlayerInfo getPlayerInfo() {
		return playerInfo;
	}
}
