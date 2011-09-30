package cl.automind.empathy.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RuleMetadata {
	public String name() default "";
	public String[] strategies() default { StrategyMetadata.DEFAULT };
	public double minVal() default MIN_VALUE;
	public double maxVal() default MAX_VALUE;
	public double threshold() default THRESHOLD;
	public static double MIN_VALUE = 0.0;
	public static double MAX_VALUE = 100.0;
	public static double THRESHOLD = 50.0;
	public static String[] STRATEGIES = new String[]{ StrategyMetadata.DEFAULT };
}
