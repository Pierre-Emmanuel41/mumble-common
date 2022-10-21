package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;
import fr.pederobien.utils.ReadableByteWrapper;

public class GetCommunicationProtocolVersionsV10 extends MumbleMessage {
	private float[] versions;

	/**
	 * Creates a message in order to get all versions of the communication protocol supported by the remote.
	 * 
	 * @param header The header associated to this message.
	 */
	protected GetCommunicationProtocolVersionsV10(IMumbleHeader header) {
		super(MumbleIdentifier.GET_CP_VERSIONS, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		ReadableByteWrapper wrapper = ReadableByteWrapper.wrap(payload);

		// When it is an answer
		if (payload.length > 0) {
			// Number of version
			int numberOfVersions = wrapper.nextInt();

			versions = new float[numberOfVersions];
			properties.add(versions);

			for (int i = 0; i < numberOfVersions; i++) {
				// Version
				versions[i] = wrapper.nextFloat();
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
