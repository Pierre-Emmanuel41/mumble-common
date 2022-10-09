package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.ServerInfo.FullServerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetFullServerConfigurationV10 extends MumbleMessage {
	private FullServerInfo serverInfo;

	/**
	 * Creates a message in order to get the full configuration of a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected GetFullServerConfigurationV10(IMumbleHeader header) {
		super(Identifier.GET_FULL_SERVER_CONFIGURATION, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		serverInfo = new FullServerInfo();

		// Number of players
		int numberOfPlayers = (int) wrapper.nextInt();
		properties.add(numberOfPlayers);

		for (int i = 0; i < numberOfPlayers; i++) {
			// Player name
			String playerName = wrapper.nextString(wrapper.nextInt());
			properties.add(playerName);

			// Player's identifier
			UUID identifier = UUID.fromString(wrapper.nextString(wrapper.nextInt()));
			properties.add(identifier);

			// Player's online status
			boolean isOnline = wrapper.nextInt() == 1 ? true : false;
			properties.add(isOnline);

			// Player's game address
			String gameAddress = wrapper.nextString(wrapper.nextInt());
			properties.add(gameAddress);

			// Player's game port
			int gamePort = wrapper.nextInt();
			properties.add(gamePort);

			// Player's administrator status
			boolean isAdmin = wrapper.nextInt() == 1 ? true : false;
			properties.add(isAdmin);

			// Player's mute status
			boolean isMute = wrapper.nextInt() == 1 ? true : false;
			properties.add(isMute);

			// Player's deafen status
			boolean isDeafen = wrapper.nextInt() == 1 ? true : false;
			properties.add(isDeafen);

			// Player's x coordinate
			double x = wrapper.nextDouble();
			properties.add(x);

			// Player's y coordinate
			double y = wrapper.nextDouble();
			properties.add(y);

			// Player's z coordinate
			double z = wrapper.nextDouble();
			properties.add(z);

			// Player's yaw angle
			double yaw = wrapper.nextDouble();
			properties.add(yaw);

			// Player's pitch angle
			double pitch = wrapper.nextDouble();
			properties.add(pitch);

			serverInfo.getPlayerInfo().put(playerName,
					new FullPlayerInfo(playerName, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch));
		}

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
				boolean isMute = wrapper.nextInt() == 1;
				properties.add(isMute);

				// Player's deafen status
				boolean isDeafen = wrapper.nextInt() == 1;
				properties.add(isDeafen);

				// Player's muteBy status
				boolean isMuteByMainPlayer = wrapper.nextInt() == 1;
				properties.add(isMuteByMainPlayer);

				channelInfo.getPlayerInfo().put(playerName, new StatusPlayerInfo(playerName, isMute, isDeafen, isMuteByMainPlayer));
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

		serverInfo = new FullServerInfo();

		int currentIndex = 0;

		// Number of players
		int numberOfPlayers = (int) properties[currentIndex++];

		for (int i = 0; i < numberOfPlayers; i++) {
			// Player name
			String playerName = (String) properties[currentIndex++];

			// Player's identifier
			UUID identifier = (UUID) properties[currentIndex++];

			// Player's online status
			boolean isOnline = (boolean) properties[currentIndex++];

			// Player's game address
			String gameAddress = (String) properties[currentIndex++];

			// Player's game port
			int gamePort = (int) properties[currentIndex++];

			// Player's administrator status
			boolean isAdmin = (boolean) properties[currentIndex++];

			// Player's mute status
			boolean isMute = (boolean) properties[currentIndex++];

			// Player's deafen status
			boolean isDeafen = (boolean) properties[currentIndex++];

			// Player's x coordinate
			double x = (double) properties[currentIndex++];

			// Player's y coordinate
			double y = (double) properties[currentIndex++];

			// Player's z coordinate
			double z = (double) properties[currentIndex++];

			// Player's yaw angle
			double yaw = (double) properties[currentIndex++];

			// Player's pitch angle
			double pitch = (double) properties[currentIndex++];

			serverInfo.getPlayerInfo().put(playerName,
					new FullPlayerInfo(playerName, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch));
		}

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

				// Parameter's value
				Object value = (Object) properties[currentIndex++];

				// Parameter's default value
				Object defaultValue = (Object) properties[currentIndex++];

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

				// Parameter's value
				Object value = (Object) properties[currentIndex++];

				// Parameter's default value
				Object defaultValue = (Object) properties[currentIndex++];

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

		// Number of players
		wrapper.putInt(serverInfo.getPlayerInfo().size());

		for (FullPlayerInfo playerInfo : serverInfo.getPlayerInfo().values()) {
			// Player's name
			wrapper.putString(playerInfo.getName(), true);

			// Player's identifier
			wrapper.putString(playerInfo.getIdentifier().toString(), true);

			// Player's online status
			wrapper.putInt(playerInfo.isOnline() ? 1 : 0);

			// Player's game address
			wrapper.putString(playerInfo.getGameAddress().getAddress().getHostAddress(), true);

			// Player's game port
			wrapper.putInt(playerInfo.getGameAddress().getPort());

			// Player's administrator status
			wrapper.putInt(playerInfo.isAdmin() ? 1 : 0);

			// Player's mute status
			wrapper.putInt(playerInfo.isMute() ? 1 : 0);

			// Player's deafen status
			wrapper.putInt(playerInfo.isDeafen() ? 1 : 0);

			// Player's x coordinate
			wrapper.putDouble(playerInfo.getX());

			// Player's y coordinate
			wrapper.putDouble(playerInfo.getY());

			// Player's z coordinate
			wrapper.putDouble(playerInfo.getZ());

			// Player's yaw angle
			wrapper.putDouble(playerInfo.getYaw());

			// Player's pitch
			wrapper.putDouble(playerInfo.getPitch());
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

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));

				// Parameter's default value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getDefaultValue()));

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

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));

				// Parameter's default value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getDefaultValue()));

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
	public FullServerInfo getServerInfo() {
		return serverInfo;
	}
}
