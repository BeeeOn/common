package data;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Device {

	private final List<Module> mModules = new ArrayList<>();

	private final int mTypeId;

	private final String mTypeName;

	private Translation mName;

	private Translation mManufacturer;

	private Features mFeatures;

	public Device(int typeId, String typeName) {
		mTypeId = typeId;
		mTypeName = typeName;
	}

	public List<Module> getModules() {
		return mModules;
	}

	public void setModules(List<Module> modules) {
		mModules.clear();
		mModules.addAll(modules);
	}

	public int getTypeId() {
		return mTypeId;
	}

	public String getTypeName() {
		return mTypeName;
	}

	public Translation getName() {
		return mName;
	}

	public void setName(Translation name) {
		mName = name;
	}

	public Translation getManufacturer() {
		return mManufacturer;
	}

	public void setManufacturer(Translation manufacturer) {
		mManufacturer = manufacturer;
	}

	@Nullable
	public Features getFeatures() {
		return mFeatures;
	}

	public void setFeatures(Features features) {
		mFeatures = features;
	}

	public static class Features {
		private Integer mRefresh;
		private Integer mRefreshId;
		private Integer mBatteryId;
		private Integer mRssiId;
		private Boolean mLed;

		public boolean hasRefresh() {
			return mRefreshId != null && mRefresh != null;
		}

		@Nullable
		public Integer getDefaultRefresh() {
			return mRefresh;
		}

		@Nullable
		public Integer getRefreshId() {
			return mRefreshId;
		}

		public void setRefresh(Integer moduleId, Integer refresh) {
			mRefreshId = moduleId;
			mRefresh = refresh;
		}

		public boolean hasBattery() {
			return mBatteryId != null;
		}

		@Nullable
		public Integer getBatteryId() {
			return mBatteryId;
		}

		public void setBattery(Integer moduleId) {
			mBatteryId = moduleId;
		}

		public boolean hasRssi() {
			return mRssiId != null;
		}

		@Nullable
		public Integer getRssiId() {
			return mRssiId;
		}

		public void setRssi(Integer moduleId) {
			mRssiId = moduleId;
		}

		public boolean hasLed() {
			return mLed != null && mLed;
		}

		public void setLed(Boolean led) {
			mLed = led;
		}
	}
}
