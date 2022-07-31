package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.Identifier;
import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class GetParameterMaxValueV10 extends MumbleMessage {
	private String channelName;
	private String parameterName;
	private ParameterType<?> parameterType;
	private Object newMaxValue;

	/**
	 * Creates a message in order to get the maximum value of a parameter of a sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected GetParameterMaxValueV10(IMumbleHeader header) {
		super(Identifier.GET_PARAMETER_MAX_VALUE, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Channel's name
		int channelNameLength = wrapper.getInt(first);
		first += 4;
		channelName = wrapper.getString(first, channelNameLength);
		first += channelNameLength;

		// Parameter's name
		int parameterNameLength = wrapper.getInt(first);
		first += 4;
		parameterName = wrapper.getString(first, parameterNameLength);
		first += parameterNameLength;

		// Parameter's type
		int code = wrapper.getInt(first);
		first += 4;
		parameterType = ParameterType.fromCode(code);

		// When it is an answer
		if (first < payload.length) {
			// Parameter's max value
			newMaxValue = parameterType.getValue(wrapper.extract(first, parameterType.size()));
			first += parameterType.size();
		}

		super.setProperties(channelName, parameterName, newMaxValue);
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
			// Parameter's max value
			newMaxValue = properties[3];
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
			// Parameter's new max value
			wrapper.put(parameterType.getBytes(newMaxValue));

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
	 * @return The new parameter's maximum value.
	 */
	public Object getNewMaxValue() {
		return newMaxValue;
	}
}
