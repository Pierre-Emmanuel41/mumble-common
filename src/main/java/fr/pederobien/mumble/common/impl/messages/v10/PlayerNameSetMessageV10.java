package fr.pederobien.mumble.common.impl.messages.v10;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.mumble.common.impl.MumbleProtocolManager;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.utils.ByteWrapper;

public class PlayerNameSetMessageV10 extends MumbleMessage {
	private String oldName, newName;

	protected PlayerNameSetMessageV10(IMumbleHeader header) {
		super(MumbleProtocolManager.PLAYER_NAME_SET, header);
	}

	@Override
	public IMessage parse(byte[] payload) {
		if (getHeader().isError())
			return this;

		List<Object> properties = new ArrayList<Object>();
		int first = 0;
		ByteWrapper wrapper = ByteWrapper.wrap(payload);

		// Old player's name
		int oldNameLength = wrapper.getInt(first);
		first += 4;
		oldName = wrapper.getString(first, oldNameLength);
		properties.add(oldName);
		first += oldNameLength;

		// New player's name
		int newNameLength = wrapper.getInt(first);
		first += 4;
		newName = wrapper.getString(first, newNameLength);
		properties.add(newName);
		first += newNameLength;

		super.setProperties(properties.toArray());
		return null;
	}

	@Override
	public void setProperties(Object... properties) {
		super.setProperties(properties);

		if (getHeader().isError())
			return;

		oldName = (String) properties[0];
		newName = (String) properties[1];
	}

	@Override
	protected byte[] generateProperties() {
		ByteWrapper wrapper = ByteWrapper.create();

		if (getHeader().isError())
			return wrapper.get();

		// Old player's name
		wrapper.putString(oldName, true);

		// New player's name
		wrapper.putString(newName, true);

		return wrapper.get();
	}
}
