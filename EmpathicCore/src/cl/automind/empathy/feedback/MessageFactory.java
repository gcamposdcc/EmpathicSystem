package cl.automind.empathy.feedback;

import gcampos.dev.patterns.creational.FlyweightNamedFactory;

import java.util.HashMap;
import java.util.Map;


public class MessageFactory extends FlyweightNamedFactory<AbstractMessage> {

	@Override
	public Map<String, AbstractMessage> initializeMap() {
		return new HashMap<String, AbstractMessage>();
	}
}
