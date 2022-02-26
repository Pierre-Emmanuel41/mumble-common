package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ChannelInfo.SimpleChannelInfo;
import fr.pederobien.mumble.common.impl.model.ClientInfo.FullClientInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.LazyParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.SimplePlayerInfo;
import fr.pederobien.mumble.common.impl.model.ServerInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.LazySoundModifierInfo;
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

		// Number of clients
		int numberOfClients = (int) wrapper.getInt(first);
		properties.add(numberOfClients);
		first += 4;

		for (int i = 0; i < numberOfClients; i++) {
			// Client identifier
			int identifierLength = wrapper.getInt(first);
			first += 4;
			UUID identifier = UUID.fromString(wrapper.getString(first, identifierLength));
			properties.add(identifier);
			first += identifierLength;

			// Player's mumble connected status
			boolean isMumbleConnected = wrapper.getInt(first) == 1;
			properties.add(isMumbleConnected);
			first += 4;

			String mumbleAddress = null;
			int mumblePort = 0;

			if (isMumbleConnected) {
				// Client's mumble address
				int addressLength = wrapper.getInt(first);
				first += 4;
				mumbleAddress = wrapper.getString(first, addressLength);
				properties.add(mumbleAddress);
				first += addressLength;

				// Client's mumble port
				mumblePort = wrapper.getInt(first);
				properties.add(mumblePort);
				first += 4;
			}

			// Player's online status
			boolean isOnline = wrapper.getInt(first) == 1;
			properties.add(isOnline);
			first += 4;

			String playerName = null, gameAddress = null;
			boolean isAdmin = false, isMute = false, isDeafen = false;
			double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;
			int gamePort = 0;

			if (isOnline) {
				// Client's game address
				int addressLength = wrapper.getInt(first);
				first += 4;
				gameAddress = wrapper.getString(first, addressLength);
				properties.add(mumbleAddress);
				first += addressLength;

				// Client's game port
				gamePort = wrapper.getInt(first);
				properties.add(mumblePort);
				first += 4;

				// Player's name
				int playerNameLength = wrapper.getInt(first);
				first += 4;
				playerName = wrapper.getString(first, playerNameLength);
				properties.add(playerName);
				first += playerNameLength;

				// Player's admin status
				isAdmin = wrapper.getInt(first) == 1;
				properties.add(isAdmin);
				first += 4;

				// Player's mute status
				isMute = wrapper.getInt(first) == 1;
				properties.add(isMute);
				first += 4;

				// Player's deafen status
				isDeafen = wrapper.getInt(first) == 1;
				properties.add(isDeafen);
				first += 4;

				// X position
				x = wrapper.getDouble(first);
				properties.add(x);
				first += 8;

				// Y position
				y = wrapper.getDouble(first);
				properties.add(y);
				first += 8;

				// Z position
				z = wrapper.getDouble(first);
				properties.add(z);
				first += 8;

				// Yaw position
				yaw = wrapper.getDouble(first);
				properties.add(yaw);
				first += 8;

				// Pitch position
				pitch = wrapper.getDouble(first);
				properties.add(pitch);
				first += 8;

			}

			serverInfo.getClientInfo().add(new FullClientInfo(identifier, isMumbleConnected, mumbleAddress, mumblePort, isOnline, gameAddress, gamePort, playerName,
					isAdmin, isMute, isDeafen, x, y, z, yaw, pitch));
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

				boolean isRange = wrapper.getInt(first) == 1;
				properties.add(isRange);
				first += 4;

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(defaultValue);
				first += type.size();

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(parameterValue);
				first += type.size();

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
				modifierInfo.getParameterInfo().add(new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}
			serverInfo.getSoundModifierInfo().add(modifierInfo);
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

			LazySoundModifierInfo soundModifierInfo = new LazySoundModifierInfo(modifierName);

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

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.extract(first, type.size()));
				properties.add(parameterValue);
				first += type.size();

				soundModifierInfo.getParameterInfo().add(new LazyParameterInfo(parameterName, type, parameterValue));
			}

			SimpleChannelInfo channelInfo = new SimpleChannelInfo(channelName, soundModifierInfo);

			// Number of players
			int numberOfPlayers = wrapper.getInt(first);
			properties.add(numberOfPlayers);
			first += 4;

			for (int j = 0; j < numberOfPlayers; j++) {
				// Player's name
				int playerNameLength = wrapper.getInt(first);
				first += 4;
				String playerName = wrapper.getString(first, playerNameLength);
				properties.add(playerName);
				first += playerNameLength;

				channelInfo.getPlayerInfo().add(new SimplePlayerInfo(playerName));
			}

			serverInfo.getChannelInfo().add(channelInfo);
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

		int numberOfClients = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfClients; i++) {
			UUID identifier = (UUID) properties[currentIndex++];

			boolean isMumbleConnected = (boolean) properties[currentIndex++];
			String mumbleAddress = null;
			int mumblePort = 0;

			if (isMumbleConnected) {
				mumbleAddress = (String) properties[currentIndex++];
				mumblePort = (int) properties[currentIndex++];
			}

			boolean isOnline = (boolean) properties[currentIndex++];
			String playerName = null, gameAddress = null;
			boolean isAdmin = false, isMute = false, isDeafen = false;
			double x = 0, y = 0, z = 0, yaw = 0, pitch = 0;
			int gamePort = 0;

			if (isOnline) {
				gameAddress = (String) properties[currentIndex++];
				gamePort = (int) properties[currentIndex++];
				playerName = (String) properties[currentIndex++];
				isAdmin = (boolean) properties[currentIndex++];
				isMute = (boolean) properties[currentIndex++];
				isDeafen = (boolean) properties[currentIndex++];
				x = (double) properties[currentIndex++];
				y = (double) properties[currentIndex++];
				z = (double) properties[currentIndex++];
				yaw = (double) properties[currentIndex++];
				pitch = (double) properties[currentIndex++];
			}

			serverInfo.getClientInfo().add(new FullClientInfo(identifier, isMumbleConnected, mumbleAddress, mumblePort, isOnline, gameAddress, gamePort, playerName,
					isAdmin, isMute, isDeafen, x, y, z, yaw, pitch));
		}

		int numberOfModifiers = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfModifiers; i++) {
			String modifierName = (String) properties[currentIndex++];
			FullSoundModifierInfo soundModifierInfo = new FullSoundModifierInfo(modifierName);

			int numberOfParameters = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfParameters; j++) {
				String parameterName = (String) properties[currentIndex++];
				ParameterType<?> type = (ParameterType<?>) properties[currentIndex++];
				boolean isRange = (boolean) properties[currentIndex++];
				Object defaultValue = (Object) properties[currentIndex++];
				Object parameterValue = (Object) properties[currentIndex++];
				Object minValue = null, maxValue = null;
				if (isRange) {
					minValue = (Object) properties[currentIndex++];
					maxValue = (Object) properties[currentIndex++];
				}

				soundModifierInfo.getParameterInfo().add(new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}
			serverInfo.getSoundModifierInfo().add(soundModifierInfo);
		}

		int numberOfChannels = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfChannels; i++) {
			String channelName = (String) properties[currentIndex++];
			String modifierName = (String) properties[currentIndex++];
			LazySoundModifierInfo soundModifierInfo = new LazySoundModifierInfo(modifierName);

			int numberOfParameters = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfParameters; j++) {
				String parameterName = (String) properties[currentIndex++];
				ParameterType<?> type = (ParameterType<?>) properties[currentIndex++];
				Object parameterValue = (Object) properties[currentIndex++];
				soundModifierInfo.getParameterInfo().add(new LazyParameterInfo(parameterName, type, parameterValue));
			}

			SimpleChannelInfo channelInfo = new SimpleChannelInfo(channelName, soundModifierInfo);

			int numberOfPlayers = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfPlayers; j++) {
				String playerName = (String) properties[currentIndex++];
				channelInfo.getPlayerInfo().add(new SimplePlayerInfo(playerName));
			}

			serverInfo.getChannelInfo().add(channelInfo);
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Number of client
		wrapper.putInt(serverInfo.getClientInfo().size());

		for (FullClientInfo clientInfo : serverInfo.getClientInfo()) {
			// Client identifier
			wrapper.putString(clientInfo.getIdentifier().toString(), true);

			// Client mumble connected status
			wrapper.putInt(clientInfo.isMumbleConnected() ? 1 : 0);

			if (clientInfo.isMumbleConnected()) {
				// Client mumble address
				wrapper.putString(clientInfo.getMumbleAddress(), true);

				// Client mumble port
				wrapper.putInt(clientInfo.getMumblePort());
			}

			// Player's online status
			wrapper.putInt(clientInfo.isOnline() ? 1 : 0);

			if (clientInfo.isOnline()) {
				// Player's game address
				wrapper.putString(clientInfo.getGameAddress(), true);

				// Player's game port
				wrapper.putInt(clientInfo.getGamePort());

				// Player's name
				wrapper.putString(clientInfo.getPlayerName(), true);

				// Player's admin status
				wrapper.putInt(clientInfo.isAdmin() ? 1 : 0);

				// Player's mute status
				wrapper.putInt(clientInfo.isMute() ? 1 : 0);

				// Player's deafen status
				wrapper.putInt(clientInfo.isDeafen() ? 1 : 0);

				// X position
				wrapper.putDouble(clientInfo.getX());

				// Y position
				wrapper.putDouble(clientInfo.getY());

				// Z position
				wrapper.putDouble(clientInfo.getZ());

				// Yaw position
				wrapper.putDouble(clientInfo.getYaw());

				// Pitch position
				wrapper.putDouble(clientInfo.getPitch());
			}
		}

		// Number of sound modifier
		wrapper.putInt(serverInfo.getSoundModifierInfo().size());

		for (FullSoundModifierInfo modifierInfo : serverInfo.getSoundModifierInfo()) {
			// Modifier's name
			wrapper.putString(modifierInfo.getName(), true);

			// Number of parameter
			wrapper.putInt(modifierInfo.getParameterInfo().size());

			for (FullParameterInfo parameterInfo : modifierInfo.getParameterInfo()) {
				// Parameter's name
				wrapper.putString(parameterInfo.getName(), true);

				// Parameter's type
				wrapper.putInt(parameterInfo.getType().getCode());

				// isRangeParameter
				wrapper.putInt(parameterInfo.isRange() ? 1 : 0);

				// Parameter's default value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getDefaultValue()));

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));

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

		for (SimpleChannelInfo channelInfo : serverInfo.getChannelInfo()) {
			// Channel's name
			wrapper.putString(channelInfo.getName(), true);

			// Channel's sound modifier name
			wrapper.putString(channelInfo.getSoundModifierInfo().getName(), true);

			// Number of parameter
			wrapper.putInt(channelInfo.getSoundModifierInfo().getParameterInfo().size());

			for (LazyParameterInfo parameterInfo : channelInfo.getSoundModifierInfo().getParameterInfo()) {
				// Parameter's name
				wrapper.putString(parameterInfo.getName(), true);

				// Parameter's type
				wrapper.putInt(parameterInfo.getType().getCode());

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));
			}

			// Number of players
			wrapper.putInt(channelInfo.getPlayerInfo().size());

			for (SimplePlayerInfo playerInfo : channelInfo.getPlayerInfo())
				// Player's name
				wrapper.putString(playerInfo.getName(), true);
		}

		return wrapper.get();
	}
}
