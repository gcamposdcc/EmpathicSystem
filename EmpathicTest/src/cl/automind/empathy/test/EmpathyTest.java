package cl.automind.empathy.test;

import java.util.Collection;
import java.util.logging.Logger;

import cl.automind.empathy.AbstractEmpathicPlugin;
import cl.automind.empathy.fw.DefaultEmpathicKernel;
import cl.automind.empathy.test.t00.TestPlugin;

public class EmpathyTest extends DefaultEmpathicKernel {


	@Override
	public void startEmpathy() {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("EmpathicKernelLoaded");
	}

	@Override
	public Collection<AbstractEmpathicPlugin> loadPluginClasses() {
		Collection<AbstractEmpathicPlugin> classes = super.loadPluginClasses();
		classes.add(new TestPlugin());
		return classes;
	}

}
