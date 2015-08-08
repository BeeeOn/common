package data.module;

/**
 * Created by Robert on 08.08.2015.
 */
public class Actuator extends Module {
	public Actuator(int id, String type) {
		super(id, type);
	}

	@Override
	public boolean isActuator() {
		return true;
	}
}
