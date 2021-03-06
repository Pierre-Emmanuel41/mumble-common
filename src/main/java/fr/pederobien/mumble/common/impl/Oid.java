package fr.pederobien.mumble.common.impl;

import java.nio.ByteBuffer;

public enum Oid {
	GET(1), SET(2), ADD(3), REMOVE(4), UNKNOWN(-1);

	private int code;
	private byte[] bytes;

	private Oid(int code) {
		this.code = code;
		bytes = ByteBuffer.allocate(4).putInt(code).array();
	}

	@Override
	public String toString() {
		return "Oid={" + super.toString() + "," + code + "}";

	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getCode() {
		return code;
	}

	/**
	 * Get the Oid associated to the given code.
	 * 
	 * @param code The code associated to the Oid to get.
	 * 
	 * @return The Oid associated to the given code or UNKNOWN if there is no Oid associated to the given code.
	 */
	public static Oid fromCode(int code) {
		for (Oid oid : values())
			if (oid.getCode() == code)
				return oid;
		return UNKNOWN;
	}
}
