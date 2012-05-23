package cl.automind.empathy.fw.data.sql;

import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.SqlType;

public class ColumnDescriptor {
	private String name;
	private SqlType type;
	private int length;
	private boolean nullable;
	
	public ColumnDescriptor(){
		this("columnName", SqlType.INTEGER, 255, false);
	}
	public ColumnDescriptor(String name, SqlType type){
		this(name, type, 255, false);
	}
	public ColumnDescriptor(Column column){
		this (column.name(), column.type(), column.length(), column.nullable());
	}
	public ColumnDescriptor(Column column, boolean nullable){
		this (column.name(), column.type(), column.length(), nullable);
	}
	public ColumnDescriptor(Column column, SqlType type, boolean nullable){
		this (column.name(), type, column.length(), nullable);
	}
	public ColumnDescriptor(String name, SqlType type, boolean nullable) {
		this(name, type, 255, nullable);
	}
	public ColumnDescriptor(String name, SqlType type, int length, boolean nullable){
		setName(name);
		setType(type);
		setLength(length);
		setNullable(nullable);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SqlType getType() {
		return type;
	}
	public void setType(SqlType type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
}
