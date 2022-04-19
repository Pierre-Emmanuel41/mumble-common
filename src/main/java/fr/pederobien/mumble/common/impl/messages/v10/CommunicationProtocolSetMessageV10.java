package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class CommunicationProtocolSetMessageV10 extends MumbleMessage {
	private float version;

	/**
	 * Creates a message in order to get the version latest version of the communication protocol supported by the remote.
	 * 
	 * @param header The header associated to this message.
	 */
	protected CommunicationProtocolSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.COMMUNICATION_PROTOCOL_VERSION_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		version = wrapper.getFloat(0);
		return this;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		version = (float) properties[0];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		wrapper.putFloat(version);
		return wrapper.get();
	}

	/**
	 * @return The latest version of the communication protocol supported by the remote.
	 */
	public float getVersion() {
		return version;
	}
}
