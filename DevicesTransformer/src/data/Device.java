package data;

import data.module.*;

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

	// Feaures modules
	private Led mFeatureLed;
	private Battery mFeatureBattery;
	private Refresh mFeatureRefresh;
	private Rssi mFeatureRssi;

	public Device(int typeId, String typeName) {
		mTypeId = typeId;
		mTypeName = typeName;
	}

	public List<Module> getModules() {
		return mModules;
	}

	public List<Module> getModulesWithoutFeatures() {
		List<Module> filteredModules = new ArrayList<>();
		for (Module module : mModules) {
			// Ignore special modules (battery, rssi, refresh, led) to generate same output as before

			filteredModules.add(module);
		}
		return filteredModules;
	}

	private void clearFeatures() {
		mFeatureRefresh = null;
		mFeatureBattery = null;
		mFeatureLed = null;
		mFeatureRssi = null;
	}

	private boolean isFeatureModule(Module module) {
		return (module instanceof Battery
				|| module instanceof Refresh
				|| module instanceof Rssi
				|| module instanceof Led);
	}

	private void setFeatureModule(Module module) {
		if (module instanceof Battery) {
			mFeatureBattery = (Battery) module;
		} else if (module instanceof Refresh) {
			mFeatureRefresh = (Refresh) module;
		} else if (module instanceof Rssi) {
			mFeatureRssi = (Rssi) module;
		} else if (module instanceof Led) {
			mFeatureLed = (Led) module;
		}
	}

	public void setModules(List<Module> modules) {
		mModules.clear();
		clearFeatures();

		for (Module module : modules) {
			mModules.add(module);
			if (isFeatureModule(module)) {
				setFeatureModule(module);
			}
		}
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

	public Led getFeatureLed() {
		return mFeatureLed;
	}

	public Battery getFeatureBattery() {
		return mFeatureBattery;
	}

	public Refresh getFeatureRefresh() {
		return mFeatureRefresh;
	}

	public Rssi getFeatureRssi() {
		return mFeatureRssi;
	}
}
