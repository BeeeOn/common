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
}
