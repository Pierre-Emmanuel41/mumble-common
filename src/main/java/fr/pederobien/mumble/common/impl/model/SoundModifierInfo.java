package fr.pederobien.mumble.common.impl.model;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.pederobien.mumble.common.impl.model.ParameterInfo.FullParameterInfo;
import fr.pederobien.mumble.common.impl.model.ParameterInfo.SimpleParameterInfo;

public class SoundModifierInfo<T extends ParameterInfo> {
	private String name;
	private Map<String, T> parameterInfo;

	protected SoundModifierInfo(String name) {
		this.name = name;
		parameterInfo = new LinkedHashMap<String, T>();
	}

	/**
	 * @return The sound modifier name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return A list of description for each parameter associated to the sound modifier.
	 */
	public Map<String, T> getParameterInfo() {
		return parameterInfo;
	}

	public static class SimpleSoundModifierInfo extends SoundModifierInfo<SimpleParameterInfo> {

		/**
		 * Creates a sound modifier description.
		 * 
		 * @param name The sound modifier name.
		 */
		public SimpleSoundModifierInfo(String name) {
			super(name);
		}
	}

	public static class FullSoundModifierInfo extends SoundModifierInfo<FullParameterInfo> {

		/**
		 * Creates a sound modifier description.
		 * 
		 * @param name The sound modifier name.
		 */
		public FullSoundModifierInfo(String name) {
			super(name);
		}
	}
}
