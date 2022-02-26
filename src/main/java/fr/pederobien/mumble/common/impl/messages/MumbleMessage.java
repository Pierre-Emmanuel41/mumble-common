package fr.pederobien.mumble.common.impl.messages;

import fr.pederobien.messenger.impl.Message;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.mumble.common.interfaces.IMumbleMessage;

public abstract class MumbleMessage extends Message implements IMumbleMessage {

	/**
	 * Creates a mumble message represented by a name and a mumble header. The message name is used for storage only but is never used
	 * during the bytes generation.
	 * 
	 * @param name   The message name.
	 * @param header The message header.
	 */
	protected MumbleMessage(String name, IMumbleHeader header) {
		super(name, header);
	}

	@Override
	public IMumbleHeader getHeader() {
		return (IMumbleHeader) super.getHeader();
	}
}
