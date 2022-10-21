package fr.pederobien.mumble.common.interfaces;

import fr.pederobien.messenger.interfaces.IHeader;
import fr.pederobien.mumble.common.impl.MumbleErrorCode;
import fr.pederobien.mumble.common.impl.MumbleIdentifier;

public interface IMumbleHeader extends IHeader {

	/**
	 * @return The mumble identifier of this message.
	 */
	MumbleIdentifier getIdentifier();

	/**
	 * @return The header error code.
	 */
	MumbleErrorCode getErrorCode();

	/**
	 * @return True if and only if the error code is not equals to {@link MumbleErrorCode#NONE}.
	 */
	default boolean isError() {
		return getErrorCode() != MumbleErrorCode.NONE;
	}
}
