package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.SimplePlayerInfo;
import fr.pederobien.mumble.common.impl.model.ServerInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class ServerInfoGetMessageV10 extends MumbleMessage {
	private ServerInfo serverInfo;

	/**
	 * Creates a message in order to get the server configuration.
	 * 
	 * @param header The message header.
	 */
	protected ServerInfoGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.SERVER_INFO_GET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		serverInfo = new ServerInfo();

		// Number of players
		int numberOfPlayers = (int) wrapper.getInt(first);
		properties.add(numberOfPlayers);
		first += 4;

		for (int i = 0; i < numberOfPlayers; i++) {
			// Player name
			int playerNameLength = wrapper.getInt(first);
			first += 4;
			String playerName = wrapper.getString(first, playerNameLength);
			properties.add(playerName);
			first += playerNameLength;

			// Player's identifier
			int identifierLength = wrapper.getInt(first);
			first += 4;
			UUID identifier = UUID.fromString(wrapper.getString(first, identifierLength));
			properties.add(identifier);
			first += identifierLength;

			// Player's online status
			boolean isOnline = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isOnline);
			first += 4;

			// Player's game address
			int addressLength = wrapper.getInt(first);
			first += 4;
			String gameAddress = wrapper.getString(first, addressLength);
			properties.add(gameAddress);
			first += addressLength;

			// Player's game port
			int gamePort = wrapper.getInt(first);
			properties.add(gamePort);
			first += 4;

			// Player's administrator status
			boolean isAdmin = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isAdmin);
			first += 4;

			// Player's mute status
			boolean isMute = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isMute);
			first += 4;

			// Player's deafen status
			boolean isDeafen = wrapper.getInt(first) == 1 ? true : false;
			properties.add(isDeafen);
			first += 4;

			// Player's x coordinate
			double x = wrapper.getDouble(first);
			properties.add(x);
			first += 8;

			// Player's y coordinate
			double y = wrapper.getDouble(first);
			properties.add(y);
			first += 8;

			// Player's z coordinate
			double z = wrapper.getDouble(first);
			properties.add(z);
			first += 8;

			// Player's yaw angle
			double yaw = wrapper.getDouble(first);
			properties.add(yaw);
			first += 8;

			// Player's pitch angle
			double pitch = wrapper.getDouble(first);
			properties.add(pitch);
			first += 8;

			serverInfo.getPlayerInfo().put(playerName,
					new FullPlayerInfo(playerName, isOnline, identifier, gameAddress, gamePort, isAdmin, isMute, isDeafen, x, y, z, yaw, pitch));
		}

		// Number of modifiers
		int numberOfModifiers = wrapper.getInt(first);
		properties.add(numberOfModifiers);
		first += 4;

		for (int i = 0; i < numberOfModifiers; i++) {
			// Modifier name
			int modifierNameLength = wrapper.getInt(first);
			first += 4;
			String modifierName = wrapper.getString(first, modifierNameLength);
			properties.add(modifierName);
			first += modifierNameLength;

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameters
			int numberOfParameters = wrapper.getInt(first);
			properties.add(numberOfParameters);
			first += 4;

			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				int parameterNameLength = wrapper.getInt(first);
				first += 4;
				String parameterName = wrapper.getString(first, parameterNameLength);
				properties.add(parameterName);
				first += parameterNameLength;

				// Parameter's type
				int code = wrapper.getInt(first);
				first += 4;
				ParameterType<?> type = ParameterType.fromCode(code);
				properties.add(type);

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(defaultValue);
				first += type.size();

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(parameterValue);
				first += type.size();

				// Parameter's range
				boolean isRange = wrapper.getInt(first) == 1;
				properties.add(isRange);
				first += 4;

				Object minValue = null, maxValue = null;
				if (isRange) {
					// Parameter's minimum value
					minValue = type.getValue(wrapper.extract(first, type.size()));
					properties.add(minValue);
					first += type.size();

					// Parameter's maximum value
					maxValue = type.getValue(wrapper.extract(first, type.size()));
					properties.add(maxValue);
					first += type.size();
				}
				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}
			serverInfo.getSoundModifierInfo().put(modifierName, modifierInfo);
		}

		// Number of channels
		int numberOfChannels = wrapper.getInt(first);
		properties.add(numberOfChannels);
		first += 4;

		for (int i = 0; i < numberOfChannels; i++) {
			// Channel's name
			int channelNameLength = wrapper.getInt(first);
			first += 4;
			String channelName = wrapper.getString(first, channelNameLength);
			properties.add(channelName);
			first += channelNameLength;

			// Channel's sound modifier name
			int soundModifierNameLength = wrapper.getInt(first);
			first += 4;
			String modifierName = wrapper.getString(first, soundModifierNameLength);
			properties.add(modifierName);
			first += soundModifierNameLength;

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

			// Number of parameter
			int numberOfParameters = wrapper.getInt(first);
			properties.add(numberOfParameters);
			first += 4;

			for (int j = 0; j < numberOfParameters; j++) {
				// Parameter's name
				int parameterNameLength = wrapper.getInt(first);
				first += 4;
				String parameterName = wrapper.getString(first, parameterNameLength);
				properties.add(parameterName);
				first += parameterNameLength;

				// Parameter's type
				int code = wrapper.getInt(first);
				first += 4;
				ParameterType<?> type = ParameterType.fromCode(code);
				properties.add(type);

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(defaultValue);
				first += type.size();

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(parameterValue);
				first += type.size();

				// Parameter's range
				boolean isRange = wrapper.getInt(first) == 1;
				properties.add(isRange);
				first += 4;

				Object minValue = null, maxValue = null;
				if (isRange) {
					// Parameter's minimum value
					minValue = type.getValue(wrapper.extract(first, type.size()));
					properties.add(minValue);
					first += type.size();

					// Parameter's maximum value
					maxValue = type.getValue(wrapper.extract(first, type.size()));
					properties.add(maxValue);
					first += type.size();
				}
				modifierInfo.getParameterInfo().put(parameterName, new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}

			SemiFullChannelInfo channelInfo = new SemiFullChannelInfo(channelName, modifierInfo);

			// Number of players
			int numberOfChannelPlayers = wrapper.getInt(first);
			properties.add(numberOfChannelPlayers);
			first += 4;

			for (int j = 0; j < numberOfChannelPlayers; j++) {
				// Player's name
				int playerNameLength = wrapper.getInt(first);
				first += 4;
				String playerName = wrapper.getString(first, playerNameLength);
				properties.add(playerName);
				first += playerNameLength;

				channelInfo.getPlayerInfo().put(playerName, new SimplePlayerInfo(playerName));
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

		serverInfo = new ServerInfo();

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
				channelInfo.getPlayerInfo().put(playerName, new SimplePlayerInfo(playerName));
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

			for (SimplePlayerInfo playerInfo : channelInfo.getPlayerInfo().values())
				// Player's name
				wrapper.putString(playerInfo.getName(), true);
		}

		return wrapper.get();
	}

	/**
	 * @return A description that contains a mumble server configuration.
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
}
