package fr.pederobien.mumble.common.impl;

import java.util.function.Function;

import fr.pederobien.utils.ByteWrapper;

public class ParameterType<T> {
	public static final int BOOLEAN_CODE = 0;
	public static final int CHAR_CODE = 1;
	public static final int BYTE_CODE = 2;
	public static final int SHORT_CODE = 3;
	public static final int INT_CODE = 4;
	public static final int LONG_CODE = 5;
	public static final int FLOAT_CODE = 6;
	public static final int DOUBLE_CODE = 7;

	// Parameter of type boolean
	public static final ParameterType<Boolean> BOOLEAN = new ParameterType<Boolean>(BOOLEAN_CODE, 4, Boolean.class, Parser.BOOLEAN);

	// Parameter of type character
	public static final ParameterType<Character> CHAR = new ParameterType<Character>(CHAR_CODE, 1, Character.class, Parser.CHARACTER);

	// Parameter of type byte
	public static final ParameterType<Byte> BYTE = new ParameterType<Byte>(BYTE_CODE, 1, Byte.class, Parser.BYTE);

	// Parameter of type short
	public static final ParameterType<Short> SHORT = new ParameterType<Short>(SHORT_CODE, 2, Short.class, Parser.SHORT);

	// Parameter of type integer
	public static final ParameterType<Integer> INT = new ParameterType<Integer>(INT_CODE, 4, Integer.class, Parser.INT);

	// Parameter of type long
	public static final ParameterType<Long> LONG = new ParameterType<Long>(LONG_CODE, 8, Long.class, Parser.LONG);

	// Parameter of type float
	public static final ParameterType<Float> FLOAT = new ParameterType<Float>(FLOAT_CODE, 2, Float.class, Parser.FLOAT);

	// Parameter of type double
	public static final ParameterType<Double> DOUBLE = new ParameterType<Double>(DOUBLE_CODE, 8, Double.class, Parser.DOUBLE);

	// Unknown parameter type
	public static final ParameterType<Object> UNKNOWN = new ParameterType<Object>(-1, 0, Object.class, null);

	private int code, size;
	private Class<T> clazz;
	private Parser<T> parser;

	/**
	 * 
	 * @param code
	 * @param size
	 * @param clazz
	 * @param parser
	 */
	private ParameterType(int code, int size, Class<T> clazz, Parser<T> parser) {
		this.code = code;
		this.size = size;
		this.clazz = clazz;
		this.parser = parser;
	}

	/**
	 * Get the parameter type associated to the given code.
	 * 
	 * @param code The code associated to the parameter type to get.
	 * 
	 * @return The parameter type associated to the given code or UNKNOWN if there is no parameter type associated to the given code.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ParameterType<T> fromCode(int code) {
		switch (code) {
		case BOOLEAN_CODE:
			return (ParameterType<T>) BOOLEAN;
		case CHAR_CODE:
			return (ParameterType<T>) CHAR;
		case BYTE_CODE:
			return (ParameterType<T>) BYTE;
		case SHORT_CODE:
			return (ParameterType<T>) SHORT;
		case INT_CODE:
			return (ParameterType<T>) INT;
		case LONG_CODE:
			return (ParameterType<T>) LONG;
		case FLOAT_CODE:
			return (ParameterType<T>) FLOAT;
		case DOUBLE_CODE:
			return (ParameterType<T>) DOUBLE;
		default:
			return (ParameterType<T>) UNKNOWN;
		}
	}

	/**
	 * @return The code associated to this parameter type.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return The number of bytes associated to this type.
	 */
	public int size() {
		return size;
	}

	/**
	 * Casts an object to the class or represented by this {@code ParameterType} object.
	 *
	 * @param value The value to be cast.
	 * 
	 * @return The value after casting, or null if value is null.
	 *
	 * @throws ClassCastException If the value is not null and is not assignable to the type T.
	 */
	public T cast(Object value) {
		return clazz.cast(value);
	}

	/**
	 * Generates the bytes array associated to the given value.
	 * 
	 * @param value The bytes whose the bytes representation is returned.
	 * 
	 * @return The bytes array corresponding to the specified value.
	 */
	public byte[] getBytes(Object value) {
		return parser == null ? null : parser.getBytes(cast(value));
	}

	/**
	 * Retrieve the value associated to the given bytes array.
	 * 
	 * @param bytes The bytes representation of the object to retrieve.
	 * 
	 * @return The object value associated to the given bytes representation.
	 */
	public T getValue(byte[] bytes) {
		return parser == null ? null : parser.getValue(bytes);
	}

	/**
	 * Retrieve the value associated to the given string representation.
	 * 
	 * @param value the string representation of the value to retrieve.
	 * 
	 * @return The object value associated to the given string representation.
	 */
	public T getValue(String value) {
		return parser == null ? null : parser.getValue(value);
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}

