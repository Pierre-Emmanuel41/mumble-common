package fr.pederobien.mumble.common.impl.model;

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
}
