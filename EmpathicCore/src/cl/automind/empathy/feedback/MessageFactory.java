package cl.automind.empathy.feedback;

import java.util.HashMap;
import java.util.Map;

import patterns.creational.FlyweightNamedFactory;

public class MessageFactory extends FlyweightNamedFactory<AbstractMessage> {

	@Override
	public Map<String, AbstractMessage> initializeMap() {
		return new HashMap<String, AbstractMessage>();
	}
}
