package fr.pederobien.mumble.common.impl.messages.v10.model;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.pederobien.mumble.common.impl.messages.v10.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;

public class ServerInfo {

	public static class SimpleServerInfo extends ServerInfo {
		private FullPlayerInfo playerInfo;
		private Map<String, FullSoundModifierInfo> soundModifierInfo;
		private Map<String, SemiFullChannelInfo> channelInfo;

		public SimpleServerInfo(FullPlayerInfo playerInfo) {
			this.playerInfo = playerInfo;
			soundModifierInfo = new LinkedHashMap<String, FullSoundModifierInfo>();
			channelInfo = new LinkedHashMap<String, SemiFullChannelInfo>();
		}

		/**
		 * @return A complete description of the main player.
		 */
		public FullPlayerInfo getPlayerInfo() {
			return playerInfo;
		}

		/**
		 * @return A list that contains a description of each sound modifier.
		 */
		public Map<String, FullSoundModifierInfo> getSoundModifierInfo() {
			return soundModifierInfo;
		}

		/**
		 * @return A list that contains a description of each channel.
		 */
		public Map<String, SemiFullChannelInfo> getChannelInfo() {
			return channelInfo;
		}
	}

	public static class FullServerInfo extends ServerInfo {
		private Map<String, FullSoundModifierInfo> soundModifierInfo;
		private Map<String, SemiFullChannelInfo> channelInfo;
		private Map<String, FullPlayerInfo> playerInfo;

		public FullServerInfo() {
			soundModifierInfo = new LinkedHashMap<String, FullSoundModifierInfo>();
			channelInfo = new LinkedHashMap<String, SemiFullChannelInfo>();
			playerInfo = new LinkedHashMap<String, FullPlayerInfo>();
		}

		/**
		 * @return A list that contains a description of each sound modifier.
		 */
		public Map<String, FullSoundModifierInfo> getSoundModifierInfo() {
			return soundModifierInfo;
		}

		/**
		 * @return A list that contains a description of each channel.
		 */
		public Map<String, SemiFullChannelInfo> getChannelInfo() {
			return channelInfo;
		}

		/**
		 * @return A list that contains a description of each connected player.
		 */
		public Map<String, FullPlayerInfo> getPlayerInfo() {
			return playerInfo;
		}
	}
}
