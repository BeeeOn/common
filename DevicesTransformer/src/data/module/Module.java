package data.module;

import com.sun.istack.internal.Nullable;
import data.Translation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public abstract class Module {
	public static final String PREFIX_VALUE_SEPARATOR = "_";

	private final int mId;
	private final String mType;
	private final List<Value> mValues = new ArrayList<>();
	private final List<Rule> mRules = new ArrayList<>();
	private Integer mOrder;
	private Translation mGroup;
	private Translation mName;
	private Constraints mConstraints;
	private String mDefaultValue;

	public Module(int id, String type) {
		mId = id;
		mType = type;
	}

	public int getId() {
		return mId;
	}

	public String getType() {
		return mType;
	}

	@Nullable
	public Integer getOrder() {
		return mOrder;
	}

	public void setOrder(Integer order) {
		mOrder = order;
	}

	public Translation getGroup() {
		return mGroup;
	}

	public void setGroup(Translation group) {
		mGroup = group;
	}

	public Translation getName() {
		return mName;
	}

	public void setName(Translation name) {
		mName = name;
	}

	public List<Value> getValues() {
		return mValues;
	}

	public void setValues(Translation name, List<Value> values) {
		mValues.clear();
		for (Value value : values) {
			Translation translation = new Translation(String.format("T:%s%s%s",
					name.getTranslationId(),
					PREFIX_VALUE_SEPARATOR,
					value.translation.getTranslationId()));

			mValues.add(new Value(value.id, translation));
		}
	}

	public List<Rule> getRules() {
		return mRules;
	}

	public void setRules(List<Rule> rules) {
		mRules.clear();
		mRules.addAll(rules);
	}

	public abstract boolean isActuator();

	@Nullable
	public Constraints getConstraints() {
		return mConstraints;
	}

	public void setConstraints(@Nullable Constraints constraints) {
		mConstraints = constraints;
	}

	public String getDefaultValue() {
		return mDefaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		mDefaultValue = defaultValue;
	}

	public static class Constraints {
		private double mMin;
		private double mMax;
		private double mGranularity;

		public double getMin() {
			return mMin;
		}

		public void setMin(double min) {
			mMin = min;
		}

		public double getMax() {
			return mMax;
		}

		public void setMax(double max) {
			mMax = max;
		}

		public double getGranularity() {
			return mGranularity;
		}

		public void setGranularity(double granularity) {
			mGranularity = granularity;
		}
	}

	public static class Value {
		public final int id;
		public final Translation translation;

		public Value(int id, Translation translation) {
			this.id = id;
			this.translation = translation;
		}
	}

	public static class Rule {
		public final int value;
		public final Integer[] hideModulesIds;

		public Rule(int value, Integer[] hideModulesIds) {
			this.value = value;
			this.hideModulesIds = hideModulesIds;
		}
	}
}
