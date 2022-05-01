package fr.pederobien.mumble.common.impl.messages.v10;

import fr.pederobien.messenger.impl.MessageCreator;
import fr.pederobien.messenger.impl.Protocol;
import fr.pederobien.mumble.common.impl.Identifier;
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
		register(new MessageCreator(Identifier.GET_FULL_SERVER_CONFIGURATION.name(), header -> new GetFullServerConfigurationV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_SERVER_CONFIGURATION.name(), header -> new GetServerConfigurationV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_SERVER_JOIN.name(), header -> new SetServerJoinV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_SERVER_LEAVE.name(), header -> new SetServerLeaveV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_CP_VERSIONS.name(), header -> new GetCommunicationProtocolVersionsV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_CP_VERSION.name(), header -> new SetCommunicationProtocolVersionV10((IMumbleHeader) header)));

		// Player messages
		register(new MessageCreator(Identifier.GET_PLAYER_INFO.name(), header -> new GetPlayerInfoV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.REGISTER_PLAYER_ON_SERVER.name(), header -> new RegisterPlayerOnServerV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.UNREGISTER_PLAYER_FROM_SERVER.name(), header -> new UnregisterPlayerFromServerV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_ONLINE_STATUS.name(), header -> new GetPlayerOnlineStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_ONLINE_STATUS.name(), header -> new SetPlayerOnlineStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_NAME.name(), header -> new SetPlayerNameV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_GAME_ADDRESS.name(), header -> new GetPlayerGameAddressV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_GAME_ADDRESS.name(), header -> new SetPlayerGameAddressV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_ADMINISTRATOR.name(), header -> new GetPlayerAdministratorStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_ADMINISTRATOR.name(), header -> new GetPlayerAdministratorStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.PLAYER_SPEAK_INPUT.name(), header -> new PlayerSpeakInputV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.PLAYER_SPEAK_OUTPUT.name(), header -> new PlayerSpeakOutputV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_MUTE.name(), header -> new GetPlayerMuteStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_MUTE.name(), header -> new SetPlayerMuteStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_MUTE_BY.name(), header -> new SetPlayerMuteByStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_DEAFEN.name(), header -> new GetPlayerDeafenStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_DEAFEN.name(), header -> new SetPlayerDeafenStatusV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.KICK_PLAYER_FROM_CHANNEL.name(), header -> new KickPlayerFromChannelV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PLAYER_POSITION.name(), header -> new GetPlayerPositionV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PLAYER_POSITION.name(), header -> new SetPlayerPositionV10((IMumbleHeader) header)));

		// Channel messages
		register(new MessageCreator(Identifier.GET_CHANNELS_INFO.name(), header -> new GetChannelsInfoV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_CHANNEL_INFO.name(), header -> new GetChannelInfoV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.REGISTER_CHANNEL_ON_THE_SERVER.name(), header -> new RegisterChannelOnServerV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.UNREGISTER_CHANNEL_FROM_SERVER.name(), header -> new UnregisterChannelFromServerV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_CHANNEL_NAME.name(), header -> new SetChannelNameV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.ADD_PLAYER_TO_CHANNEL.name(), header -> new AddPlayerToChannelV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.REMOVE_PLAYER_FROM_CHANNEL.name(), header -> new RemovePlayerFromChannelV10((IMumbleHeader) header)));

		// Parameter message
		register(new MessageCreator(Identifier.GET_PARAMETER_VALUE.name(), header -> new GetParameterValueV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PARAMETER_VALUE.name(), header -> new SetParameterValueV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PARAMETER_MIN_VALUE.name(), header -> new GetParameterMinValueV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PARAMETER_MIN_VALUE.name(), header -> new SetParameterMinValueV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_PARAMETER_MAX_VALUE.name(), header -> new GetParameterMaxValueV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_PARAMETER_MAX_VALUE.name(), header -> new SetParameterMaxValueV10((IMumbleHeader) header)));

		// Sound modifier messages
		register(new MessageCreator(Identifier.GET_SOUND_MODIFIERS_INFO.name(), header -> new GetSoundModifiersInfoV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.GET_CHANNEL_SOUND_MODIFIER_INFO.name(), header -> new GetChannelSoundModifierV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_CHANNEL_SOUND_MODIFIER.name(), header -> new SetChannelSoundModifierV10((IMumbleHeader) header)));

		// Game port messages
		register(new MessageCreator(Identifier.IS_GAME_PORT_USED.name(), header -> new IsGamePortUsedV10((IMumbleHeader) header)));
		register(new MessageCreator(Identifier.SET_GAME_PORT_USED.name(), header -> new SetGamePortUsedV10((IMumbleHeader) header)));
	}
}
