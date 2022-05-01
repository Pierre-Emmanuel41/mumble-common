package fr.pederobien.mumble.common.impl;

import java.util.HashMap;
import java.util.Map;

import fr.pederobien.utils.ByteWrapper;

public enum Identifier {
	/**
	 * Identifier of the message to get the complete server configuration.
	 */
	GET_FULL_SERVER_CONFIGURATION,

	/**
	 * Identifier of the message to get the server configuration.
	 */
	GET_SERVER_CONFIGURATION,

	/*
	 * Identifier of the message in order to join a mumble server.
	 */
	SET_SERVER_JOIN,

	/**
	 * Identifier of the message to leave a mumble server.
	 */
	SET_SERVER_LEAVE,

	/**
	 * Identifier of the message to get the versions of the communication protocol of the remote.
	 */
	GET_CP_VERSIONS,

	/**
	 * Identifier of the message to set the version of the communication protocol of the remote.
	 */
	SET_CP_VERSION,

	/**
	 * Identifier of the message to get information about a player.
	 */
	GET_PLAYER_INFO,

	/**
	 * Identifier of the message to register a player on a mumble server.
	 */
	REGISTER_PLAYER_ON_SERVER,

	/**
	 * Identifier of the message to unregister a player from a mumble server.
	 */
	UNREGISTER_PLAYER_FROM_SERVER,

	/**
	 * Identifier of the message to get the online status of a player.
	 */
	GET_PLAYER_ONLINE_STATUS,

	/**
	 * Identifier of the message to set the online status of a player.
	 */
	SET_PLAYER_ONLINE_STATUS,

	/**
	 * Identifier of the message to set the name of a player.
	 */
	SET_PLAYER_NAME,

	/**
	 * Identifier of the message to get the game address of a player.
	 */
	GET_PLAYER_GAME_ADDRESS,

	/**
	 * Identifier of the message to set the game address of a player.
	 */
	SET_PLAYER_GAME_ADDRESS,

	/**
	 * Identifier of the message to get the administrator status of a player.
	 */
	GET_PLAYER_ADMINISTRATOR,

	/**
	 * Identifier of the message to set the administrator status of a player.
	 */
	SET_PLAYER_ADMINISTRATOR,

	/**
	 * Identifier of the message to received from an audio sample from a vocal client.
	 */
	PLAYER_SPEAK_INPUT,

	/**
	 * Identifier of the message to send an audio sample to a specific client.
	 */
	PLAYER_SPEAK_OUTPUT,

	/**
	 * Identifier of the message to get the mute status of a player.
	 */
	GET_PLAYER_MUTE,

	/**
	 * Identifier of the message to set the mute status of a player.
	 */
	SET_PLAYER_MUTE,

	/**
	 * Identifier of the message to set the mute status of a player for another player.
	 */
	SET_PLAYER_MUTE_BY,

	/**
	 * Identifier of the message to get the deafen status of a player.
	 */
	GET_PLAYER_DEAFEN,

	/**
	 * Identifier of the message to set the deafen status of a player.
	 */
	SET_PLAYER_DEAFEN,

	/**
	 * Identifier of the message to kick a player from a channel.
	 */
	KICK_PLAYER_FROM_CHANNEL,

	/**
	 * Identifier of the message to get the position of a player.
	 */
	GET_PLAYER_POSITION,

	/**
	 * Identifier of the message to set the position of a player.
	 */
	SET_PLAYER_POSITION,

	/**
	 * Identifier of the message to get information about each channels.
	 */
	GET_CHANNELS_INFO,

	/**
	 * Identifier of the message to get information about a channel.
	 */
	GET_CHANNEL_INFO,

	/**
	 * Identifier of the message to add a channel on the mumble server.
	 */
	REGISTER_CHANNEL_ON_THE_SERVER,

	/**
	 * Identifier of the message to remove a channel from the mumble server.
	 */
	UNREGISTER_CHANNEL_FROM_SERVER,

	/**
	 * Identifier of the message to set the name of a channel.
	 */
	SET_CHANNEL_NAME,

	/**
	 * Identifier of the message to add a player to a channel.
	 */
	ADD_PLAYER_TO_CHANNEL,

	/**
	 * Identifier of the message to add a player to a channel.
	 */
	REMOVE_PLAYER_FROM_CHANNEL,

	/**
	 * Identifier of the message to get the value of a parameter of a sound modifier associated to a channel.
	 */
	GET_PARAMETER_VALUE,

	/**
	 * Identifier of the message to set the value of a parameter of a sound modifier associated to a channel.
	 */
	SET_PARAMETER_VALUE,

	/**
	 * Identifier of the message to get the minimum value of a parameter of a sound modifier associated to a channel.
	 */
	GET_PARAMETER_MIN_VALUE,

	/**
	 * Identifier of the message to set the minimum value of a parameter of a sound modifier associated to a channel.
	 */
	SET_PARAMETER_MIN_VALUE,

	/**
	 * Identifier of the message to get the maximum value of a parameter of a sound modifier associated to a channel.
	 */
	GET_PARAMETER_MAX_VALUE,

	/**
	 * Identifier of the message to set the maximum value of a parameter of a sound modifier associated to a channel.
	 */
	SET_PARAMETER_MAX_VALUE,

	/**
	 * Identifier of the message to get informations about each sound modifier registered on the mumble server.
	 */
	GET_SOUND_MODIFIERS_INFO,

	/**
	 * Identifier of the message to get informations about the sound modifier of a channel.
	 */
	GET_CHANNEL_SOUND_MODIFIER_INFO,

	/**
	 * Identifier of the message to set the sound modifier of a channel.
	 */
	SET_CHANNEL_SOUND_MODIFIER,

	/**
	 * Identifier of the message to check if a specific game port is used on client side.
	 */
	IS_GAME_PORT_USED,

	/**
	 * Identifier of the message to set if a specific game port is used on client side.
	 */
	SET_GAME_PORT_USED,

	/**
	 * Code when the identifier cannot be parsed.
	 */
	UNKNOWN;

	private static int codeGenerator;
	private static Map<Integer, Identifier> identifiers;
	private int code;
	private byte[] bytes;

	private Identifier() {
		this(generateCode());
	}

	private Identifier(int code) {
		this.code = code;
		bytes = ByteWrapper.create().putInt(code).get();
	}

	@Override
	public String toString() {
		return "MumbleIdentifier={" + super.toString() + "," + code + "}";
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getCode() {
		return code;
	}

	/**
	 * Get the identifier associated to the given code.
	 * 
	 * @param code The code associated to the identifier to get.
	 * 
	 * @return The identifier associated to the given code or UNKNOWN if there is no identifier associated to the given code.
	 */
	public static Identifier fromCode(int code) {
		Identifier identifier = identifiers.get(code);
		return identifier == null ? UNKNOWN : identifier;
	}

	private static int generateCode() {
		return codeGenerator++;
	}

	static {
		identifiers = new HashMap<Integer, Identifier>();
		for (Identifier identifier : values())
			identifiers.put(identifier.getCode(), identifier);
	}
}
