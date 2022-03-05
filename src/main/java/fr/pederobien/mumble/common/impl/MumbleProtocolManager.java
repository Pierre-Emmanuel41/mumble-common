package fr.pederobien.mumble.common.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fr.pederobien.messenger.impl.ProtocolManager;
import fr.pederobien.messenger.interfaces.IHeader;
import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.messenger.interfaces.IProtocol;
import fr.pederobien.mumble.common.impl.messages.v10.ProtocolV10;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;

public class MumbleProtocolManager {

	/**
	 * The name of the message to get the complete server configuration.
	 */
	public static final String SERVER_INFO_GET = "ServerInfoGet";

	/*
	 * The name of the message in order to join a mumble server.
	 */
	public static final String SERVER_JOIN_INFO = "ServerJoinInfo";

	/**
	 * The name of the message to leave a mumble server.
	 */
	public static final String SERVER_LEAVE_INFO = "ServerLeaveInfo";

	/**
	 * The name of the message to get information about a player.
	 */
	public static final String PLAYER_GET = "PlayerGet";

	/**
	 * The name of the message to set the information about a player.
	 */
	public static final String PLAYER_SET = "PlayerSet";

	/**
	 * The name of the message to add a player.
	 */
	public static final String PLAYER_ADD = "PlayerAdd";

	/**
	 * The name of the message to remove a player.
	 */
	public static final String PLAYER_REMOVE = "PlayerRemove";

	/**
	 * The name of the message to change the online status of a player.
	 */
	public static final String PLAYER_ONLINE_GET = "PlayerOnlineGet";

	/**
	 * The name of the message to change the online status of a player.
	 */
	public static final String PLAYER_ONLINE_SET = "PlayerOnlineSet";

	/**
	 * The name of the message to set the name of a player.
	 */
	public static final String PLAYER_NAME_SET = "PlayerNameSet";

	/**
	 * The name of the message to get the game address of a player.
	 */
	public static final String PLAYER_GAME_ADDRESS_GET = "PlayerGameAddressGet";

	/**
	 * The name of the message to get the game address of a player.
	 */
	public static final String PLAYER_GAME_ADDRESS_SET = "PlayerGameAddressSet";

	/**
	 * The name of the message to change the admin status of a player.
	 */
	public static final String PLAYER_ADMIN_SET = "PlayerAdminSet";

	/**
	 * The name of the message to dispatch an audio sample.
	 */
	public static final String PLAYER_SPEAK_INFO = "PlayerSpeakInfo";

	/**
	 * The name of the message to player an audio sample.
	 */
	public static final String PLAYER_SPEAK_SET = "PlayerSpeakSet";

	/**
	 * The name of the message to mute or unmute a player.
	 */
	public static final String PLAYER_MUTE_SET = "PlayerMuteSet";

	/**
	 * The name of the message to mute a player for another player.
	 */
	public static final String PLAYER_MUTE_BY_SET = "PlayerMuteBySet";

	/**
	 * The name of the message to deafen or undeafen a player.
	 */
	public static final String PLAYER_DEAFEN_SET = "PlayerDeafenSet";

	/**
	 * The name of the message to kick a player from a channel.
	 */
	public static final String PLAYER_KICK_SET = "KickPlayerSet";

	/**
	 * The name of the message to get the position of a player.
	 */
	public static final String PLAYER_POSITION_GET = "PlayerPositionGet";

	/**
	 * The name of the message to set the position of a player.
	 */
	public static final String PLAYER_POSITION_SET = "PlayerPositionSet";

	/**
	 * The name of the message to get information about each channels.
	 */
	public static final String CHANNELS_GET = "ChannelsGet";

	/**
	 * The name of the message to add a channel on the mumble server.
	 */
	public static final String CHANNELS_ADD = "ChannelsAdd";

	/**
	 * The name of the message to remove a channel from the mumble server.
	 */
	public static final String CHANNELS_REMOVE = "ChannelsRemove";

	/**
	 * The name of the message to change the name of a channel.
	 */
	public static final String CHANNELS_SET = "ChannelsSet";

	/**
	 * The name of the message to add a player to a channel.
	 */
	public static final String CHANNELS_PLAYER_ADD = "ChannelsPlayerAdd";

	/**
	 * The name of the message to add a player to a channel.
	 */
	public static final String CHANNELS_PLAYER_REMOVE = "ChannelsPlayerRemove";

	/**
	 * The name of the message to get the sound modifier associated to a channel.
	 */
	public static final String SOUND_MODIFIER_GET = "SoundModifierGet";

	/**
	 * The name of the message to change the sound modifier of a channel.
	 */
	public static final String SOUND_MODIFIER_SET = "SoundModifierSet";

