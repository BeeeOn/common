package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Devices {

	private final String mVersion;

	private final List<Device> mDevices = new ArrayList<>();

	public Devices(String version) {
		mVersion = version;
	}

	public void addDevice(Device device) {
		mDevices.add(device);
	}

	public String getVersion() {
		return mVersion;
	}

	public List<Device> getDevices() {
		return mDevices;
	}
}
