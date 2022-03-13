package fr.pederobien.mumble.common.impl.model;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class PlayerInfo {

	public static class SimplePlayerInfo {
		private String name;

		/**
		 * Creates a player description containing the player name.
		 * 
		 * @param name The playerName.
		 */
		public SimplePlayerInfo(String name) {
			this.name = name;
		}

		/**
		 * @return The player name.
		 */
		public String getName() {
			return name;
		}
	}

	public static class StatusPlayerInfo extends SimplePlayerInfo {
		private boolean isMute, isDeafen;

		/**
		 * Creates a player description containing the name, the mute status and the deafen status.
		 * 
		 * @param name     The playerName.
		 * @param isMute   The player mute status.
		 * @param isDeafen The player deafen status.
		 */
		public StatusPlayerInfo(String name, boolean isMute, boolean isDeafen) {
			super(name);
			this.isMute = isMute;
			this.isDeafen = isDeafen;
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
	}

	public static class CoordinatePlayerInfo extends SimplePlayerInfo {
		private double x, y, z, yaw, pitch;

		/**
		 * Creates a player description.
		 * 
		 * @param name  The player name.
		 * @param x     The player x coordinate.
		 * @param y     The player y coordinate.
		 * @param z     The player z coordinate.
		 * @param yaw   The player yaw.
		 * @param pitch The player pitch.
		 */
		public CoordinatePlayerInfo(String name, double x, double y, double z, double yaw, double pitch) {
			super(name);
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
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

	public static class FullPlayerInfo extends SimplePlayerInfo {
		private boolean isOnline;
		private UUID identifier;
		private InetSocketAddress gameAddress;
		private boolean isAdmin;
		private boolean isMute;
		private boolean isDeafen;
		private double x, y, z, yaw, pitch;

		/**
		 * Creates a player description.
		 * 
		 * @param name        The player's name.
		 * @param isOnline    The player's online status.
		 * @param identifier  The player's identifier.
		 * @param gameAddress The player's address used to play to the game.
		 * @param isAdmin     The player's administrator status.
		 * @param isMute      The player's mute status.
		 * @param isDeafen    The player's deafen status.
		 * @param x           The player's x coordinate.
		 * @param y           The player's y coordinate.
		 * @param z           The player's z coordinate.
		 * @param yaw         The player's yaw angle.
		 * @param pitch       The player's pitch angle.
		 */
		public FullPlayerInfo(String name, boolean isOnline, UUID identifier, String address, int port, boolean isAdmin, boolean isMute, boolean isDeafen, double x,
				double y, double z, double yaw, double pitch) {
			super(name);
			this.isOnline = isOnline;
			this.identifier = identifier;
			this.isAdmin = isAdmin;
			this.isMute = isMute;
			this.isDeafen = isDeafen;
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;

			try {
				gameAddress = new InetSocketAddress(InetAddress.getByName(address), port);
			} catch (UnknownHostException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		/**
		 * @return The player's online status.
		 */
		public boolean isOnline() {
			return isOnline;
		}

		/**
		 * @return The player's identifier.
		 */
		public UUID getIdentifier() {
			return identifier;
		}

		/**
		 * @return The player's address used to play to the game.
		 */
		public InetSocketAddress getGameAddress() {
			return gameAddress;
		}

		/**
		 * @return The player's administrator status.
		 */
		public boolean isAdmin() {
			return isAdmin;
		}

		/**
		 * @return The player's mute status.
		 */
		public boolean isMute() {
			return isMute;
		}

		/**
		 * @return The player's deafen status.
		 */
		public boolean isDeafen() {
			return isDeafen;
		}

		/**
		 * @return The player's x coordinate.
		 */
		public double getX() {
			return x;
		}

		/**
		 * @return The player's y coordinate.
		 */
		public double getY() {
			return y;
		}

		/**
		 * @return The player's z coordinate.
		 */
		public double getZ() {
			return z;
		}

		/**
		 * @return The player's yaw angle.
		 */
		public double getYaw() {
			return yaw;
		}

		/**
		 * @return The player's pitch angle.
		 */
		public double getPitch() {
			return pitch;
		}
	}
}
