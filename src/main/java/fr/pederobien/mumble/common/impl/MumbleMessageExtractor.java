package fr.pederobien.mumble.common.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.pederobien.communication.interfaces.IAnswersExtractor;
import fr.pederobien.messenger.impl.Header;
import fr.pederobien.mumble.common.impl.messages.MumbleMessage;

public class MumbleMessageExtractor implements IAnswersExtractor {
	private byte[] remaining = new byte[0];

	@Override
	public Map<Integer, byte[]> extract(byte[] received) {
		Map<Integer, byte[]> answers = new HashMap<Integer, byte[]>();
		byte[] toParse;
		if (remaining.length == 0)
			toParse = received;
		else {
			toParse = new byte[remaining.length + received.length];
			System.arraycopy(remaining, 0, toParse, 0, remaining.length);
			System.arraycopy(received, 0, toParse, remaining.length, received.length);
			remaining = new byte[0];
		}

		List<Integer> syncWordsIndex = new ArrayList<Integer>();
		for (int i = 0; i < toParse.length - 4; i++) {
			if (toParse[i] != 98 || toParse[i + 1] != 105 || toParse[i + 2] != 110 || toParse[i + 3] != 27)
				continue;
			syncWordsIndex.add(i);
		}

		List<byte[]> buffers = new ArrayList<byte[]>();
		if (!syncWordsIndex.isEmpty()) {
			for (int i = 0; i < syncWordsIndex.size() - 1; i++) {
				buffers.add(extract(toParse, syncWordsIndex.get(i), syncWordsIndex.get(i + 1) - syncWordsIndex.get(i)));
			}
			buffers.add(extract(toParse, syncWordsIndex.get(syncWordsIndex.size() - 1), toParse.length - syncWordsIndex.get(syncWordsIndex.size() - 1)));
		}

		for (int i = 0; i < buffers.size(); i++) {
			byte[] bytes = buffers.get(i);
			try {
				int identifier = getIdentifier(bytes);
				int length = getLength(bytes) + MumbleMessage.END_WORD.length;
				answers.put(identifier, extract(bytes, 0, length));
			} catch (IndexOutOfBoundsException e) {
				// If exception for the last index then last answer not complete.
				// Otherwise, mal formed answer.
				if (i == buffers.size() - 1)
					remaining = bytes;
			}
		}

		return answers;
	}

	private byte[] extract(byte[] buffer, int first, int length) {
		byte[] data = new byte[length];
		System.arraycopy(buffer, first, data, 0, length);
		return data;
	}

	private int getIdentifier(byte[] bytes) {
		return toInt(bytes, Header.SEQUENCE_NUMBER_INDEX);
	}

	private int getLength(byte[] bytes) {
		// SYNC_WORD + header length + properties length + properties
		return MumbleMessage.BEGIN_WORD.length + MumbleHeader.HEADER_LENGH + 4 + toInt(bytes, MumbleMessage.LENGTH_INDEX);
	}

	private int toInt(byte[] bytes, int first) {
		return ByteBuffer.wrap(extract(bytes, first, 4)).getInt();
	}
}
