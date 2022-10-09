package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetChannelSoundModifierV10 extends MumbleMessage {
	private String channelName;
	private FullSoundModifierInfo modifierInfo;

	/**
	 * Creates a message in order to get information about the sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected GetChannelSoundModifierV10(IMumbleHeader header) {
		super(Identifier.GET_CHANNEL_SOUND_MODIFIER_INFO, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Channel name
		int channelNameLength = wrapper.nextInt();
		channelName = wrapper.nextString(channelNameLength);
		properties.add(channelName);

		try {
			// When it is an answer

			// Modifier name
			String modifierName = wrapper.nextString(wrapper.nextInt());
			properties.add(modifierName);

			modifierInfo = new FullSoundModifierInfo(modifierName);

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
		} catch (IndexOutOfBoundsException e) {
			// When it is a request
		}

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		int currentIndex = 0;

		channelName = (String) properties[currentIndex++];

		// When it is an answer
		if (properties.length > 1) {
			String modifierName = (String) properties[currentIndex++];

			modifierInfo = new FullSoundModifierInfo(modifierName);

			int numberOfParameters = (int) properties[currentIndex++];

			for (int i = 0; i < numberOfParameters; i++) {
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
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Channel's name
		wrapper.putString(channelName, true);

		// When it is an answer
		if (modifierInfo != null) {
			// Modifier's name
			wrapper.putString(modifierInfo.getName(), true);

			// Number of parameters
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
		return wrapper.get();
	}

	/**
	 * @return The channel name whose the sound modifier is returned.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return The sound modifier description. It is not null when the server replies but null when client send a request.
	 */
	public FullSoundModifierInfo getSoundModifierInfo() {
		return modifierInfo;
	}
}
