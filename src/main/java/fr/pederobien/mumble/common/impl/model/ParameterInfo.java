package fr.pederobien.mumble.common.impl.model;

public class ParameterInfo {

	public static class LazyParameterInfo {
		private String name;
		private ParameterType<?> type;
		private Object value;

		/**
		 * Creates a parameter description.
		 * 
		 * @param name  The parameter name.
		 * @param type  The parameter type.
		 * @param value The parameter value.
		 */
		public LazyParameterInfo(String name, ParameterType<?> type, Object value) {
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
		 * @return The parameter value.
		 */
		public Object getValue() {
			return value;
		}
	}

	public static class FullParameterInfo extends LazyParameterInfo {
		private Object defaultValue, minValue, maxValue;
		private boolean isRange;

		/**
		 * Creates a description of a parameter.
		 * 
		 * @param name         The parameter name.
		 * @param type         The parameter type.
		 * @param value        The parameter value.
		 * @param defaultValue The default parameter value.
		 * @param isRange      true if a range is associated to the parameter.
		 * @param min          The minimum parameter value.
		 * @param max          The maximum parameter value.
		 */
		public FullParameterInfo(String name, ParameterType<?> type, Object value, Object defaultValue, boolean isRange, Object min, Object max) {
			super(name, type, defaultValue);
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
