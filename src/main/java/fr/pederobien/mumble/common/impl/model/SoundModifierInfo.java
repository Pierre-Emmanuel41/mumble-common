package fr.pederobien.mumble.common.impl.model;

import java.util.ArrayList;
import java.util.List;

import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.LazyParameterInfo;

public class SoundModifierInfo {

	public static class LazySoundModifierInfo {
		private String name;
		private List<LazyParameterInfo> parameterInfo;

		/**
		 * Creates a sound modifier description.
		 * 
		 * @param name The sound modifier name.
		 */
		public LazySoundModifierInfo(String name) {
			this.name = name;
			parameterInfo = new ArrayList<LazyParameterInfo>();
		}

		/**
		 * @return The sound modifier name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return The list that contains a description of each parameter.
		 */
		public List<LazyParameterInfo> getParameterInfo() {
			return parameterInfo;
		}
	}

	public static class FullSoundModifierInfo {
		private String name;
		private List<FullParameterInfo> parameterInfo;

		/**
		 * Creates a sound modifier description.
		 * 
		 * @param name The sound modifier name.
		 */
		public FullSoundModifierInfo(String name) {
			this.name = name;
			parameterInfo = new ArrayList<FullParameterInfo>();
		}

		/**
		 * @return The sound modifier name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return The list that contains a description of each parameter.
		 */
		public List<FullParameterInfo> getParameterInfo() {
			return parameterInfo;
		}
	}
}
