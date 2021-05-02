package fr.pederobien.mumble.common.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import fr.pederobien.messenger.interfaces.IMessageInterpreter;
import fr.pederobien.messenger.interfaces.InterpretersFactory;
import fr.pederobien.mumble.common.impl.interpreters.ChannelsInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.ChannelsPlayerInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerAdminInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerDeafenInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerKickInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerMuteByInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerMuteInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerSpeakInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.PlayerStatusInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.UUIDInterpreter;
import fr.pederobien.mumble.common.impl.interpreters.UdpPortInterpreter;

public class MumbleInterpretersFactory implements InterpretersFactory<Header> {
	private static final IMessageInterpreter DEFAULT_INTERPRETER = new DefaultInterpreter();
	private Map<Idc, Function<Header, IMessageInterpreter>> interpreters;

	public MumbleInterpretersFactory() {
		interpreters = new HashMap<Idc, Function<Header, IMessageInterpreter>>();

		interpreters.put(Idc.UNIQUE_IDENTIFIER, new UUIDInterpreter());
		interpreters.put(Idc.PLAYER_STATUS, new PlayerStatusInterpreter());
		interpreters.put(Idc.PLAYER_ADMIN, new PlayerAdminInterpreter());
		interpreters.put(Idc.CHANNELS, new ChannelsInterpreter());
		interpreters.put(Idc.CHANNELS_PLAYER, new ChannelsPlayerInterpreter());
		interpreters.put(Idc.UDP_PORT, new UdpPortInterpreter());
		interpreters.put(Idc.PLAYER_SPEAK, new PlayerSpeakInterpreter());
		interpreters.put(Idc.PLAYER_MUTE, new PlayerMuteInterpreter());
		interpreters.put(Idc.PLAYER_DEAFEN, new PlayerDeafenInterpreter());
		interpreters.put(Idc.PLAYER_MUTE_BY, new PlayerMuteByInterpreter());
		interpreters.put(Idc.PLAYER_KICK, new PlayerKickInterpreter());
	}

	@Override
	public IMessageInterpreter get(Header header) {
		Function<Header, IMessageInterpreter> interpreter = interpreters.get(header.getIdc());
		return interpreter == null ? DEFAULT_INTERPRETER : interpreter.apply(header);
	}

	private static class DefaultInterpreter implements IMessageInterpreter {

		@Override
		public byte[] generate(Object[] payload) {
			return new byte[0];
		}

		@Override
		public Object[] interprete(byte[] payload) {
			return new Object[0];
		}
	}
}
