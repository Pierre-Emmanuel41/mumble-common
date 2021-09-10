package fr.pederobien.mumble.common.impl;

import fr.pederobien.utils.ByteWrapper;

public enum Oid {
	GET, SET, ADD, REMOVE, INFO, UNKNOWN(-1);

	private static int codeGenerator;
	private int code;
	private byte[] bytes;

	private Oid() {
		this(generateCode());
	}

	private Oid(int code) {
		this.code = code;
		bytes = ByteWrapper.create().putInt(code).get();
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

	private static int generateCode() {
		return codeGenerator++;
	}
}
