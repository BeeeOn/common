package data;

import java.util.*;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Language {
	public static final String TRANSLATION_PREFIX = "devices__";
	public static final String VALUES_SEPARATOR = "_";

	private final String mCode;

	private final Map<String, Item> mItems = new HashMap<>();

	public Language(String code) {
		mCode = code;
	}

	public void addItem(String key, String value) {
		mItems.put(key, new Item(key, value));
	}

	public void addItem(Item item) {
		mItems.put(item.key, item);
	}

	public void addItems(List<Item> items) {
		for (Item item : items) {
			mItems.put(item.key, item);
		}
	}

	public Item getItem(String key) {
		return mItems.get(key);
	}

	public String getCode() {
		return mCode;
	}

	public List<Item> getItems() {
		List<Item> items = new ArrayList<>();
		for (Item item : mItems.values()) {
			items.add(item);
		}
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o1.key.compareTo(o2.key);
			}
		});
		return items;
	}

	public static class Item {
		public final String key;
		public final String value;

		public Item(String key, String value) {
			this.key = TRANSLATION_PREFIX + key;
			this.value = value;
		}

		public Item(String prefix, String key, String value) {
			this.key = TRANSLATION_PREFIX + prefix + VALUES_SEPARATOR + key;
			this.value = value;
		}
	}

}
