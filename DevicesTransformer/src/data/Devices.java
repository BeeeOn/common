package data;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

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
