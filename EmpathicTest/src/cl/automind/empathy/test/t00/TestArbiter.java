package cl.automind.empathy.test.t00;

import cl.automind.empathy.ArbiterCriterion;
import cl.automind.empathy.EmpathicKernel;
import cl.automind.empathy.fw.arbiter.DefaultArbiter;

public class TestArbiter extends DefaultArbiter{

	public TestArbiter(EmpathicKernel kernel) {
		super(kernel);
		// TODO Auto-generated constructor stub
	}
	public TestArbiter(EmpathicKernel kernel, ArbiterCriterion criterion){
		super(kernel, criterion);
	}

}
