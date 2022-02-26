package fr.pederobien.mumble.common.interfaces;

import fr.pederobien.messenger.interfaces.IHeader;
import fr.pederobien.mumble.common.impl.ErrorCode;
import fr.pederobien.mumble.common.impl.Idc;
import fr.pederobien.mumble.common.impl.Oid;

public interface IMumbleHeader extends IHeader {

	/**
	 * @return The header idc.
	 */
	Idc getIdc();

	/**
	 * @return The header oid.
	 */
	Oid getOid();

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
