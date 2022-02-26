package fr.pederobien.mumble.common.impl.model;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.mumble.common.impl.model.PlayerInfo.SimplePlayerInfo;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.LazySoundModifierInfo;

public class ChannelInfo {

	public static class LazyChannelInfo {
		private String name;
		private LazySoundModifierInfo soundModifierInfo;

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public LazyChannelInfo(String name, LazySoundModifierInfo soundModifierInfo) {
			this.name = name;
			this.soundModifierInfo = soundModifierInfo;
		}

		/**
		 * @return The channel name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return The sound modifier description.
		 */
		public LazySoundModifierInfo getSoundModifierInfo() {
			return soundModifierInfo;
		}
	}

	public static class SimpleChannelInfo extends LazyChannelInfo {
		private List<SimplePlayerInfo> playerInfo;

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public SimpleChannelInfo(String name, LazySoundModifierInfo soundModifierInfo) {
			super(name, soundModifierInfo);

			playerInfo = new ArrayList<SimplePlayerInfo>();
		}

		/**
		 * @return A description of each player registered in this channel.
		 */
		public List<SimplePlayerInfo> getPlayerInfo() {
			return playerInfo;
		}
	}

	public static class FullChannelInfo extends LazyChannelInfo {
		private List<StatusPlayerInfo> playerInfo;

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public FullChannelInfo(String name, LazySoundModifierInfo soundModifierInfo) {
			super(name, soundModifierInfo);

			playerInfo = new ArrayList<StatusPlayerInfo>();
		}

		/**
		 * @return A description of each player registered in this channel.
		 */
		public List<StatusPlayerInfo> getPlayerInfo() {
			return playerInfo;
		}
	}
}
