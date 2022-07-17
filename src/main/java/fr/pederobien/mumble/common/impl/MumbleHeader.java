package fr.pederobien.mumble.common.impl;

import fr.pederobien.messenger.impl.Header;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class MumbleHeader extends Header implements IMumbleHeader {
	/**
	 * The number of bytes reserved for the message header. It correspond to:</br>
	 * </br>
	 * +0: Communication protocol version.</br>
	 * +4: The sequence number.</br>
	 * +8: The identifier.</br>
	 * +12: The error code.
	 */
	public static final int HEADER_LENGH = 16;
	private Identifier identifier;
	private MumbleErrorCode mumbleErrorCode;

	/**
	 * Creates a new header associated to the given version. The value of the Idc is {@link Idc#UNKNOWN}, the Oid is
	 * {@link Oid#UNKNOWN} and the error code is {@link MumbleErrorCode#NONE}.
	 * 
	 * @param version The protocol version used to create this header.
	 */
	public MumbleHeader(float version) {
		super(version);
		identifier = Identifier.UNKNOWN;
		mumbleErrorCode = MumbleErrorCode.NONE;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);
		identifier = (Identifier) properties[0];
		mumbleErrorCode = (MumbleErrorCode) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		return ByteWrapper.create().put(identifier.getBytes()).put(mumbleErrorCode.getBytes()).get();
	}

	@Override
	public MumbleHeader parse(byte[] buffer) {
		super.parse(buffer);
		ByteWrapper wrapper = ByteWrapper.wrap(buffer);

		// +8: Identifier
		identifier = Identifier.fromCode(wrapper.getInt(MumbleMessage.IDENTIFIER_INDEX - MumbleMessage.BEGIN_WORD.length));

		// +12: ErrorCode
		mumbleErrorCode = MumbleErrorCode.fromCode(wrapper.getInt(MumbleMessage.ERROR_CODE_INDEX - MumbleMessage.BEGIN_WORD.length));
		super.setProperties(identifier, mumbleErrorCode);
		return this;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public MumbleErrorCode getErrorCode() {
		return mumbleErrorCode;
	}
}
