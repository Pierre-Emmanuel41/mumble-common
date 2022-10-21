package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.ServerInfo.SimpleServerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetServerConfigurationV10 extends MumbleMessage {
	private SimpleServerInfo serverInfo;

	/**
	 * Creates a message in order to get the full configuration of a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected GetServerConfigurationV10(IMumbleHeader header) {
		super(MumbleIdentifier.GET_SERVER_CONFIGURATION, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Vocal server's port number
		int vocalPort = wrapper.nextInt();
		properties.add(vocalPort);

		// Player's online status
		boolean isOnline = wrapper.nextInt() == 1 ? true : false;
		properties.add(isOnline);

		String name = null, gameAddress = null;
		UUID identifier = null;
		int gamePort = 0;
		boolean isAdmin = false, isMute = false, isDeafen = false;
		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;

		if (isOnline) {
			// Player name
			name = wrapper.nextString(wrapper.nextInt());
			properties.add(name);

			// Player's identifier
			identifier = UUID.fromString(wrapper.nextString(wrapper.nextInt()));
			properties.add(identifier);

			// Player's game address
			gameAddress = wrapper.nextString(wrapper.nextInt());
			properties.add(gameAddress);

			// Player's game port
			gamePort = wrapper.nextInt();
			properties.add(gamePort);

			// Player's administrator status
			isAdmin = wrapper.nextInt() == 1 ? true : false;
			properties.add(isAdmin);

			// Player's mute status
			isMute = wrapper.nextInt() == 1 ? true : false;
			properties.add(isMute);

			// Player's deafen status
			isDeafen = wrapper.nextInt() == 1 ? true : false;
			properties.add(isDeafen);

			// Player's x coordinate
			x = wrapper.nextDouble();
			properties.add(x);

			// Player's y coordinate
			y = wrapper.nextDouble();
			properties.add(y);

			// Player's z coordinate
			z = wrapper.nextDouble();
			properties.add(z);

			// Player's yaw angle
			yaw = wrapper.nextDouble();
			properties.add(yaw);

			// Player's pitch angle
			pitch = wrapper.nextDouble();
			properties.add(pitch);
		}

		FullPlayerInfo playerInfo = new FullPlayerInfo(name, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
		serverInfo = new SimpleServerInfo(playerInfo, vocalPort);

		// Number of modifiers
		int numberOfModifiers = wrapper.nextInt();
		properties.add(numberOfModifiers);

		for (int i = 0; i < numberOfModifiers; i++) {
			// Modifier name
			String modifierName = wrapper.nextString(wrapper.nextInt());
			properties.add(modifierName);

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameters
			int numberOfParameters = wrapper.nextInt();
			properties.add(numberOfParameters);

			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				String parameterName = wrapper.nextString(wrapper.nextInt());
				properties.add(parameterName);

				// Parameter's type
				ParameterType<?> type = ParameterType.fromCode(wrapper.nextInt());
				properties.add(type);

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.next(type.size()));
				properties.add(defaultValue);

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.next(type.size()));
				properties.add(parameterValue);

				// Parameter's range
				boolean isRange = wrapper.nextInt() == 1;
				properties.add(isRange);

				Object minValue = null, maxValue = null;
				if (isRange) {
					// Parameter's minimum value
					minValue = type.getValue(wrapper.next(type.size()));
					properties.add(minValue);

					// Parameter's maximum value
					maxValue = type.getValue(wrapper.next(type.size()));
					properties.add(maxValue);
				}
				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}
			serverInfo.getSoundModifierInfo().put(modifierName, modifierInfo);
		}

		// Number of channels
		int numberOfChannels = wrapper.nextInt();
		properties.add(numberOfChannels);

		for (int i = 0; i < numberOfChannels; i++) {
			// Channel's name
			String channelName = wrapper.nextString(wrapper.nextInt());
			properties.add(channelName);

			// Channel's sound modifier name
			String modifierName = wrapper.nextString(wrapper.nextInt());
			properties.add(modifierName);

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameter
			int numberOfParameters = wrapper.nextInt();
			properties.add(numberOfParameters);

			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				String parameterName = wrapper.nextString(wrapper.nextInt());
				properties.add(parameterName);

				// Parameter's type
				ParameterType<?> type = ParameterType.fromCode(wrapper.nextInt());
				properties.add(type);

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.next(type.size()));
				properties.add(defaultValue);

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.next(type.size()));
				properties.add(parameterValue);

				// Parameter's range
				boolean isRange = wrapper.nextInt() == 1;
				properties.add(isRange);

				Object minValue = null, maxValue = null;
				if (isRange) {
					// Parameter's minimum value
					minValue = type.getValue(wrapper.next(type.size()));
					properties.add(minValue);

					// Parameter's maximum value
					maxValue = type.getValue(wrapper.next(type.size()));
					properties.add(maxValue);
				}
				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}

			SemiFullChannelInfo channelInfo = new SemiFullChannelInfo(channelName, modifierInfo);

			// Number of players
			int numberOfChannelPlayers = wrapper.nextInt();
			properties.add(numberOfChannelPlayers);

			for (int j = 0; j < numberOfChannelPlayers; j++) {
				// Player's name
				String playerName = wrapper.nextString(wrapper.nextInt());
				properties.add(playerName);

				// Player's mute status
				boolean playerMute = wrapper.nextInt() == 1;
				properties.add(playerMute);

				// Player's deafen status
				boolean playerDeafen = wrapper.nextInt() == 1;
				properties.add(playerDeafen);

				// Player's muteBy status
				boolean isMuteByMainPlayer = wrapper.nextInt() == 1;
				properties.add(isMuteByMainPlayer);

				channelInfo.getPlayerInfo().put(playerName, new StatusPlayerInfo(playerName, playerMute, playerDeafen, isMuteByMainPlayer));
			}

			serverInfo.getChannelInfo().put(channelName, channelInfo);
		}

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (properties.length == 0 || getHeader().isError())
			return;

		int currentIndex = 0;

		// Vocal server's port number
		int vocalPort = (int) properties[currentIndex++];

		// Player's online status
		boolean isOnline = (boolean) properties[currentIndex++];

		String name = null, gameAddress = null;
		UUID identifier = null;
		int gamePort = 0;
		boolean isAdmin = false, isMute = false, isDeafen = false;
		double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;

		if (isOnline) {
			// Player name
			name = (String) properties[currentIndex++];

			// Player's identifier
			identifier = (UUID) properties[currentIndex++];

			// Player's game address
			gameAddress = (String) properties[currentIndex++];

			// Player's game port
			gamePort = (int) properties[currentIndex++];

			// Player's administrator status
			isAdmin = (boolean) properties[currentIndex++];

			// Player's mute status
			isMute = (boolean) properties[currentIndex++];

			// Player's deafen status
			isDeafen = (boolean) properties[currentIndex++];

			// Player's x coordinate
			x = (double) properties[currentIndex++];

			// Player's y coordinate
			y = (double) properties[currentIndex++];

			// Player's z coordinate
			z = (double) properties[currentIndex++];

			// Player's yaw angle
			yaw = (double) properties[currentIndex++];

			// Player's pitch angle
			pitch = (double) properties[currentIndex++];
		}

		FullPlayerInfo playerInfo = new FullPlayerInfo(name, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch);
		serverInfo = new SimpleServerInfo(playerInfo, vocalPort);

		// Number of sound modifiers
		int numberOfModifiers = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfModifiers; i++) {
			// Sound modifier's name
			String modifierName = (String) properties[currentIndex++];

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameters
			int numberOfParameters = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				String parameterName = (String) properties[currentIndex++];

				// Parameter's type
				ParameterType<?> type = (ParameterType<?>) properties[currentIndex++];

				// Parameter's default value
				Object defaultValue = (Object) properties[currentIndex++];

				// Parameter's value
				Object value = (Object) properties[currentIndex++];

				// Parameter's range
				boolean isRange = (boolean) properties[currentIndex++];

				Object min = null, max = null;
				if (isRange) {
					// Parameter's minimum value
					min = (Object) properties[currentIndex++];

					// Parameter's maximum value
					max = (Object) properties[currentIndex++];
				}

				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, value, defaultValue, isRange, min, max));
			}
			serverInfo.getSoundModifierInfo().put(modifierName, modifierInfo);
		}

		// Number of channels
		int numberOfChannels = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfChannels; i++) {
			// Channel's name
			String channelName = (String) properties[currentIndex++];

			// Sound modifier's name
			String modifierName = (String) properties[currentIndex++];

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameters
			int numberOfParameters = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				String parameterName = (String) properties[currentIndex++];

				// Parameter's type
				ParameterType<?> type = (ParameterType<?>) properties[currentIndex++];

				// Parameter's default value
				Object defaultValue = (Object) properties[currentIndex++];

				// Parameter's value
				Object value = (Object) properties[currentIndex++];

				// Parameter's range
				boolean isRange = (boolean) properties[currentIndex++];

				Object min = null, max = null;
				if (isRange) {
					// Parameter's minimum value
					min = (Object) properties[currentIndex++];

					// Parameter's maximum value
					max = (Object) properties[currentIndex++];
				}

				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, value, defaultValue, isRange, min, max));
			}

			SemiFullChannelInfo channelInfo = new SemiFullChannelInfo(channelName, modifierInfo);

			// Number of players registered in the channel
			int numberOfChannelsPlayers = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfChannelsPlayers; j++) {
				// Player's name
				String playerName = (String) properties[currentIndex++];

				// Player's mute status
				boolean playerMute = (boolean) properties[currentIndex++];

				// Player's deafen status
				boolean playerDeafen = (boolean) properties[currentIndex++];

				// Player's muteBy status
				boolean isMuteByMainPlayer = (boolean) properties[currentIndex++];

				channelInfo.getPlayerInfo().put(playerName, new StatusPlayerInfo(playerName, playerMute, playerDeafen, isMuteByMainPlayer));
			}

			serverInfo.getChannelInfo().put(channelName, channelInfo);
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Vocal server's port number
		wrapper.putInt(serverInfo.getVocalPort());

		// Player's online status
		wrapper.putInt(serverInfo.getPlayerInfo().isOnline() ? 1 : 0);

		if (serverInfo.getPlayerInfo().isOnline()) {
			// Player's name
			wrapper.putString(serverInfo.getPlayerInfo().getName(), true);

			// Player's identifier
			wrapper.putString(serverInfo.getPlayerInfo().getIdentifier().toString(), true);

			// Player's game address
			wrapper.putString(serverInfo.getPlayerInfo().getGameAddress().getAddress().getHostAddress(), true);

			// Player's game port
			wrapper.putInt(serverInfo.getPlayerInfo().getGameAddress().getPort());

			// Player's administrator status
			wrapper.putInt(serverInfo.getPlayerInfo().isAdmin() ? 1 : 0);

			// Player's mute status
			wrapper.putInt(serverInfo.getPlayerInfo().isMute() ? 1 : 0);

			// Player's deafen status
			wrapper.putInt(serverInfo.getPlayerInfo().isDeafen() ? 1 : 0);

			// Player's x coordinate
			wrapper.putDouble(serverInfo.getPlayerInfo().getX());

			// Player's y coordinate
			wrapper.putDouble(serverInfo.getPlayerInfo().getY());

			// Player's z coordinate
			wrapper.putDouble(serverInfo.getPlayerInfo().getZ());

			// Player's yaw angle
			wrapper.putDouble(serverInfo.getPlayerInfo().getYaw());

			// Player's pitch
			wrapper.putDouble(serverInfo.getPlayerInfo().getPitch());
		}

		// Number of sound modifier
		wrapper.putInt(serverInfo.getSoundModifierInfo().size());

		for (FullSoundModifierInfo modifierInfo : serverInfo.getSoundModifierInfo().values()) {
			// Modifier's name
			wrapper.putString(modifierInfo.getName(), true);

			// Number of parameter
			wrapper.putInt(modifierInfo.getParameterInfo().size());

			for (FullParameterInfo parameterInfo : modifierInfo.getParameterInfo().values()) {
				// Parameter's name
				wrapper.putString(parameterInfo.getName(), true);

				// Parameter's type
				wrapper.putInt(parameterInfo.getType().getCode());

				// Parameter's default value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getDefaultValue()));

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));

				// Parameter's range
				wrapper.putInt(parameterInfo.isRange() ? 1 : 0);

				if (parameterInfo.isRange()) {
					// Parameter's minimum value
					wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getMinValue()));

					// Parameter's maximum value
					wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getMaxValue()));
				}
			}
		}

		// Number of channels
		wrapper.putInt(serverInfo.getChannelInfo().size());

		for (SemiFullChannelInfo channelInfo : serverInfo.getChannelInfo().values()) {
			// Channel's name
			wrapper.putString(channelInfo.getName(), true);

			// Channel's sound modifier name
			wrapper.putString(channelInfo.getSoundModifierInfo().getName(), true);

			// Number of parameter
			wrapper.putInt(channelInfo.getSoundModifierInfo().getParameterInfo().size());

			for (FullParameterInfo parameterInfo : channelInfo.getSoundModifierInfo().getParameterInfo().values()) {
				// Parameter's name
				wrapper.putString(parameterInfo.getName(), true);

				// Parameter's type
				wrapper.putInt(parameterInfo.getType().getCode());

				// Parameter's default value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getDefaultValue()));

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));

				// Parameter's range
				wrapper.putInt(parameterInfo.isRange() ? 1 : 0);

				if (parameterInfo.isRange()) {
					// Parameter's minimum value
					wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getMinValue()));

					// Parameter's maximum value
					wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getMaxValue()));
				}
			}

			// Number of players
			wrapper.putInt(channelInfo.getPlayerInfo().size());

			for (StatusPlayerInfo playerInfo : channelInfo.getPlayerInfo().values()) {
				// Player's name
				wrapper.putString(playerInfo.getName(), true);

				// Player's mute status
				wrapper.putInt(playerInfo.isMute() ? 1 : 0);

				// Player's deafen status
				wrapper.putInt(playerInfo.isDeafen() ? 1 : 0);

				// Player's muteBy status
				wrapper.putInt(playerInfo.isMuteByMainPlayer() ? 1 : 0);
			}
		}

		return wrapper.get();
	}

	/**
	 * @return A description that contains a mumble server configuration.
	 */
	public SimpleServerInfo getServerInfo() {
		return serverInfo;
	}
}
