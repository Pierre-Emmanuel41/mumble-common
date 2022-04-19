package fr.pederobien.mumble.common.impl;

import fr.pederobien.utils.ByteWrapper;

public enum Idc {

	// Idc to get the server configuration.
	SERVER_INFO,

	// Idc to join the server.
	SERVER_JOIN,

	// Idc to leave the server.
	SERVER_LEAVE,

	// Idc to modify the version of the communication protocol
	COMMUNICATION_PROTOCOL_VERSION,

	// Idc to get access to all information about a player.
	PLAYER,

	// Idc to get access to the online status of a player.
	PLAYER_ONLINE,

	// Idc to get access to the name of a player.
	PLAYER_NAME,

	// Idc to get access to the address used by a player to play to a game.
	PLAYER_GAME_ADDRESS,

	// Idc to know if player admin status has changed.
	PLAYER_ADMIN,

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

	// Idc for the player position.
	PLAYER_POSITION,

	// Idc to know if channel has been renamed, added, removed.
	CHANNELS,

	// Idc to know if a player has been added, removed from a channel.
	CHANNELS_PLAYER,

	// Idc to set the value of a parameter of a sound modifier associated to a channel.
	PARAMETER_VALUE,

	// Idc to set the minimum value of a parameter of a sound modifier associated to a channel.
	PARAMETER_MIN_VALUE,

	// Idc to set the maximum value of a parameter of a sound modifier associated to a channel.
	PARAMETER_MAX_VALUE,

	// Idc to set the sound modifier of a channel.
	SOUND_MODIFIER,

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
		bytes = ByteWrapper.create().putInt(code).get();
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
