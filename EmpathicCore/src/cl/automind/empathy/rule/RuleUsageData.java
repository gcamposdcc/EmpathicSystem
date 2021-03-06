package cl.automind.empathy.rule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cl.automind.empathy.rule.RuleUsage.Source;

public class RuleUsageData{
	private List<RuleUsage> dates = new ArrayList<RuleUsage>();
	public RuleUsageData(boolean newUse){
		if(newUse) {
			newUse();
		}
	}
	public int getTimesUsed(){
		return dates.size();
	}
	public final void newUse(){
		dates.add(new RuleUsage(Calendar.getInstance().getTime()));
	}
	public void newUse(Date date){
		dates.add(new RuleUsage(date));
	}
	public void newUse(Date date, Source source){
		dates.add(new RuleUsage(date, source));
	}
}
