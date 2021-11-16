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
	public static final ParameterType<Boolean> BOOLEAN = new ParameterType<Boolean>(BOOLEAN_CODE, Boolean.class, Parser.BOOLEAN);

	// Parameter of type character
	public static final ParameterType<Character> CHAR = new ParameterType<Character>(CHAR_CODE, Character.class, Parser.CHARACTER);

	// Parameter of type byte
	public static final ParameterType<Byte> BYTE = new ParameterType<Byte>(BYTE_CODE, Byte.class, Parser.BYTE);

	// Parameter of type short
	public static final ParameterType<Short> SHORT = new ParameterType<Short>(SHORT_CODE, Short.class, Parser.SHORT);

	// Parameter of type integer
	public static final ParameterType<Integer> INT = new ParameterType<Integer>(INT_CODE, Integer.class, Parser.INT);

	// Parameter of type long
	public static final ParameterType<Long> LONG = new ParameterType<Long>(LONG_CODE, Long.class, Parser.LONG);

	// Parameter of type float
	public static final ParameterType<Float> FLOAT = new ParameterType<Float>(FLOAT_CODE, Float.class, Parser.FLOAT);

	// Parameter of type double
	public static final ParameterType<Double> DOUBLE = new ParameterType<Double>(DOUBLE_CODE, Double.class, Parser.DOUBLE);

	// Unknown parameter type
	public static final ParameterType<Object> UNKNOWN = new ParameterType<Object>(-1, Object.class, null);

	private int code;
	private Class<T> clazz;
	private Parser<T> parser;

	private ParameterType(int code, Class<T> clazz, Parser<T> parser) {
		this.code = code;
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
	public byte[] getBytes(T value) {
		return parser == null ? null : parser.getBytes(value);
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

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}

	private static class Parser<U> {
		/**
		 * Parser that transform boolean to bytes array and retrieve the boolean value from the bytes array.
		 */
		public static final Parser<Boolean> BOOLEAN = new Parser<Boolean>(value -> value.getWrapper().putInt(value.getValue() ? 1 : 0).get(),
				value -> value.getWrapper().getInt(0) == 1);

		/**
		 * Parser that transform character to bytes array and retrieve the character value from the bytes array.
		 */
		public static final Parser<Character> CHARACTER = new Parser<Character>(value -> value.getWrapper().put((byte) (char) value.getValue()).get(),
				value -> (char) value.getWrapper().get(0));

		/**
		 * Parser that transform byte to bytes array and retrieve the byte value from the bytes array.
		 */
		public static final Parser<Byte> BYTE = new Parser<Byte>(value -> value.getWrapper().put(value.getValue()).get(), value -> value.getWrapper().get(0));

		/**
		 * Parser that transform short to bytes array and retrieve the short value from the bytes array.
		 */
		public static final Parser<Short> SHORT = new Parser<Short>(value -> value.getWrapper().putShort(value.getValue()).get(),
				value -> value.getWrapper().getShort(0));

		/**
		 * Parser that transform integer to bytes array and retrieve the integer value from the bytes array.
		 */
		public static final Parser<Integer> INT = new Parser<Integer>(value -> value.getWrapper().putInt(value.getValue()).get(), value -> value.getWrapper().getInt(0));

		/**
		 * Parser that transform long to bytes array and retrieve the long value from the bytes array.
		 */
		public static final Parser<Long> LONG = new Parser<Long>(value -> value.getWrapper().putLong(value.getValue()).get(), value -> value.getWrapper().getLong(0));

		/**
		 * Parser that transform float to bytes array and retrieve the float value from the bytes array.
		 */
		public static final Parser<Float> FLOAT = new Parser<Float>(value -> value.getWrapper().putFloat(value.getValue()).get(),
				value -> value.getWrapper().getFloat(0));

		/**
		 * Parser that transform double to bytes array and retrieve the double value from the bytes array.
		 */
		public static final Parser<Double> DOUBLE = new Parser<Double>(value -> value.getWrapper().putDouble(value.getValue()).get(),
				value -> value.getWrapper().getDouble(0));

		private Function<WrappedValue<U>, byte[]> bytesGenerator;
		private Function<WrappedValue<U>, U> valueGenerator;

		public Parser(Function<WrappedValue<U>, byte[]> bytesGenerator, Function<WrappedValue<U>, U> valueGenerator) {
			this.bytesGenerator = bytesGenerator;
			this.valueGenerator = valueGenerator;
		}

		/**
		 * Generates the bytes array associated to the given value.
		 * 
		 * @param value The bytes whose the bytes representation is returned.
		 * 
		 * @return The bytes array corresponding to the specified value.
		 */
		public byte[] getBytes(U value) {
			return bytesGenerator.apply(new WrappedValue<U>(value));
		}

		/**
		 * Retrieve the value associated to the given bytes array.
		 * 
		 * @param bytes The bytes representation of the object to retrieve.
		 * 
		 * @return The object value associated to the given bytes representation.
		 */
		public U getValue(byte[] bytes) {
			return valueGenerator.apply(new WrappedValue<U>(bytes));
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
}
