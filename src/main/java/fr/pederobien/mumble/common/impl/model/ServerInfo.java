package fr.pederobien.mumble.common.impl.model;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.mumble.common.impl.model.ChannelInfo.SimpleChannelInfo;
import fr.pederobien.mumble.common.impl.model.PlayerInfo.FullPlayerInfo;
import fr.pederobien.mumble.common.impl.model.SoundModifierInfo.FullSoundModifierInfo;

public class ServerInfo {
	private List<FullSoundModifierInfo> soundModifierInfo;
	private List<SimpleChannelInfo> channelInfo;
	private List<FullPlayerInfo> playerInfo;

	public ServerInfo() {
		soundModifierInfo = new ArrayList<FullSoundModifierInfo>();
		channelInfo = new ArrayList<SimpleChannelInfo>();
		playerInfo = new ArrayList<FullPlayerInfo>();
	}

	/**
	 * @return A list that contains a description of each sound modifier.
	 */
	public List<FullSoundModifierInfo> getSoundModifierInfo() {
		return soundModifierInfo;
	}

	/**
	 * @return A list that contains a description of each channel.
	 */
	public List<SimpleChannelInfo> getChannelInfo() {
		return channelInfo;
	}

	/**
	 * @return A list that contains a description of each connected player.
	 */
	public List<FullPlayerInfo> getPlayerInfo() {
		return playerInfo;
	}
}
