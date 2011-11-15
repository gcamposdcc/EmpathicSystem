package cl.automind.empathy.data.sql;

public @interface Model {
	public Id id() default @Id;
	public Column[] columns() default {};
}