	private static class Parser<U> {
		/**
		 * Parser that transform boolean to bytes array and retrieve the boolean value from the bytes array.
		 */
		public static final Parser<Boolean> BOOLEAN = new Parser<Boolean>(ValueToBytesConverter.BOOLEAN, BytesToValueConverter.BOOLEAN, StringToValueConverter.BOOLEAN);

		/**
		 * Parser that transform character to bytes array and retrieve the character value from the bytes array.
		 */
		public static final Parser<Character> CHARACTER = new Parser<Character>(ValueToBytesConverter.CHAR, BytesToValueConverter.CHAR, StringToValueConverter.CHAR);

		/**
		 * Parser that transform byte to bytes array and retrieve the byte value from the bytes array.
		 */
		public static final Parser<Byte> BYTE = new Parser<Byte>(ValueToBytesConverter.BYTE, BytesToValueConverter.BYTE, StringToValueConverter.BYTE);

		/**
		 * Parser that transform short to bytes array and retrieve the short value from the bytes array.
		 */
		public static final Parser<Short> SHORT = new Parser<Short>(ValueToBytesConverter.SHORT, BytesToValueConverter.SHORT, StringToValueConverter.SHORT);

		/**
		 * Parser that transform integer to bytes array and retrieve the integer value from the bytes array.
		 */
		public static final Parser<Integer> INT = new Parser<Integer>(ValueToBytesConverter.INT, BytesToValueConverter.INT, StringToValueConverter.INT);

		/**
		 * Parser that transform long to bytes array and retrieve the long value from the bytes array.
		 */
		public static final Parser<Long> LONG = new Parser<Long>(ValueToBytesConverter.LONG, BytesToValueConverter.LONG, StringToValueConverter.LONG);

		/**
		 * Parser that transform float to bytes array and retrieve the float value from the bytes array.
		 */
		public static final Parser<Float> FLOAT = new Parser<Float>(ValueToBytesConverter.FLOAT, BytesToValueConverter.FLOAT, StringToValueConverter.FLOAT);

		/**
		 * Parser that transform double to bytes array and retrieve the double value from the bytes array.
		 */
		public static final Parser<Double> DOUBLE = new Parser<Double>(ValueToBytesConverter.DOUBLE, BytesToValueConverter.DOUBLE, StringToValueConverter.DOUBLE);

		private ValueToBytesConverter<U> bytesConverter;
		private BytesToValueConverter<U> valueConverter;
		private StringToValueConverter<U> stringConverter;

		public Parser(ValueToBytesConverter<U> bytesConverter, BytesToValueConverter<U> valueConverter, StringToValueConverter<U> stringConverter) {
			this.bytesConverter = bytesConverter;
			this.valueConverter = valueConverter;
			this.stringConverter = stringConverter;
		}

		/**
		 * Generates the bytes array associated to the given value.
		 * 
		 * @param value The bytes whose the bytes representation is returned.
		 * 
		 * @return The bytes array corresponding to the specified value.
		 */
		public byte[] getBytes(U value) {
			return bytesConverter.apply(value);
		}

		/**
		 * Retrieve the value associated to the given bytes array.
		 * 
		 * @param bytes The bytes representation of the object to retrieve.
		 * 
		 * @return The object value associated to the given bytes representation.
		 */
		public U getValue(byte[] bytes) {
			return valueConverter.apply(bytes);
		}

		public U getValue(String value) {
			return stringConverter.apply(value);
		}
	}

	private static class WrappedValue<U> {
		private U value;
		private ByteWrapper wrapper;

		public WrappedValue(U value) {
			this.value = value;
			wrapper = ByteWrapper.create();
		}

		public WrappedValue(byte[] bytes) {
			wrapper = ByteWrapper.wrap(bytes);
		}

		/**
		 * @return The underlying value.
		 */
		public U getValue() {
			return value;
		}

		/**
		 * @return the wrapper the generate the bytes array or retrieve the generic value.
		 */
		public ByteWrapper getWrapper() {
			return wrapper;
		}
	}

	private static class ValueToBytesConverter<U> {
		/**
		 * Converter that converts a boolean value into a bytes array.
		 */
		public static final ValueToBytesConverter<Boolean> BOOLEAN = new ValueToBytesConverter<Boolean>(
				value -> value.getWrapper().putInt(value.getValue() ? 1 : 0).get());

		/**
		 * Converter that converts a character value into a bytes array.
		 */
		public static final ValueToBytesConverter<Character> CHAR = new ValueToBytesConverter<Character>(
				value -> value.getWrapper().put((byte) (char) value.getValue()).get());

		/**
		 * Converter that converts a byte value into a bytes array.
		 */
		public static final ValueToBytesConverter<Byte> BYTE = new ValueToBytesConverter<Byte>(value -> value.getWrapper().put(value.getValue()).get());

		/**
		 * Converter that converts a short value into a bytes array.
		 */
		public static final ValueToBytesConverter<Short> SHORT = new ValueToBytesConverter<Short>(value -> value.getWrapper().putShort(value.getValue()).get());

		/**
		 * Converter that converts a integer value into a bytes array.
		 */
		public static final ValueToBytesConverter<Integer> INT = new ValueToBytesConverter<Integer>(value -> value.getWrapper().putInt(value.getValue()).get());

