package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SetChannelSoundModifierV10 extends MumbleMessage {
	private SemiFullChannelInfo channelInfo;

	/**
	 * Creates a message in order to set the sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected SetChannelSoundModifierV10(IMumbleHeader header) {
		super(Identifier.SET_CHANNEL_SOUND_MODIFIER, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Channel name
		int channelNameLength = wrapper.getInt(first);
		first += 4;
		String channelName = wrapper.getString(first, channelNameLength);
		properties.add(channelName);
		first += channelNameLength;

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

		channelInfo = new SemiFullChannelInfo(channelName, modifierInfo);

		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		int currentIndex = 0;

		// Channel name
		String channelName = (String) properties[currentIndex++];

		// Sound modifier's name
		String modifierName = (String) properties[currentIndex++];

		FullSoundModifierInfo modifierInfo = new FullSoundModifierInfo(modifierName);

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

		channelInfo = new SemiFullChannelInfo(channelName, modifierInfo);
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Channel' name
		wrapper.putString(channelInfo.getName(), true);

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

		return wrapper.get();
	}

	/**
	 * @return The channel description.
	 */
	public SemiFullChannelInfo getChannelInfo() {
		return channelInfo;
	}
}
