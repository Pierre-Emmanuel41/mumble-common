package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.ChannelInfo.FullChannelInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetChannelInfoV10 extends MumbleMessage {
	private String channelName;
	private FullChannelInfo channelInfo;

	/**
	 * Creates a message to get the information about a channel.
	 * 
	 * @param header The message header.
	 */
	protected GetChannelInfoV10(IMumbleHeader header) {
		super(MumbleIdentifier.GET_CHANNEL_INFO, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Channel's name
		int channelNameLength = wrapper.nextInt();
		channelName = wrapper.nextString(channelNameLength);
		properties.add(channelName);

		try {
			// Case when it is an answer

			// Channel's sound modifier name
			String soundModifierName = wrapper.nextString(wrapper.nextInt());
			properties.add(soundModifierName);

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(soundModifierName);

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

			channelInfo = new FullChannelInfo(channelName, modifierInfo);

			// Number of players
			int numberOfPlayers = wrapper.nextInt();
			properties.add(numberOfPlayers);

			for (int j = 0; j < numberOfPlayers; j++) {
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
		} catch (IndexOutOfBoundsException e) {
			// Case when it is a request
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

		// Channel's name
		channelName = (String) properties[currentIndex++];

		// When it is an answer
		if (properties.length > 1) {
			// Sound modifier's name
			String modifierName = (String) properties[currentIndex++];

			FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

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

			channelInfo = new FullChannelInfo(channelName, modifierInfo);

			int numberOfPlayers = (int) properties[currentIndex++];

			for (int j = 0; j < numberOfPlayers; j++) {
				// Player's name
				String playerName = (String) properties[currentIndex++];

				// Player's mute status
				boolean isMute = (boolean) properties[currentIndex++];

				// Player's deafen status
				boolean isDeafen = (boolean) properties[currentIndex++];

				// Player's muteBy status
				boolean isMutebyMainPlayer = (boolean) properties[currentIndex++];

				channelInfo.getPlayerInfo().put(playerName, new StatusPlayerInfo(playerName, isMute, isDeafen, isMutebyMainPlayer));
			}
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Channel's name
		wrapper.putString(channelName, true);

		if (channelInfo != null) {

			// Modifier's name
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
			}
		}

		return wrapper.get();
	}

	/**
	 * @return A list that contains information about each registered channels.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return A list that contains information about each registered channels.
	 */
	public FullChannelInfo getChannelInfo() {
		return channelInfo;
	}
}
