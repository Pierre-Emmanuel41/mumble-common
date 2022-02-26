package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SoundModifierInfoMessageV10 extends MumbleMessage {
	private List<FullSoundModifierInfo> soundModifiers;

	/**
	 * Creates a message to get a description of all sound modifiers registered on the mumble server.
	 * 
	 * @param header The message header.
	 */
	protected SoundModifierInfoMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.SOUND_MODIFIER_INFO, header);
		soundModifiers = new ArrayList<FullSoundModifierInfo>();
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (payload.length == 0 || getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

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

			FullSoundModifierInfo soundModifierInfo = new FullSoundModifierInfo(modifierName);

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

				soundModifierInfo.getParameterInfo().add(new FullParameterInfo(parameterName, type, parameterValue, defaultValue, isRange, minValue, maxValue));
			}

			soundModifiers.add(soundModifierInfo);
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

		int numberOfSoundModifiers = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfSoundModifiers; i++) {
			String modifierName = (String) properties[currentIndex++];
			FullSoundModifierInfo soundModifierInfo = new FullSoundModifierInfo(modifierName);

			int numberOfParameters = (int) properties[currentIndex++];
			for (int j = 0; j < numberOfParameters; j++) {
				String parameterName = (String) properties[currentIndex++];
				ParameterType<?> parameterType = (ParameterType<?>) properties[currentIndex++];
				boolean isRange = (boolean) properties[currentIndex++];
				Object defaultValue = (Object) properties[currentIndex++];
				Object parameterValue = (Object) properties[currentIndex++];
				Object minValue = null, maxValue = null;
				if (isRange) {
					minValue = (Object) properties[currentIndex++];
					maxValue = (Object) properties[currentIndex++];
				}

				soundModifierInfo.getParameterInfo().add(new FullParameterInfo(parameterName, parameterType, parameterValue, defaultValue, isRange, minValue, maxValue));
			}

			soundModifiers.add(soundModifierInfo);
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

			for (FullParameterInfo parameterInfo : soundModifierInfo.getParameterInfo()) {
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
