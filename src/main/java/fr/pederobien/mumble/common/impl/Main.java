package fr.pederobien.mumble.common.impl;

import fr.pederobien.messenger.interfaces.IMessage;
import fr.pederobien.utils.ByteWrapper;

public class Main {

	public static void main(String[] args) {
		IMessage<Header> message = MumbleMessageFactory.create(Idc.SOUND_MODIFIER, "Channel 1");
		System.out.println(ByteWrapper.wrap(message.getBytes()));

		IMessage<Header> messageParsed = MumbleMessageFactory.parse(message.getBytes());
		System.out.println(messageParsed);

		IMessage<Header> response = MumbleMessageFactory.create(Idc.SOUND_MODIFIER, "Channel 1", "Sound modifier 1");
		System.out.println(ByteWrapper.wrap(response.getBytes()));

		IMessage<Header> responseParsed = MumbleMessageFactory.parse(response.getBytes());
		System.out.println(responseParsed);

		message = MumbleMessageFactory.create(Idc.SOUND_MODIFIER, Oid.INFO);
		System.out.println(ByteWrapper.wrap(message.getBytes()));

		messageParsed = MumbleMessageFactory.parse(message.getBytes());
		System.out.println(messageParsed);

		response = MumbleMessageFactory.create(Idc.SOUND_MODIFIER, Oid.INFO, 2, "Sound modifier 1", "Sound modifier 2");
		System.out.println(ByteWrapper.wrap(response.getBytes()));

		responseParsed = MumbleMessageFactory.parse(response.getBytes());
		System.out.println(responseParsed);
	}
}
