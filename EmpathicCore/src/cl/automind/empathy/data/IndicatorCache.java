package cl.automind.empathy.data;

import java.util.Hashtable;
import java.util.Map;

import patterns.creational.FlyweightNamedFactory;

public class IndicatorCache extends FlyweightNamedFactory<IIndicator>{

	@Override
	public Map<String, IIndicator> initializeMap() {
		return new Hashtable<String, IIndicator>();
	}

}
