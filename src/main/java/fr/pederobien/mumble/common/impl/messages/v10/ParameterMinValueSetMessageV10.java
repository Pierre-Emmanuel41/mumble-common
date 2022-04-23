package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.impl.model.ParameterType;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class ParameterMinValueSetMessageV10 extends MumbleMessage {
	private String channelName;
	private String parameterName;
	private ParameterType<?> parameterType;
	private Object newMinValue;

	/**
	 * Creates a request in order to set the value of a parameter of a sound modifier associated to a channel.
	 * 
	 * @param header The message header.
	 */
	protected ParameterMinValueSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PARAMETER_MIN_VALUE_SET, header);
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

		// Parameter's new min value
		newMinValue = parameterType.getValue(wrapper.extract(first, parameterType.size()));
		first += parameterType.size();

		super.setProperties(channelName, parameterName, newMinValue);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		channelName = (String) properties[0];
		parameterName = (String) properties[1];
		parameterType = (ParameterType<?>) properties[2];
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

		// Parameter's new value
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