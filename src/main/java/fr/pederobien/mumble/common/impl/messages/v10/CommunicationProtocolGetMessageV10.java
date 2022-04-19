package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class CommunicationProtocolGetMessageV10 extends MumbleMessage {
	private float[] versions;

	/**
	 * Creates a message in order to get the version latest version of the communication protocol supported by the remote.
	 * 
	 * @param header The header associated to this message.
	 */
	protected CommunicationProtocolGetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.COMMUNICATION_PROTOCOL_VERSION_GET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> properties = new ArrayList<Object>();

		// When it is an answer
		if (payload.length > 0) {
			// Number of version
			int numberOfVersions = wrapper.getInt(first);
			first += 4;

			versions = new float[numberOfVersions];

			for (int i = 0; i < numberOfVersions; i++) {
				// Version
				float version = wrapper.getFloat(first);
				versions[i] = version;
				properties.add(versions);
				first += 4;
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

		// When it is an answer
		if (properties.length > 0) {
			versions = new float[properties.length];

			for (int i = 0; i < properties.length; i++)
				versions[i] = (float) properties[i];
		}
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// When it is an answer
		if (getProperties().length > 0) {
			// Number of versions
			wrapper.putInt(versions.length);

			for (float version : versions)
				wrapper.putFloat(version);
		}

		return wrapper.get();
	}

	/**
	 * @return The latest version of the communication protocol supported by the remote.
	 */
	public float[] getVersions() {
		return versions;
	}
}
