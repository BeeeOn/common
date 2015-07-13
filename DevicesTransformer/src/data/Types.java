package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 23. 5. 2015.
 */
public class Types {

	private final String mVersion;

	private final List<Type> mTypes = new ArrayList<>();

	public Types(String version) {
		mVersion = version;
	}

	public void addType(Type type) {
		mTypes.add(type);
	}

	public String getVersion() {
		return mVersion;
	}

	public List<Type> getTypes() {
		return mTypes;
	}
}
