package fr.pederobien.mumble.common.impl;

import java.nio.ByteBuffer;

public enum ErrorCode {
	// Code when no errors happened.
	NONE(1, "No error."),

	// Code when a timeout occurs.
	TIMEOUT(2, "Request times out"),

	// Code when player has not the permission to send the request
	PERMISSION_REFUSED(3, "Permission refused."),

	// Code when an unexpected error attempt on the server.
	UNEXPECTED_ERROR(4, "An unexpected error occurs."),

	// Code when incompatible idc and oid
	INCOMPATIBLE_IDC_OID(5, "The idc and the oid are incompatibles."),

	// Code when there are no treatment associated to the given idc.
	IDC_UNKNOWN(6, "There is no treatment associated to the given idc."),

	// Code when trying to add a channel whose name is already used.
	CHANNEL_ALREADY_EXISTS(7, "The channel already exists."),

	// Code when trying to remove a not existing channel.
	CHANNEL_DOES_NOT_EXISTS(8, "The channel does not exists."),

	// Code when the player is not recognized.
	PLAYER_NOT_RECOGNIZED(9, "The player is not recognized."),

	// Code when trying to add an already registered player in a channel.
	PLAYER_ALREADY_REGISTERED(10, "The player is already registered in a channel."),

	// Code when player tries to mute/unmute another player that is not in same channel.
	PLAYERS_IN_DIFFERENT_CHANNELS(11, "A player can only mute or unmute another player that is in the same channel as him"),

	// Code when player is not registered in a channel.
	PLAYER_NOT_REGISTERED(12, "The player is not registered in a channel"),

	// Code when the sound modifier does not exist.
	SOUND_MODIFIER_DOES_NOT_EXIST(13, "The sound modifier does not exist"),

	// Code when cannot be parsed.
	UNKNOWN(-1, "Cannot parse the error code.");

	private int code;
	private String message;
	private byte[] bytes;

	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
		bytes = ByteBuffer.allocate(4).putInt(code).array();
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
}
