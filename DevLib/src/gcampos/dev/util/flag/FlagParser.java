package gcampos.dev.util.flag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FlagParser {
	private final FlagFactory nameFactory;
	private final FlagFactory shortFlagFactory;
	private final FlagFactory longFlagFactory;
	private final Set<String> flagsUsed;
	public FlagParser(FlagFactory factory){
		this.nameFactory = factory;
		this.shortFlagFactory = new FlagFactory();
		this.longFlagFactory = new FlagFactory();
		Flag flag;
		for (String name: nameFactory.getNames()){
			flag = getNameFactory().createElement(name);
			getShortFlagFactory().registerElement(flag.getShortFlag(), flag);
			getLongFlagFactory().registerElement(flag.getLongFlag(), flag);
		}
		this.flagsUsed = new HashSet<String>();
	}
	public Map<String, Collection<String>> parse(final String[] args){
		Map<String, Collection<String>> flagArgsMap = new HashMap<String, Collection<String>>();
		getFlagsUsed().clear();
		Flag flag;
		for (int i = 0; i < args.length; i++){
			boolean shortFlag = true;
			if (args[i].startsWith("-")) shortFlag = true;
			else if (args[i].startsWith("--")) shortFlag = false;
			else continue;
			Collection<String> flagArgs = new ArrayList<String>();
			flag = (shortFlag ? getShortFlagFactory() : getLongFlagFactory()).createElement(args[i]);
			int j = i + 1;
			while (j < args.length){
				if (args[i].startsWith("-") || args[i].startsWith("--")) {
					flagArgsMap.put(flag.getName(), Collections.unmodifiableCollection(flagArgs));
					break;
				} else {
					flagArgs.add(args[j]);
					j++;
				}
			}
		}
		return Collections.unmodifiableMap(flagArgsMap);
	}
	protected FlagFactory getNameFactory() {
		return nameFactory;
	}
	protected Set<String> getFlagsUsed() {
		return flagsUsed;
	}
	public FlagFactory getShortFlagFactory() {
		return shortFlagFactory;
	}
	public FlagFactory getLongFlagFactory() {
		return longFlagFactory;
	}
}