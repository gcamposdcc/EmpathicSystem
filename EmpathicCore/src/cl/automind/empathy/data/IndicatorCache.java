package cl.automind.empathy.data;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

import java.util.Hashtable;
import java.util.Map;


public class IndicatorCache extends FlyweightNamedFactory<IIndicator>{

	@Override
	public Map<String, IIndicator> initializeMap() {
		return new Hashtable<String, IIndicator>();
	}

}
