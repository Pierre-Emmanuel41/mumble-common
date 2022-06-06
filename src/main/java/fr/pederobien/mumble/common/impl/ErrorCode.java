package fr.pederobien.mumble.common.impl;

import fr.pederobien.utils.ByteWrapper;

public enum ErrorCode {
	// Code when no errors happened.
	NONE("No error."),

	// Code when a timeout occurs.
	TIMEOUT("Request times out"),

	// Code when player has not the permission to send the request
	PERMISSION_REFUSED("Permission refused."),

	// Code when an unexpected error attempt on the server.
	UNEXPECTED_ERROR("An unexpected error occurs."),

	// Code when the request is malformed
	REQUEST_MALFORMED("The request is malformed."),

	// Code when the version is not supported.
	INCOMPATIBLE_VERSION("The protocol version is not supported"),

	// Code when there are no treatment associated to the given identifier.
	IDENTIFIER_UNKNOWN("There is no treatment associated to the given identifier."),

	// Code when server plugins cancelled a request.
	REQUEST_CANCELLED("The request has been cancelled by the server"),

	// Code when a client try to join a server whereas it has already joined the server
	SERVER_ALREADY_JOINED("The client has already joined the server"),

	// Code when trying to add a channel whose name is already used.
	CHANNEL_ALREADY_EXISTS("The channel already exists."),

	// Code the channel does not exist.
	CHANNEL_NOT_FOUND("The channel does not exists."),

	// Code when the player does not exist.
	PLAYER_NOT_FOUND("The player does not exist."),

	// Code when the player involved in the request does not match with the player's mumble client
	PLAYER_DOES_NOT_MATCH("The player involved in the request does not match with the player hosted by the client"),

	// Code when the client of a player has not joined the server yet
	PLAYER_CLIENT_NOT_JOINED("The client of the player has not joined the server yet"),

	// Code when trying to rename a player
	PLAYER_ALREADY_EXISTS("The player already exist"),

	// Code when trying to add an already registered player in a channel.
	PLAYER_ALREADY_REGISTERED("The player is already registered in a channel."),

	// Code when player tries to mute/unmute another player that is not in same channel.
	PLAYERS_IN_DIFFERENT_CHANNELS("A player can only mute or unmute another player that is in the same channel as him"),

	// Code when player is not registered in a channel.
	PLAYER_NOT_REGISTERED("The player is not registered in a channel"),

	// Code when the parameter of a sound modifier does not exist.
	PARAMETER_NOT_FOUND("The parameter does not exist"),

	// Code when there is no minimum defined for a parameter.
	PARAMETER_WITHOUT_MIN("The parameter has no defined minimum"),

	// Code when there is no minimum defined for a parameter.
	PARAMETER_WITHOUT_MAX("The parameter has no defined maximum"),

	// Code when the sound modifier does not exist.
	SOUND_MODIFIER_DOES_NOT_EXIST("The sound modifier does not exist"),

	// Code when cannot be parsed.
	UNKNOWN(-1, "Cannot parse the error code.");

	private static int codeGenerator;
	private int code;
	private String message;
	private byte[] bytes;

	private ErrorCode(String message) {
		this(generateCode(), message);
	}

	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
		bytes = ByteWrapper.create().putInt(code).get();
	}

	@Override
	public String toString() {
		return "ErrorCode={" + super.toString() + "," + code + "," + message + "}";
	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Get the ErrorCode code associated to the given code.
	 * 
	 * @param code The code used as key to get the associated ErrorCode.
	 * 
	 * @return The ErrorCode associated to the given code or UNKNOWN if there is no ErrorCode associated to the given code.
	 */
	public static ErrorCode fromCode(int code) {
		for (ErrorCode errorCode : values())
			if (errorCode.getCode() == code)
				return errorCode;
		return UNKNOWN;
	}

	private static int generateCode() {
		return codeGenerator++;
	}
}
