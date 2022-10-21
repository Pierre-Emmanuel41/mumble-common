package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.messages.v10.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetSoundModifiersInfoV10 extends MumbleMessage {
	private List<FullSoundModifierInfo> soundModifiers;

	/**
	 * Creates a message to get a information of all sound modifiers registered on the mumble server.
	 * 
	 * @param header The message header.
	 */
	protected GetSoundModifiersInfoV10(IMumbleHeader header) {
		super(MumbleIdentifier.GET_SOUND_MODIFIERS_INFO, header);
		soundModifiers = new ArrayList<FullSoundModifierInfo>();
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

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
				int code = wrapper.nextInt();
				ParameterType<?> type = ParameterType.fromCode(code);
				properties.add(type);

				boolean isRange = wrapper.nextInt() == 1;
				properties.add(isRange);

				// Parameter's default value
				Object defaultValue = type.getValue(wrapper.next(type.size()));
				properties.add(defaultValue);

				// Parameter's value
				Object parameterValue = type.getValue(wrapper.next(type.size()));
				properties.add(parameterValue);

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

			soundModifiers.add(modifierInfo);
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

		// Number of sound modifiers registered on the server
		int numberOfSoundModifiers = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfSoundModifiers; i++) {
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

			soundModifiers.add(modifierInfo);
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getProperties().length == 0 || getHeader().isError())
			return wrapper.get();

		// Get all sound modifier registered on the server
		wrapper.putInt(soundModifiers.size());

		for (FullSoundModifierInfo soundModifierInfo : soundModifiers) {
			// Modifier's name
			wrapper.putString(soundModifierInfo.getName(), true);

			// Number of parameter
			wrapper.putInt(soundModifierInfo.getParameterInfo().size());

			for (FullParameterInfo parameterInfo : soundModifierInfo.getParameterInfo().values()) {
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
		return wrapper.get();
	}

	/**
	 * @return A description of each registered sound modifiers
	 */
	public List<FullSoundModifierInfo> getSoundModifiers() {
		return soundModifiers;
	}
}
