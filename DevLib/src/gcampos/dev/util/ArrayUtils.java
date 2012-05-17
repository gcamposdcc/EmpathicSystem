package gcampos.dev.util;

public class ArrayUtils {
	public static <T> boolean contains(T[] array, T value){
		for (T t: array) if (t.equals(value)) return true;
		return false;
	}
	public static <T> boolean containsAny(T[] a0, T[] a1){
		for(T t: a1) if (contains(a0, t)) return true;
		return false;
	}
	public static <T> boolean containsAll(T[] a0, T[] a1){
		for(T t: a1) if (!contains(a0, t)) return false;
		return true;
	}
}
