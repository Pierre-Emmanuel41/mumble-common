package fr.pederobien.mumble.common.impl;

import fr.pederobien.messenger.impl.Header;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class MumbleHeader extends Header implements IMumbleHeader {
	private Idc idc;
	private Oid oid;
	private ErrorCode errorCode;

	/**
	 * Creates a new header associated to the given version. The value of the Idc is {@link Idc#UNKNOWN}, the Oid is
	 * {@link Oid#UNKNOWN} and the error code is {@link ErrorCode#NONE}.
	 * 
	 * @param version The protocol version used to create this header.
	 */
	public MumbleHeader(float version) {
		super(version);
		idc = Idc.UNKNOWN;
		oid = Oid.UNKNOWN;
		errorCode = ErrorCode.NONE;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);
		idc = (Idc) properties[0];
		oid = (Oid) properties[1];
		errorCode = (ErrorCode) properties[2];
	}

	@Override
	protected byte[] generateProperties() {
		return ByteWrapper.create().put(idc.getBytes()).put(oid.getBytes()).put(errorCode.getBytes()).get();
	}

	@Override
	public MumbleHeader parse(byte[] buffer) {
		super.parse(buffer);
		ByteWrapper wrapper = ByteWrapper.wrap(buffer);

		// +8: Idc
		idc = Idc.fromCode(wrapper.getInt(8));

		// +12: Oid
		oid = Oid.fromCode(wrapper.getInt(12));

		// +16: ErrorCode
		errorCode = ErrorCode.fromCode(wrapper.getInt(16));
		super.setProperties(idc, oid, errorCode);
		return this;
	}

	@Override
	public Idc getIdc() {
		return idc;
	}

	@Override
	public Oid getOid() {
		return oid;
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
