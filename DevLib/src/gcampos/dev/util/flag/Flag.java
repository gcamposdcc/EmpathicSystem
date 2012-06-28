package gcampos.dev.util.flag;

import gcampos.dev.interfaces.structural.INamed;

public class Flag implements INamed {
	private String name;
	private final String shortFlag;
	private final String longFlag;
	private final FlagTask task;
	public Flag(FlagTask task){
		FlagMetadata meta = getClass().getAnnotation(FlagMetadata.class);
		this.shortFlag = fix(meta.shortName());
		this.longFlag = fix(meta.longName());
		this.task = task;
	}
	public Flag(String shortName, String longName, FlagTask task){
		this.shortFlag = fix(shortName);
		this.longFlag = fix(longName);
		this.task = task;
	}
	public Flag(String name, String shortName, String longName, FlagTask task){
		this.name = name;
		this.shortFlag = fix(shortName);
		this.longFlag = fix(longName);
		this.task = task;
	}
	private static String fix(String s){
		String fixed = s;
		while(fixed.indexOf("-") == 0){
			fixed = fixed.substring(fixed.indexOf(1));
		}
		return fixed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortFlag() {
		return shortFlag;
	}
	public String getLongFlag() {
		return longFlag;
	}
	public FlagTask getTask() {
		return task;
	}
}
