package fr.pederobien.mumble.common.impl;

import java.util.StringJoiner;

import fr.pederobien.messenger.interfaces.IHeader;
import fr.pederobien.utils.ByteWrapper;

public class Header implements IHeader<Header> {
	private Idc idc;
	private Oid oid;
	private ErrorCode errorCode;
	private byte[] bytes;

	/**
	 * Constructor when the header has to be filled using method {@link #parse(byte[])}.
	 */
	public Header() {
		idc = Idc.UNKNOWN;
		oid = Oid.UNKNOWN;
		errorCode = ErrorCode.UNKNOWN;
		bytes = new byte[0];
	}

	public Header(Idc idc, Oid oid, ErrorCode errorCode) {
		this.idc = idc;
		this.oid = oid;
		this.errorCode = errorCode;

		bytes = ByteWrapper.create().put(idc.getBytes()).put(oid.getBytes()).put(errorCode.getBytes()).get();
	}

	public Header(Idc idc, Oid oid) {
		this(idc, oid, ErrorCode.NONE);
	}

	public Header(Idc idc) {
		this(idc, Oid.GET);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public int getLength() {
		return bytes.length;
	}

	@Override
	public Header parse(byte[] buffer) {
		ByteWrapper wrapper = ByteWrapper.wrap(buffer);
		idc = Idc.fromCode(wrapper.getInt(0));
		oid = Oid.fromCode(wrapper.getInt(4));
		errorCode = ErrorCode.fromCode(wrapper.getInt(8));

		bytes = ByteWrapper.create().put(idc.getBytes()).put(oid.getBytes()).put(errorCode.getBytes()).get();
		return this;
	}

	/**
	 * @return The header idc.
	 */
	public Idc getIdc() {
		return idc;
	}

	/**
	 * @return The header oid.
	 */
	public Oid getOid() {
		return oid;
	}

	/**
	 * @return The header error code.
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * @return True if and only if the error code is not equals to {@link ErrorCode#NONE}.
	 */
	public boolean isError() {
		return errorCode != ErrorCode.NONE;
	}

	@Override
	public String toString() {
		return new StringJoiner(",").add(idc.toString()).add(oid.toString()).add(errorCode.toString()).toString();
	}
}