	/**
	 * The name of the message to get a description of all sound modifiers registered.
	 */
	public static final String SOUND_MODIFIER_INFO = "SoundModifierInfo";

	/**
	 * The name of the message to check a specific game port on client side.
	 */
	public static final String GAME_PORT_GET = "GamePortGet";

	/**
	 * The name of the message to specify if a specific game port is used on client side.
	 */
	public static final String GAME_PORT_SET = "GamePortSet";

	private ProtocolManager manager;
	private Map<Idc, Map<Oid, String>> associations;
	private Function<IHeader, String> parser;
	private float version;
	private IProtocol protocol;

	/**
	 * Creates a protocol manager associated to the mumble communication protocol.
	 * 
	 * @param beginIdentifier The first identifier from which next messages identifier are incremented by 1.
	 */
	public MumbleProtocolManager(int beginIdentifier) {
		parser = header -> {
			IMumbleHeader mumbleHeader = (IMumbleHeader) header;
			return associations.get(mumbleHeader.getIdc()).get(mumbleHeader.getOid());
		};
		manager = new ProtocolManager(beginIdentifier, version -> new MumbleHeader(version), parser);

		initializeAssociations();
		initializeProtocols();

		version = 1.0f;
		protocol = manager.getProtocol(version).get();
	}

	/**
	 * Create a message based on the given parameters.
	 * 
	 * @param idc       The message idc.
	 * @param oid       The message oid.
	 * @param errorCode The message errorCode.
	 * @param payload   The message payload.
	 * 
	 * @return The created message.
	 */
	public IMessage create(Idc idc, Oid oid, ErrorCode errorCode, Object... payload) {
		IMessage message = protocol.get(associations.get(idc).get(oid));
		if (message == null)
			return null;

		message.getHeader().setProperties(idc, oid, errorCode);
		message.setProperties(payload);
		return message;
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. Neither the identifier nor the header are
	 * modified.
	 * 
	 * @param message    The message to answer.
	 * @param properties The response properties.
	 * 
	 * @return A new message.
	 */
	public IMessage answer(IMessage message, Object... properties) {
		return protocol.answer(message, properties);
	}

	/**
	 * Creates a new message corresponding to the answer of the <code>message</code>. The identifier is not incremented.
	 * 
	 * @param message    The message to answer.
	 * @param idc        The response IDC.
	 * @param oid        The response OID.
	 * @param errorCode  The response ErrorCode.
	 * @param properties The response properties.
	 * 
	 * @return The message associated to the answer.
	 */
	public IMessage answer(int identifier, Idc idc, Oid oid, ErrorCode errorCode, Object... properties) {
		IMessage message = protocol.get(associations.get(idc).get(oid));
		if (message == null)
			return null;

		message.getHeader().setIdentifier(identifier);
		message.getHeader().setProperties(idc, oid, errorCode);
		message.setProperties(properties);
		return message;
	}

	/**
	 * @return The protocol manager in order to generate/parse messages.
	 */
	public ProtocolManager getManager() {
		return manager;
	}

	/**
	 * @return The latest protocol version.
	 */
	public IProtocol getProtocol() {
		return protocol;
	}

	private void initializeAssociations() {
		associations = new HashMap<Idc, Map<Oid, String>>();

		// Server info map
		Map<Oid, String> serverInfoMap = new HashMap<Oid, String>();
		serverInfoMap.put(Oid.INFO, SERVER_INFO_GET);
		associations.put(Idc.SERVER_INFO, serverInfoMap);

		// Server join map
		Map<Oid, String> serverJoinMap = new HashMap<Oid, String>();
		serverJoinMap.put(Oid.INFO, SERVER_JOIN_INFO);
		associations.put(Idc.SERVER_JOIN, serverJoinMap);

		// Server leave map
		Map<Oid, String> serverLeaveMap = new HashMap<Oid, String>();
		serverLeaveMap.put(Oid.INFO, SERVER_LEAVE_INFO);
		associations.put(Idc.SERVER_LEAVE, serverLeaveMap);

		// Player map
		Map<Oid, String> playerInfoMap = new HashMap<Oid, String>();
		playerInfoMap.put(Oid.GET, PLAYER_GET);
		playerInfoMap.put(Oid.SET, PLAYER_SET);
		playerInfoMap.put(Oid.ADD, PLAYER_ADD);
		associations.put(Idc.PLAYER, playerInfoMap);

		// Player online map
		Map<Oid, String> playerOnlineMap = new HashMap<Oid, String>();
		playerOnlineMap.put(Oid.GET, PLAYER_ONLINE_GET);
		playerOnlineMap.put(Oid.SET, PLAYER_ONLINE_SET);
		associations.put(Idc.PLAYER_ONLINE, playerOnlineMap);

		// Player name map
		Map<Oid, String> playerNameMap = new HashMap<Oid, String>();
		playerNameMap.put(Oid.SET, PLAYER_NAME_SET);
		associations.put(Idc.PLAYER_NAME, playerNameMap);

		// Player game address map
		Map<Oid, String> playerGameAddressMap = new HashMap<Oid, String>();
		playerGameAddressMap.put(Oid.GET, PLAYER_GAME_ADDRESS_GET);
		playerGameAddressMap.put(Oid.SET, PLAYER_GAME_ADDRESS_SET);
		associations.put(Idc.PLAYER_ONLINE, playerGameAddressMap);

		// Player admin map
		Map<Oid, String> playerAdminMap = new HashMap<Oid, String>();
		playerAdminMap.put(Oid.SET, PLAYER_ADMIN_SET);
		associations.put(Idc.PLAYER_ADMIN, playerAdminMap);

		// Channels map
		Map<Oid, String> channelsMap = new HashMap<Oid, String>();
		channelsMap.put(Oid.GET, CHANNELS_GET);
		channelsMap.put(Oid.ADD, CHANNELS_ADD);
		channelsMap.put(Oid.REMOVE, CHANNELS_REMOVE);
		channelsMap.put(Oid.SET, CHANNELS_SET);
		associations.put(Idc.CHANNELS, channelsMap);

		// Channels player map
		Map<Oid, String> channelsPlayerMap = new HashMap<Oid, String>();
		channelsPlayerMap.put(Oid.ADD, CHANNELS_PLAYER_ADD);
		channelsPlayerMap.put(Oid.REMOVE, CHANNELS_PLAYER_REMOVE);
		associations.put(Idc.CHANNELS_PLAYER, channelsPlayerMap);

		// Player speak map
		Map<Oid, String> playerSpeakMap = new HashMap<Oid, String>();
		playerSpeakMap.put(Oid.INFO, PLAYER_SPEAK_INFO);
		playerSpeakMap.put(Oid.SET, PLAYER_SPEAK_SET);
		associations.put(Idc.PLAYER_SPEAK, playerSpeakMap);

		// Player mute map
		Map<Oid, String> playerMuteMap = new HashMap<Oid, String>();
		playerMuteMap.put(Oid.SET, PLAYER_MUTE_SET);
		associations.put(Idc.PLAYER_MUTE, playerMuteMap);

		// Player mute by map
		Map<Oid, String> playerMuteByMap = new HashMap<Oid, String>();
		playerMuteByMap.put(Oid.SET, PLAYER_MUTE_BY_SET);
		associations.put(Idc.PLAYER_MUTE_BY, playerMuteByMap);

		// Player deafen map
		Map<Oid, String> playerDeafenMap = new HashMap<Oid, String>();
		playerDeafenMap.put(Oid.SET, PLAYER_DEAFEN_SET);
		associations.put(Idc.PLAYER_DEAFEN, playerDeafenMap);

		// Player kick map
		Map<Oid, String> playerKickMap = new HashMap<Oid, String>();
		playerKickMap.put(Oid.SET, PLAYER_KICK_SET);
		associations.put(Idc.PLAYER_KICK, playerKickMap);

		// Sound modifier map
		Map<Oid, String> soundModifierMap = new HashMap<Oid, String>();
		soundModifierMap.put(Oid.GET, SOUND_MODIFIER_GET);
		soundModifierMap.put(Oid.SET, SOUND_MODIFIER_SET);
		soundModifierMap.put(Oid.INFO, SOUND_MODIFIER_INFO);
		associations.put(Idc.SOUND_MODIFIER, soundModifierMap);

		// Player position map
		Map<Oid, String> playerPositionMap = new HashMap<Oid, String>();
		playerPositionMap.put(Oid.GET, PLAYER_POSITION_GET);
		playerPositionMap.put(Oid.SET, PLAYER_POSITION_SET);
		associations.put(Idc.PLAYER_POSITION, playerPositionMap);

		// Game port map
		Map<Oid, String> gamePortMap = new HashMap<Oid, String>();
		gamePortMap.put(Oid.GET, GAME_PORT_GET);
		gamePortMap.put(Oid.SET, GAME_PORT_SET);
		associations.put(Idc.GAME_PORT, gamePortMap);
	}

	private void initializeProtocols() {
		IProtocol protocolV10 = new ProtocolV10(this);
		manager.register(protocolV10.getVersion(), protocolV10);
	}
}
