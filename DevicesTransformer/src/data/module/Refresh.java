package data.module;

/**
 * Created by Robert on 08.08.2015.
 */
public class Refresh extends Module {
	public Refresh(int id, String type) {
		super(id, type);
	}

	@Override
	public boolean isActuator() {
		return true;
	}
}
