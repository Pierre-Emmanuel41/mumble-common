package fr.pederobien.mumble.common.impl.interpreters;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.mumble.common.impl.ParameterType;
import fr.pederobien.utils.ByteWrapper;

public class SoundModifierInterpreter extends AbstractInterpreter {

	@Override
	protected byte[] internalGenerate(Object[] payload) {
		int currentIndex = 0;
		ByteWrapper wrapper = ByteWrapper.create();
		int numberOfParameters = 0;

		switch (getHeader().getOid()) {
		case GET:
		case SET:
			// Channel's name
			wrapper.putString((String) payload[currentIndex++], true);

			// When it is an answer
			if (payload.length > 1) {
				// Modifier's name
				wrapper.putString((String) payload[currentIndex++], true);

				// Number of parameters
				numberOfParameters = (int) payload[currentIndex++];
				wrapper.putInt(numberOfParameters);

				for (int i = 0; i < numberOfParameters; i++) {
					// Parameter's name
					wrapper.putString((String) payload[currentIndex++], true);

					// Parameter's type
					ParameterType<?> type = (ParameterType<?>) payload[currentIndex++];
					wrapper.putInt(type.getCode());

					// Parameter's value
					wrapper.put(type.getBytes(payload[currentIndex++]));
				}
			}
			return wrapper.get();
		case INFO:
			// Get all sound modifier registered on the server
			int numberOfModifiers = (int) payload[currentIndex++];
			wrapper.putInt(numberOfModifiers);

			for (int i = 0; i < numberOfModifiers; i++) {
				// Modifier's name
				wrapper.putString((String) payload[currentIndex++], true);

				// Number of parameter
				numberOfParameters = (int) payload[currentIndex++];
				wrapper.putInt(numberOfParameters);

				for (int j = 0; j < numberOfParameters; j++) {
					// Parameter's name
					wrapper.putString((String) payload[currentIndex++], true);

					// Parameter's type
					ParameterType<?> type = (ParameterType<?>) payload[currentIndex++];
					wrapper.putInt(type.getCode());

					// isRangeParameter
					boolean isRangeParameter = (boolean) payload[currentIndex++];
					wrapper.putInt(isRangeParameter ? 1 : 0);

					// Parameter's default value
					wrapper.put(type.getBytes(payload[currentIndex++]));

					// Parameter's value
					wrapper.put(type.getBytes(payload[currentIndex++]));

					if (isRangeParameter) {
						// Parameter's minimum value
						wrapper.put(type.getBytes(payload[currentIndex++]));

						// Parameter's maximum value
						wrapper.put(type.getBytes(payload[currentIndex++]));
					}
				}
			}
			return wrapper.get();
		default:
			return new byte[0];
		}
	}

	@Override
	protected Object[] internalInterprete(byte[] payload) {
		ByteWrapper wrapper = ByteWrapper.wrap(payload);
		List<Object> informations = new ArrayList<Object>();
		int channelNameLength, modifierNameLength, numberOfParameters;
		int first = 0;

		switch (getHeader().getOid()) {
		case GET:
		case SET:
			// Channel name
			channelNameLength = wrapper.getInt(first);
			first += 4;
			informations.add(wrapper.getString(first, channelNameLength));
			first += channelNameLength;

			if (first < payload.length) {
				// Modifier name
				modifierNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, modifierNameLength));
				first += modifierNameLength;

				// Number of parameters
				numberOfParameters = wrapper.getInt(first);
				first += 4;
				informations.add(numberOfParameters);

				for (int j = 0; j < numberOfParameters; j++) {
					// Parameter's name
					int parameterNameLength = wrapper.getInt(first);
					first += 4;
					informations.add(wrapper.getString(first, parameterNameLength));
					first += parameterNameLength;

					// Parameter's type
					int code = wrapper.getInt(first);
					first += 4;
					ParameterType<?> type = ParameterType.fromCode(code);
					informations.add(type);

					// Parameter's value
					informations.add(type.getValue(wrapper.extract(first, type.size())));
					first += type.size();
				}
			}
			return informations.toArray();
		case INFO:
			// Number of modifiers
			int numberOfModifiers = wrapper.getInt(first);
			informations.add(numberOfModifiers);
			first += 4;

			for (int i = 0; i < numberOfModifiers; i++) {
				// Modifier name
				modifierNameLength = wrapper.getInt(first);
				first += 4;
				informations.add(wrapper.getString(first, modifierNameLength));
				first += modifierNameLength;

				// Number of parameters
				numberOfParameters = wrapper.getInt(first);
				first += 4;
				informations.add(numberOfParameters);

				for (int j = 0; j < numberOfParameters; j++) {
					// Parameter's name
					int parameterNameLength = wrapper.getInt(first);
					first += 4;
					informations.add(wrapper.getString(first, parameterNameLength));
					first += parameterNameLength;

					// Parameter's type
					int code = wrapper.getInt(first);
					first += 4;
					ParameterType<?> type = ParameterType.fromCode(code);
					informations.add(type);

					int isRangeParameter = wrapper.getInt(first);
					first += 4;
					informations.add(isRangeParameter == 1);

					// Parameter's default value
					informations.add(type.getValue(wrapper.extract(first, type.size())));
					first += type.size();

					// Parameter's value
					informations.add(type.getValue(wrapper.extract(first, type.size())));
					first += type.size();

					if (isRangeParameter == 1) {
						// Parameter's minimum value
						informations.add(type.getValue(wrapper.extract(first, type.size())));
						first += type.size();

						// Parameter's maximum value
						informations.add(type.getValue(wrapper.extract(first, type.size())));
						first += type.size();
					}
				}
			}

			return informations.toArray();
		default:
			return new Object[0];
		}
	}
}
