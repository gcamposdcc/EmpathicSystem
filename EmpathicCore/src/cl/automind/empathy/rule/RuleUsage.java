package cl.automind.empathy.rule;

import java.util.Date;

public class RuleUsage{
	public enum Source {
		NonSpecified,
		Manual,
		Periodic
	}
	private final Date date;
	private final Source source;
	public RuleUsage(Date date){
		this.date = date;
		this.source = Source.NonSpecified;
	}
	public RuleUsage(Date date, Source source){
		this.date = date;
		this.source = source;
	}
	public Date getDate(){
		return date;
	}
	public Source getSource(){
		return source;
	}
}
