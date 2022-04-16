package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.impl.MessageCreator;
import fr.pederobien.messenger.impl.Protocol;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class ProtocolV10 extends Protocol {

	/**
	 * Creates a protocol associated to the version 1.0.
	 * 
	 * @param mumbleManager The manager associated to this protocol.
	 */
	public ProtocolV10(MumbleProtocolManager mumbleManager) {
		super(mumbleManager.getManager().getSequence(), 1.0f, mumbleManager.getManager().getHeader(), mumbleManager.getManager().getParser());

		// Server messages
		register(new MessageCreator(MumbleProtocolManager.SERVER_INFO_GET, header -> new ServerInfoGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.SERVER_JOIN_INFO, header -> new ServerJoinInfoMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.SERVER_LEAVE_INFO, header -> new ServerLeaveInfoMessageV10((IMumbleHeader) header)));

		// Player messages
		register(new MessageCreator(MumbleProtocolManager.PLAYER_GET, header -> new PlayerGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_SET, header -> new PlayerSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_ADD, header -> new PlayerAddMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_REMOVE, header -> new PlayerRemoveMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_ONLINE_GET, header -> new PlayerOnlineGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_ONLINE_SET, header -> new PlayerOnlineSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_NAME_SET, header -> new PlayerNameSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_GAME_ADDRESS_GET, header -> new PlayerGameAddressGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_GAME_ADDRESS_SET, header -> new PlayerGameAddressSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_ADMIN_SET, header -> new PlayerAdminSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_MUTE_SET, header -> new PlayerMuteSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_DEAFEN_SET, header -> new PlayerDeafenSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_MUTE_BY_SET, header -> new PlayerMuteBySetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_SPEAK_INFO, header -> new PlayerSpeakInfoMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_SPEAK_SET, header -> new PlayerSpeakSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_KICK_SET, header -> new PlayerKickSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_POSITION_GET, header -> new PlayerPositionGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PLAYER_POSITION_SET, header -> new PlayerPositionSetMessageV10((IMumbleHeader) header)));

		// Channel messages
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_GET, header -> new ChannelsGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_ADD, header -> new ChannelsAddMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_REMOVE, header -> new ChannelsRemoveMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_SET, header -> new ChannelsSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_PLAYER_ADD, header -> new ChannelsPlayerAddMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.CHANNELS_PLAYER_REMOVE, header -> new ChannelsPlayerRemoveMessageV10((IMumbleHeader) header)));

		// Parameter message
		register(new MessageCreator(MumbleProtocolManager.PARAMETER_VALUE_SET, header -> new ParameterValueSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PARAMETER_MIN_VALUE_SET, header -> new ParameterMinValueSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.PARAMETER_MAX_VALUE_SET, header -> new ParameterMaxValueSetMessageV10((IMumbleHeader) header)));

		// Sound modifier messages
		register(new MessageCreator(MumbleProtocolManager.SOUND_MODIFIER_GET, header -> new SoundModifierGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.SOUND_MODIFIER_SET, header -> new SoundModifierSetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.SOUND_MODIFIER_INFO, header -> new SoundModifierInfoMessageV10((IMumbleHeader) header)));

		// Game port messages
		register(new MessageCreator(MumbleProtocolManager.GAME_PORT_GET, header -> new GamePortGetMessageV10((IMumbleHeader) header)));
		register(new MessageCreator(MumbleProtocolManager.GAME_PORT_SET, header -> new GamePortSetMessageV10((IMumbleHeader) header)));
	}
}
