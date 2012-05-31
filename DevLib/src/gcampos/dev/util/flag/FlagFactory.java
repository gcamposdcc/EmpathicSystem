package gcampos.dev.util.flag;

import java.util.HashMap;
import java.util.Map;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

public class FlagFactory extends FlyweightNamedFactory<Flag> {

	@Override
	public Map<String, Flag> initializeMap() {
		return new HashMap<String, Flag>();
	}

}
