package cl.automind.empathy.test;

import java.util.Collection;

import cl.automind.empathy.EmpathicPlugin;
import cl.automind.empathy.fw.DefaultEmpathicKernel;
import cl.automind.empathy.test.t00.TestPlugin;

public class EmpathyTest extends DefaultEmpathicKernel {


	@Override
	public void startEmpathy() {
		System.out.println("EmpathicKernelLoaded");
	}
	
	@Override
	public Collection<EmpathicPlugin> loadPluginClasses() {
		Collection<EmpathicPlugin> classes = super.loadPluginClasses();
		classes.add(new TestPlugin());
		return classes;
	}

}
