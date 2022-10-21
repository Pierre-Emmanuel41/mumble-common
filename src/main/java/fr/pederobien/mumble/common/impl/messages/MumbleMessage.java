package fr.pederobien.mumble.common.impl.messages;

import fr.pederobien.messenger.impl.Message;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;
import fr.pederobien.mumble.common.interfaces.IMumbleHeader;
import fr.pederobien.mumble.common.interfaces.IMumbleMessage;

public abstract class MumbleMessage extends Message implements IMumbleMessage {

	/**
	 * The index at which there is the first byte of the message identifier.
	 */
	public static final int IDENTIFIER_INDEX = 12;

	/**
	 * The index at which there is the first byte of the error code.
	 */
	public static final int ERROR_CODE_INDEX = 16;

	/**
	 * The index at which there is the first byte of the message properties length.
	 */
	public static final int LENGTH_INDEX = 20;

	/**
	 * Creates a mumble message represented by a name and a mumble header. The message name is used for storage only but is never used
	 * during the bytes generation.
	 * 
	 * @param mumbleIdentifier The message identifier.
	 * @param header     The message header.
	 */
	protected MumbleMessage(MumbleIdentifier mumbleIdentifier, IMumbleHeader header) {
		super(mumbleIdentifier.name(), header);
	}

	@Override
	public IMumbleHeader getHeader() {
		return (IMumbleHeader) super.getHeader();
	}
}
