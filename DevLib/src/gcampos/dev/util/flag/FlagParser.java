package gcampos.dev.util.flag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class FlagParser {
	private final static Logger LOGGER = Logger.getLogger(FlagParser.class.getCanonicalName());
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
			LOGGER.info("ProcessingFlag::" + name);
			flag = getNameFactory().createElement(name);
			LOGGER.info("Flag::" + flag.getName() + "::" + flag.getShortFlag() + "::" + flag.getLongFlag());
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
			if (args[i].startsWith("-")) {
				LOGGER.info("ShortFlagDetected::" + args[i]);
				shortFlag = true;
			}
			else if (args[i].startsWith("--")) {
				LOGGER.info("LongFlagDetected::" + args[i]);
				shortFlag = false;
			}
			else {
				LOGGER.info("FlagArgDetected::" + args[i]);
				continue;
			}
			Collection<String> flagArgs = new ArrayList<String>();
			if (shortFlag){
				flag = getShortFlagFactory().createElement(args[i].substring(1));
			} else {
				flag = getLongFlagFactory().createElement(args[i].substring(2));
			}
			LOGGER.info("Flag::" + flag.getName() + "::" + flag.getShortFlag() + "::" + flag.getLongFlag());
			int j = i + 1;
			while (j < args.length){
				if (args[j].startsWith("-") || args[j].startsWith("--")) {
					LOGGER.info("SettingFlagArgs::" + flag.getName() + "::" + flagArgs);
					flagArgsMap.put(flag.getName(), Collections.unmodifiableCollection(flagArgs));
					break;
				} else {
					if(args[j].startsWith("\"") || args[j].startsWith("'")){
						flagArgs.add(args[j].substring(1, args[j].length() - 1));
					} else {
						flagArgs.add(args[j]);
					}
					j++;
				}
			}
			if (j == args.length){
				flagArgsMap.put(flag.getName(), Collections.unmodifiableCollection(flagArgs));
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
	protected FlagFactory getShortFlagFactory() {
		return shortFlagFactory;
	}
	protected FlagFactory getLongFlagFactory() {
		return longFlagFactory;
	}
	public void interpreteFlag(String flag, Collection<String> flagArgs){
		if (flagArgs != null){
			if (flagArgs.size() != 0){
				interpreteFlag(flag, flagArgs.toArray(new String[flagArgs.size()]));
				return;
			} 
		}
		interpreteFlag(flag, new String[]{});
	}
	public void interpreteFlag(String flag, String[] flagArgs){
		Flag f = getNameFactory().createElement(flag);
		if (f != null){
			LOGGER.warning("FlagRegistered::" + flag);
			if (flagArgs != null){
				f.getTask().execute(flagArgs);
			} else {
				f.getTask().execute(new String[]{});
			}
			LOGGER.warning("FlagTaskExecuted::" + flag);
		} else {
			LOGGER.warning("FlagNotRegistered::" + flag);
		}
	}
}