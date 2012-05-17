package cl.automind.empathy.feedback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EmotionMetadata {
	public static final String DEFAULT = "default";
	public static final String[] FAMILIES = new String[]{ DEFAULT };
	
	public String name();
	public String[] families() default { DEFAULT };
}
