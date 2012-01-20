package cl.automind.empathy.fw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import cl.automind.empathy.AbstractEmpathicKernel;
import cl.automind.empathy.AbstractEmpathicPlugin;

public class DefaultEmpathicKernel extends AbstractEmpathicKernel{
	protected static AbstractEmpathicPlugin[] defaultPlugins =
	{
		new DefaultEmpathicPlugin()
	};
	@Override
	public Collection<AbstractEmpathicPlugin> loadPluginClasses() {
		Collection<AbstractEmpathicPlugin> plugins = new ArrayList<AbstractEmpathicPlugin>();
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+plugins);
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+defaultPlugins);
		for(AbstractEmpathicPlugin plugin : defaultPlugins){
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+plugin);
			plugins.add(plugin);
		}
		return plugins;
	}

	@Override
	public void startEmpathy() {

	}

}
