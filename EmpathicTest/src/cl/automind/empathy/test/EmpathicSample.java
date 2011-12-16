package cl.automind.empathy.test;

import java.util.ArrayList;
import java.util.Collection;

import cl.automind.empathy.EmpathicKernel;
import cl.automind.empathy.EmpathicPlugin;
import cl.automind.empathy.fw.DefaultEmpathicKernel;

public class EmpathicSample {

	public static void main(String[] args) {
		// se crea el objeto y se carga todo al inicializar
		EmpathicKernel kernel = new DefaultEmpathicKernel(){
			@Override
			public Collection<EmpathicPlugin> loadPluginClasses() {
				Collection<EmpathicPlugin> plugins = new ArrayList<EmpathicPlugin>();
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
