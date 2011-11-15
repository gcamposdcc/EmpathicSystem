package cl.automind.empathy.data;

public interface IStringMappeable<T>{
	public String map();
	public T unmap(String map);
}
