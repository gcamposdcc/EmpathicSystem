package cl.automind.empathy.data.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlMetadata {
	public String name() default  "";
	public NamedQueries queries() default @NamedQueries;
}
