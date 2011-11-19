package cl.automind.empathy.test.t00;

import cl.automind.empathy.IArbiterCriterion;
import cl.automind.empathy.EmpathicKernel;
import cl.automind.empathy.fw.arbiter.DefaultArbiter;

public class TestArbiter extends DefaultArbiter{

	public TestArbiter(EmpathicKernel kernel) {
		super(kernel);
	}
	public TestArbiter(EmpathicKernel kernel, IArbiterCriterion criterion){
		super(kernel, criterion);
	}

}
