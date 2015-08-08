package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Translation {
	private static final String RESOURCE_STRING = "R.string." + Language.TRANSLATION_PREFIX + "%s";

	private final String mTranslationId;

	public static final List<Translation> translations = new ArrayList<>();

	public Translation(String translation, boolean remember) {
		String[] parts = translation.split(":");

		if (parts.length != 2) {
			throw new IllegalArgumentException(String.format("data.Translation string must have 2 parts (separated by ':'). Given '%s'.", translation));
		}

		if (!parts[0].equals("T")) {
			throw new IllegalArgumentException(String.format("data.Translation string must start with 'T:'. Given '%s'.", translation));
		}

		mTranslationId = parts[1];

		// Remember all translations in global object
		if (remember) {
			this.translations.add(this);
		}
	}

	public Translation(String translation) {
		this(translation, true);
	}

	public String getTranslationId() {
		return mTranslationId;
	}

	public String getResourceId() {
		return String.format(RESOURCE_STRING, mTranslationId.toLowerCase());
	}

}
