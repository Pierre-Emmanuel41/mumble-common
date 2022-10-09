package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetParameterMinValueV10 extends MumbleMessage {
	private String channelName;
	private String parameterName;
	private ParameterType<?> parameterType;
	private Object newMinValue;

	/**
	 * Creates a message in order to get the minimum value of a parameter of a sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected GetParameterMinValueV10(IMumbleHeader header) {
		super(Identifier.GET_PARAMETER_MIN_VALUE, header);
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

		try {
			// When it is a response

			// Parameter's minimum value
			newMinValue = parameterType.getValue(wrapper.next(parameterType.size()));
		} catch (IndexOutOfBoundsException e) {
			// when it is a request
		}

		super.setProperties(channelName, parameterName, newMinValue);
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

		// When it is an answer
		if (properties.length > 4)
			// Parameter's minimum value
			newMinValue = properties[3];
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

		// When it is an answer
		if (getProperties().length > 4)
			// Parameter's minimum value
			wrapper.put(parameterType.getBytes(newMinValue));

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
	 * @return The new parameter's minimum value.
	 */
	public Object getNewMinValue() {
		return newMinValue;
	}
}
