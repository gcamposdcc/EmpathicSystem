package cl.automind.empathy.fw.data.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebMetadata {
	public boolean useSameUrl() default true;
	public String defaultUrl();
	public String insertUrl() default "";
	public String deleteUrl() default "";
	public String updateUrl() default "";
	public String selectUrl() default "";

	public String name() default "";
}
