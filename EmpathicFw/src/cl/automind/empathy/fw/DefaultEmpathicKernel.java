package cl.automind.empathy.fw;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.EmpathicKernel;
import cl.automind.empathy.EmpathicPlugin;

public class DefaultEmpathicKernel extends EmpathicKernel{
	protected static EmpathicPlugin[] defaultPlugins = 
	{
		new DefaultEmpathicPlugin()
	};
	@Override
	public Collection<EmpathicPlugin> loadPluginClasses() {
		Collection<EmpathicPlugin> plugins = new ArrayList<EmpathicPlugin>();
		System.out.println(plugins);
		System.out.println(defaultPlugins);
		for(EmpathicPlugin plugin : defaultPlugins){
			System.out.println(plugin);
			plugins.add(plugin);
		}
		return plugins;
	}

	@Override
	public void startEmpathy() {
		
	}

}