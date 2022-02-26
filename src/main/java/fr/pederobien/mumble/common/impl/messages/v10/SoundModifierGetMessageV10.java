package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.LazyParameterInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.LazySoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class SoundModifierGetMessageV10 extends MumbleMessage {
	private String channelName;
	private LazySoundModifierInfo soundModifierInfo;

	/**
	 * Creates a request in order to get the sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected SoundModifierGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.SOUND_MODIFIER_GET, header);
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
		channelName = wrapper.getString(first, channelNameLength);
		properties.add(channelName);
		first += channelNameLength;

		// When it is an answer
		if (first < payload.length) {
			// Modifier name
			int modifierNameLength = wrapper.getInt(first);
			first += 4;
			String modifierName = wrapper.getString(first, modifierNameLength);
			properties.add(modifierName);
			first += modifierNameLength;

			soundModifierInfo = new LazySoundModifierInfo(modifierName);

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
				ParameterType<?> parameterType = ParameterType.fromCode(code);
				properties.add(parameterType);

				// Parameter's value
				Object parameterValue = parameterType.getValue(wrapper.extract(first, parameterType.size()));
				properties.add(parameterValue);
				first += parameterType.size();

				soundModifierInfo.getParameterInfo().add(new LazyParameterInfo(parameterName, parameterType, parameterValue));
			}
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

			soundModifierInfo = new LazySoundModifierInfo(modifierName);

			int numberOfParameters = (int) properties[currentIndex++];

			for (int i = 0; i < numberOfParameters; i++) {
				String parameterName = (String) properties[currentIndex++];
				ParameterType<?> parameterType = (ParameterType<?>) properties[currentIndex++];
				Object parameterValue = (Object) properties[currentIndex++];

				soundModifierInfo.getParameterInfo().add(new LazyParameterInfo(parameterName, parameterType, parameterValue));
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
		if (soundModifierInfo != null) {
			// Modifier's name
			wrapper.putString(soundModifierInfo.getName(), true);

			// Number of parameters
			wrapper.putInt(soundModifierInfo.getParameterInfo().size());

			for (LazyParameterInfo parameterInfo : soundModifierInfo.getParameterInfo()) {
				// Parameter's name
				wrapper.putString(parameterInfo.getName(), true);

				// Parameter's type
				wrapper.putInt(parameterInfo.getType().getCode());

				// Parameter's value
				wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));
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
	public LazySoundModifierInfo getSoundModifierInfo() {
		return soundModifierInfo;
	}
}
