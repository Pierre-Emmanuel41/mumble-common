package fr.pederobien.mumble.common.interfaces;

import fr.pederobien.messenger.interfaces.IMessage;

public interface IMumbleMessage extends IMessage {

	/**
	 * @return The header associated to this message.
	 */
	IMumbleHeader getHeader();
}
