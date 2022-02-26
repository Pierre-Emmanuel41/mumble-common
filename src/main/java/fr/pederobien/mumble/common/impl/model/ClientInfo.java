package fr.pederobien.mumble.common.impl.model;

import java.util.UUID;

public class ClientInfo {

	public static class FullClientInfo {
		private UUID identifier;
		private String mumbleAddress, gameAddress, playerName;
		private boolean isMumbleConnected, isOnline, isAdmin, isMute, isDeafen;
		private double x, y, z, yaw, pitch;
		private int mumblePort, gamePort;

		/**
		 * Creates a player description.
		 * 
		 * @param identifier    The client unique identifier.
		 * @param mumbleAddress The IP address used by the client to connect to mumble.
		 * @param mumblePort    The port number used by the client to connect to mumble.
		 * @param isOnline      True if the player is connected in game, false otherwise.
		 * @param gameAddress   The IP address used by the client to play to the game.
		 * @param gamePort      The port number used by the client to play to the game.
		 * @param playerName    The player name.
		 * @param isAdmin       True if the player is admin in game, false otherwise.
		 * @param isMute        True if the player is mute, false otherwise.
		 * @param isDeafen      True if the player is deafen, false otherwise.
		 * @param x             The player x coordinate.
		 * @param y             The player y coordinate.
		 * @param z             The player z coordinate.
		 * @param yaw           The player yaw.
		 * @param pitch         The player pitch.
		 */
		public FullClientInfo(UUID identifier, boolean isMumbleConnected, String mumbleAddress, int mumblePort, boolean isOnline, String gameAddress, int gamePort,
				String playerName, boolean isAdmin, boolean isMute, boolean isDeafen, double x, double y, double z, double yaw, double pitch) {
			this.identifier = identifier;
			this.isMumbleConnected = isMumbleConnected;
			this.mumbleAddress = mumbleAddress;
			this.mumblePort = mumblePort;
			this.isOnline = isOnline;
			this.gameAddress = gameAddress;
			this.gamePort = gamePort;
			this.playerName = playerName;
			this.isMute = isMute;
			this.isDeafen = isDeafen;
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
		}

		/**
		 * @return The client identifier.
		 */
		public UUID getIdentifier() {
			return identifier;
		}

		/**
		 * @return True if the client is connected on mumble side.
		 */
		public boolean isMumbleConnected() {
			return isMumbleConnected;
		}

		/**
		 * @return The address used by the client to connect to mumble.
		 */
		public String getMumbleAddress() {
			return mumbleAddress;
		}

		/**
		 * @return The port number used by the client to connect to mumble.
		 */
		public int getMumblePort() {
			return mumblePort;
		}

		/**
		 * @return True if the player is connected in game, false otherwise.
		 */
		public boolean isOnline() {
			return isOnline;
		}

		/**
		 * @return The address used by the client to play to the game.
		 */
		public String getGameAddress() {
			return gameAddress;
		}

		/**
		 * @return The port number used by the client to play to the game.
		 */
		public int getGamePort() {
			return gamePort;
		}

		/**
		 * @return The player name.
		 */
		public String getPlayerName() {
			return playerName;
		}

		/**
		 * @return True if the player is admin in game, false otherwise.
		 */
		public boolean isAdmin() {
			return isAdmin;
		}

		/**
		 * @return The player mute status.
		 */
		public boolean isMute() {
			return isMute;
		}

		/**
		 * @return The player deafen status.
		 */
		public boolean isDeafen() {
			return isDeafen;
		}

		/**
		 * @return The player X coordinate.
		 */
		public double getX() {
			return x;
		}

		/**
		 * @return The player X coordinate.
		 */
		public double getY() {
			return y;
		}

		/**
		 * @return The player Y coordinate.
		 */
		public double getZ() {
			return z;
		}

		/**
		 * @return The player yaw.
		 */
		public double getYaw() {
			return yaw;
		}

		/**
		 * @return The player pitch.
		 */
		public double getPitch() {
			return pitch;
		}
	}
}
