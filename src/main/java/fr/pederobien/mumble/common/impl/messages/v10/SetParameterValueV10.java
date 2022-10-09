package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class SetParameterValueV10 extends MumbleMessage {
	private String channelName;
	private String parameterName;
	private ParameterType<?> parameterType;
	private Object newValue;

	/**
	 * Creates a message in order to set the value of a parameter of a sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected SetParameterValueV10(IMumbleHeader header) {
		super(Identifier.SET_PARAMETER_VALUE, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// Channel's name
		channelName = wrapper.nextString(wrapper.nextInt());

		// Parameter's name
		parameterName = wrapper.nextString(wrapper.nextInt());

		// Parameter's type
		parameterType = ParameterType.fromCode(wrapper.nextInt());

		// Parameter's new value
		newValue = parameterType.getValue(wrapper.next(parameterType.size()));

		super.setProperties(channelName, parameterName, newValue);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		// Channel's name
		channelName = (String) properties[0];

		// Parameter's name
		parameterName = (String) properties[1];

		// Parameter's type
		parameterType = (ParameterType<?>) properties[2];

		// Parameter's new value
		newValue = properties[3];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Channel's name
		wrapper.putString(channelName, true);

		// Parameter's name
		wrapper.putString(parameterName, true);

		// Parameter's type
		wrapper.putInt(parameterType.getCode());

		// Parameter's new value
		wrapper.put(parameterType.getBytes(newValue));

		return wrapper.get();
	}

	/**
	 * @return The name of the channel whose one parameter of its sound modifier should be changed.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return The name of the parameter whose the current value must be changed.
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @return The new parameter's value.
	 */
	public Object getNewValue() {
		return newValue;
	}
}
