package fr.pederobien.mumble.common.impl.messages.v10.model;

import fr.pederobien.mumble.common.impl.ParameterType;

public class ParameterInfo {
	private String name;
	private ParameterType<?> type;
	private Object value;

	protected ParameterInfo(String name, ParameterType<?> type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	/**
	 * @return The parameter name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The parameter type.
	 */
	public ParameterType<?> getType() {
		return type;
	}

	/**
	 * @return The parameter current value.
	 */
	public Object getValue() {
		return value;
	}

	public static class SimpleParameterInfo extends ParameterInfo {

		/**
		 * Creates a parameter description.
		 * 
		 * @param name  The parameter name.
		 * @param type  The parameter type.
		 * @param value The parameter current value.
		 */
		public SimpleParameterInfo(String name, ParameterType<?> type, Object value) {
			super(name, type, value);
		}
	}

	public static class FullParameterInfo extends ParameterInfo {
		private Object defaultValue, minValue, maxValue;
		private boolean isRange;

		/**
		 * Creates a parameter description.
		 * 
		 * @param name         The parameter name.
		 * @param type         The parameter type.
		 * @param value        The parameter current value.
		 * @param defaultValue The parameter default value.
		 * @param isRange      True if a range is associated to the parameter.
		 * @param min          The parameter minimum value.
		 * @param max          The parameter maximum value.
		 */
		public FullParameterInfo(String name, ParameterType<?> type, Object value, Object defaultValue, boolean isRange, Object min, Object max) {
			super(name, type, value);
			this.defaultValue = defaultValue;
			this.isRange = isRange;
			this.minValue = min;
			this.maxValue = max;
		}

		/**
		 * @return The default parameter value.
		 */
		public Object getDefaultValue() {
			return defaultValue;
		}

		/**
		 * @return True is a range is attached to the parameter.
		 */
		public boolean isRange() {
			return isRange;
		}

		/**
		 * @return The minimum parameter value.
		 */
		public Object getMinValue() {
			return minValue;
		}

		/**
		 * @return The maximum parameter value.
		 */
		public Object getMaxValue() {
			return maxValue;
		}
	}
}
