package fr.pederobien.mumble.common.impl.messages.v10.model;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.SimplePlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.PlayerInfo.StatusPlayerInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.FullSoundModifierInfo;
import fr.pederobien.mumble.common.impl.messages.v10.model.SoundModifierInfo.SimpleSoundModifierInfo;

public class ChannelInfo<T extends SoundModifierInfo<?>, U extends PlayerInfo> {
	private String name;
	private T soundModifierInfo;
	private Map<String, U> playerInfo;

	protected ChannelInfo(String name, T soundModifierInfo) {
		this.name = name;
		this.soundModifierInfo = soundModifierInfo;

		playerInfo = new LinkedHashMap<String, U>();
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
	public T getSoundModifierInfo() {
		return soundModifierInfo;
	}

	/**
	 * @return A description of each player registered in this channel.
	 */
	public Map<String, U> getPlayerInfo() {
		return playerInfo;
	}

	public static class SimpleChannelInfo extends ChannelInfo<SimpleSoundModifierInfo, SimplePlayerInfo> {

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public SimpleChannelInfo(String name, SimpleSoundModifierInfo soundModifierInfo) {
			super(name, soundModifierInfo);
		}
	}

	public static class SemiFullChannelInfo extends ChannelInfo<FullSoundModifierInfo, StatusPlayerInfo> {

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public SemiFullChannelInfo(String name, FullSoundModifierInfo soundModifierInfo) {
			super(name, soundModifierInfo);
		}
	}

	public static class FullChannelInfo extends ChannelInfo<FullSoundModifierInfo, StatusPlayerInfo> {

		/**
		 * Creates a channel description.
		 * 
		 * @param name              The channel name.
		 * @param soundModifierInfo The sound modifier description.
		 */
		public FullChannelInfo(String name, FullSoundModifierInfo soundModifierInfo) {
			super(name, soundModifierInfo);
		}
	}
}