		/**
		 * Converter that converts a long value into a bytes array.
		 */
		public static final ValueToBytesConverter<Long> LONG = new ValueToBytesConverter<Long>(value -> value.getWrapper().putLong(value.getValue()).get());

		/**
		 * Converter that converts a float value into a bytes array.
		 */
		public static final ValueToBytesConverter<Float> FLOAT = new ValueToBytesConverter<Float>(value -> value.getWrapper().putFloat(value.getValue()).get());

		/**
		 * Converter that converts a double value into a bytes array.
		 */
		public static final ValueToBytesConverter<Double> DOUBLE = new ValueToBytesConverter<Double>(value -> value.getWrapper().putDouble(value.getValue()).get());

		private Function<WrappedValue<U>, byte[]> converter;

		public ValueToBytesConverter(Function<WrappedValue<U>, byte[]> converter) {
			this.converter = converter;
		}

		public byte[] apply(U value) {
			return converter.apply(new WrappedValue<U>(value));
		}
	}

	private static class BytesToValueConverter<U> {
		/**
		 * Converter that converts a bytes array value into an boolean value.
		 */
		public static final BytesToValueConverter<Boolean> BOOLEAN = new BytesToValueConverter<Boolean>(value -> value.getWrapper().getInt(0) == 1);

		/**
		 * Converter that converts a bytes array value into an character value.
		 */
		public static final BytesToValueConverter<Character> CHAR = new BytesToValueConverter<Character>(value -> (char) value.getWrapper().get(0));

		/**
		 * Converter that converts a bytes array value into an byte value.
		 */
		public static final BytesToValueConverter<Byte> BYTE = new BytesToValueConverter<Byte>(value -> value.getWrapper().get(0));

		/**
		 * Converter that converts a bytes array value into an short value.
		 */
		public static final BytesToValueConverter<Short> SHORT = new BytesToValueConverter<Short>(value -> value.getWrapper().getShort(0));

		/**
		 * Converter that converts a bytes array value into an integer value.
		 */
		public static final BytesToValueConverter<Integer> INT = new BytesToValueConverter<Integer>(value -> value.getWrapper().getInt(0));

		/**
		 * Converter that converts a bytes array value into an long value.
		 */
		public static final BytesToValueConverter<Long> LONG = new BytesToValueConverter<Long>(value -> value.getWrapper().getLong(0));

		/**
		 * Converter that converts a bytes array value into an float value.
		 */
		public static final BytesToValueConverter<Float> FLOAT = new BytesToValueConverter<Float>(value -> value.getWrapper().getFloat(0));

		/**
		 * Converter that converts a bytes array value into an double value.
		 */
		public static final BytesToValueConverter<Double> DOUBLE = new BytesToValueConverter<Double>(value -> value.getWrapper().getDouble(0));

		private Function<WrappedValue<U>, U> converter;

		public BytesToValueConverter(Function<WrappedValue<U>, U> converter) {
			this.converter = converter;
		}

		public U apply(byte[] value) {
			return converter.apply(new WrappedValue<U>(value));
		}
	}

	private static class StringToValueConverter<U> {
		/**
		 * Converter that converts a boolean value into a bytes array.
		 */
		public static final StringToValueConverter<Boolean> BOOLEAN = new StringToValueConverter<Boolean>(value -> Boolean.parseBoolean(value));

		/**
		 * Converter that converts a character value into a bytes array.
		 */
		public static final StringToValueConverter<Character> CHAR = new StringToValueConverter<Character>(value -> value.charAt(0));

		/**
		 * Converter that converts a byte value into a bytes array.
		 */
		public static final StringToValueConverter<Byte> BYTE = new StringToValueConverter<Byte>(value -> (byte) value.charAt(0));

		/**
		 * Converter that converts a short value into a bytes array.
		 */
		public static final StringToValueConverter<Short> SHORT = new StringToValueConverter<Short>(value -> Short.parseShort(value));

		/**
		 * Converter that converts a integer value into a bytes array.
		 */
		public static final StringToValueConverter<Integer> INT = new StringToValueConverter<Integer>(value -> Integer.parseInt(value));

		/**
		 * Converter that converts a long value into a bytes array.
		 */
		public static final StringToValueConverter<Long> LONG = new StringToValueConverter<Long>(value -> Long.parseLong(value));

		/**
		 * Converter that converts a float value into a bytes array.
		 */
		public static final StringToValueConverter<Float> FLOAT = new StringToValueConverter<Float>(value -> Float.parseFloat(value));

		/**
		 * Converter that converts a double value into a bytes array.
		 */
		public static final StringToValueConverter<Double> DOUBLE = new StringToValueConverter<Double>(value -> Double.parseDouble(value));

		private Function<String, U> converter;

		public StringToValueConverter(Function<String, U> converter) {
			this.converter = converter;
		}

		public U apply(String value) {
			return converter.apply(value);
		}
	}
}
