package fr.pederobien.mumble.common.impl;

import java.nio.ByteBuffer;

public enum Idc {
	// Idc to get or create a unique identifier for the player.
	UNIQUE_IDENTIFIER(1),

	// Idc to know if a player is online or offline.
	PLAYER_STATUS(2),

	// Idc to know if player admin status has changed.
	PLAYER_ADMIN(3),

	// Idc to know if channel has been renamed, added, removed.
	CHANNELS(4),

	// Idc to know if a player has been added, removed from a channel.
	CHANNELS_PLAYER(5),

	// Idc to get the server configuration
	SERVER_CONFIGURATION(6),

	// Idc to get the udp port number.
	UDP_PORT(7),

	// Idc when a player speak.
	PLAYER_SPEAK(8),

	// Idc when a player is mute.
	PLAYER_MUTE(9),

	// Idc when a player mute another player.
	PLAYER_MUTE_BY(10),

	// Idc when a player is deafen.
	PLAYER_DEAFEN(11),

	// Idc when a player is kick
	PLAYER_KICK(12),

	// Idc when cannot be parsed.
	UNKNOWN(-1);

	private int code;
	private byte[] bytes;

	private Idc(int code) {
		this.code = code;
		bytes = ByteBuffer.allocate(4).putInt(code).array();
	}

	@Override
	public String toString() {
		return "Idc={" + super.toString() + "," + code + "}";
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getCode() {
		return code;
	}

	/**
	 * Get the Idc associated to the given code.
	 * 
	 * @param code The code associated to the Idc to get.
	 * 
	 * @return The Idc associated to the given code or UNKNOWN if there is no Idc associated to the given code.
	 */
	public static Idc fromCode(int code) {
		for (Idc idc : values())
			if (idc.getCode() == code)
				return idc;
		return UNKNOWN;
	}
}
