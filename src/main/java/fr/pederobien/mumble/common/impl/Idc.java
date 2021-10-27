package fr.pederobien.mumble.common.impl;

import java.nio.ByteBuffer;

public enum Idc {

	// Idc to join the server.
	SERVER_JOIN,

	// Idc to leave the server.
	SERVER_LEAVE,

	// Idc to know if a player is online or offline.
	PLAYER_INFO,

	// Idc to know if player admin status has changed.
	PLAYER_ADMIN,

	// Idc to know if channel has been renamed, added, removed.
	CHANNELS,

	// Idc to know if a player has been added, removed from a channel.
	CHANNELS_PLAYER,

	// Idc when a player speak.
	PLAYER_SPEAK,

	// Idc when a player is mute.
	PLAYER_MUTE,

	// Idc when a player mute another player.
	PLAYER_MUTE_BY,

	// Idc when a player is deafen.
	PLAYER_DEAFEN,

	// Idc when a player is kick.
	PLAYER_KICK,

	// Idc to set the sound modifier of a channel.
	SOUND_MODIFIER,

	// Idc for the player position.
	PLAYER_POSITION,

	// Idc to check if a port is used on client side.
	GAME_PORT,

	// Idc when cannot be parsed.
	UNKNOWN(-1);

	private static int codeGenerator;
	private int code;
	private byte[] bytes;

	private Idc() {
		this(generateCode());
	}

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

	private static int generateCode() {
		return codeGenerator++;
	}
}
