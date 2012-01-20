package cl.automind.empathy.test.t00;

import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.AbstractEmpathicKernel;
import cl.automind.empathy.fw.arbiter.DefaultArbiter;

public class TestArbiter extends DefaultArbiter{

	public TestArbiter(AbstractEmpathicKernel kernel) {
		super(kernel);
	}
	public TestArbiter(AbstractEmpathicKernel kernel, IArbiterCriterion criterion){
		super(kernel, criterion);
	}

}
