package cl.automind.empathy.test;

public class AppContext {
	private static AppContext instance;
	public static AppContext getInstance(){
		if(instance == null) instance = new AppContext();
		return instance;
	}
	
	AppContext(){
		setEmpathy(new EmpathyTest());
	}

	private EmpathyTest empathy;
	
	public void setEmpathy(EmpathyTest empathy) {
		this.empathy = empathy;
	}

	public EmpathyTest getEmpathy() {
		return empathy;
	}

}
