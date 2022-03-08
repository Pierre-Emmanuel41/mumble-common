package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ChannelInfo.LazyChannelInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.LazyParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.LazySoundModifierInfo;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class ChannelsAddMessageV10 extends MumbleMessage {
	private LazyChannelInfo channelInfo;

	/**
	 * Creates a message to add a channel to a mumble server.
	 * 
	 * @param header The message header.
	 */
	protected ChannelsAddMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.CHANNELS_ADD, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Channel's name
		int channelNameLength = wrapper.getInt(first);
		first += 4;
		String channelName = wrapper.getString(first, channelNameLength);
		properties.add(channelName);
		first += channelNameLength;

		// Sound modifier's name
		int soundModifierNameLength = wrapper.getInt(first);
		first += 4;
		String modifierName = wrapper.getString(first, soundModifierNameLength);
		properties.add(modifierName);
		first += soundModifierNameLength;

		LazySoundModifierInfo soundModifierInfo = new LazySoundModifierInfo(modifierName);

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

		channelInfo = new LazyChannelInfo(channelName, soundModifierInfo);
		super.setProperties(properties.toArray());
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		int currentIndex = 0;

		// Channel's name
		String channelName = (String) properties[currentIndex++];

		// Sound modifier's name
		String soundModifierName = (String) properties[currentIndex++];

		LazySoundModifierInfo soundModifierInfo = new LazySoundModifierInfo(soundModifierName);

		// Number of parameters
		int numberOfParameters = (int) properties[currentIndex++];
		for (int i = 0; i < numberOfParameters; i++) {
			// Parameter's name
			String parameterName = (String) properties[currentIndex++];

			// Parameter's type
			ParameterType<?> parameterType = (ParameterType<?>) properties[currentIndex++];

			// Parameter's value
			Object parameterValue = (Object) properties[currentIndex++];

			soundModifierInfo.getParameterInfo().add(new LazyParameterInfo(parameterName, parameterType, parameterValue));
		}

		channelInfo = new LazyChannelInfo(channelName, soundModifierInfo);
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

		for (LazyParameterInfo parameterInfo : channelInfo.getSoundModifierInfo().getParameterInfo()) {
			// Parameter's name
			wrapper.putString(parameterInfo.getName(), true);

			// Parameter's type
			wrapper.putInt(parameterInfo.getType().getCode());

			// Parameter's value
			wrapper.put(parameterInfo.getType().getBytes(parameterInfo.getValue()));
		}

		return wrapper.get();
	}

	/**
	 * @return The information about the channel to add.
	 */
	public LazyChannelInfo getChannelInfo() {
		return channelInfo;
	}
}
