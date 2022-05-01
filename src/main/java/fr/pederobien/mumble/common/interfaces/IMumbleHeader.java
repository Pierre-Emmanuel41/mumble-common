package fr.pederobien.mumble.common.interfaces;

import fr.pederobien.messenger.interfaces.IHeader;
import fr.pederobien.mumble.common.impl.ErrorCode;
import fr.pederobien.mumble.common.impl.Identifier;

public interface IMumbleHeader extends IHeader {

	/**
	 * @return The mumble identifier of this message.
	 */
	Identifier getIdentifier();

	/**
	 * @return The header error code.
	 */
	ErrorCode getErrorCode();

	/**
	 * @return True if and only if the error code is not equals to {@link ErrorCode#NONE}.
	 */
	default boolean isError() {
		return getErrorCode() != ErrorCode.NONE;
	}
}
