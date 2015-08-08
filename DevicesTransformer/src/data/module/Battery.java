package data.module;

/**
 * Created by Robert on 08.08.2015.
 */
public class Battery extends Module {
	public Battery(int id, String type) {
		super(id, type);
	}

	@Override
	public boolean isActuator() {
		return false;
	}
}
