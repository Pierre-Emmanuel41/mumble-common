package fr.pederobien.mumble.common.impl.model;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.pederobien.mumble.common.impl.model.ChannelInfo.SemiFullChannelInfo;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;

public class ServerInfo {
	private Map<String, FullSoundModifierInfo> soundModifierInfo;
	private Map<String, SemiFullChannelInfo> channelInfo;
	private Map<String, FullPlayerInfo> playerInfo;

	public ServerInfo() {
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
