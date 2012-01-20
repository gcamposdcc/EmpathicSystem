package cl.automind.empathy.test;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.AbstractEmpathicKernel;
import cl.automind.empathy.AbstractEmpathicPlugin;
import cl.automind.empathy.fw.DefaultEmpathicKernel;

public class EmpathicSample {

	public static void main(String[] args) {
		// se crea el objeto y se carga todo al inicializar
		AbstractEmpathicKernel kernel = new DefaultEmpathicKernel(){
			@Override
			public Collection<AbstractEmpathicPlugin> loadPluginClasses() {
				Collection<AbstractEmpathicPlugin> plugins = new ArrayList<AbstractEmpathicPlugin>();
				plugins.add(new SamplePlugin());
				return plugins;
			}
		};
		// método opcional
		kernel.startEmpathy();

		kernel.triggerEmpathy();

		kernel.registerRule(new SampleRule());
	}

}
