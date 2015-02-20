package util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SetUtil {
	// http://sysgears.com/articles/immutable-collections-java-0/
	
	//EXAMPLE 1
	public final static Set<String> GARMENT_SET_ELEMENTS;
	static {
		Set<String> gt = new TreeSet<String>();
		gt.add("pant");
		gt.add("shirt");
		GARMENT_SET_ELEMENTS = Collections.unmodifiableSet(gt);

	}
	public final static Set<String> COLOR_ELEMENTS;
	static {
		Set<String> ct = new TreeSet<String>();
		ct.add("white");
		ct.add("black");
		ct.add("blue");
		ct.add("red");
		ct.add("lilac");
		COLOR_ELEMENTS = Collections.unmodifiableSet(ct);
	}
	public final static Set<String> IRONING_ELEMENTS;
	static {
		Set<String> it = new TreeSet<String>();
		it.add("ironing");
		it.add("crumpled");
		IRONING_ELEMENTS = Collections.unmodifiableSet(it);
	}
	public final static Set<String> PICTURE_ELEMENTS;
	static {
		Set<String> pt = new TreeSet<String>();
		pt.add("horizontal");
		pt.add("vertical");
		pt.add("smooth");
		pt.add("gust");
		PICTURE_ELEMENTS = Collections.unmodifiableSet(pt);
	}
	
	//EXAMPLE 2
	public final static Set<String> G_SET_ELEMENTS;
	static {
		Set<String> g = new TreeSet<String>();
		g.add("whistle");
		g.add("scolding");
		g.add("shoes");
		g.add("stocking");
		G_SET_ELEMENTS = Collections.unmodifiableSet(g);

	}
	public final static Set<String> C_ELEMENTS;
	static {
		Set<String> c = new TreeSet<String>();
		c.add("white");
		c.add("black");
		c.add("blue");
		c.add("red");
		c.add("lilac");
		C_ELEMENTS = Collections.unmodifiableSet(c);
	}
	public final static Set<String> BODY_ELEMENTS;
	static {
		Set<String> i = new TreeSet<String>();
		i.add("mouth");
		i.add("pocket");
		i.add("foot");
		BODY_ELEMENTS = Collections.unmodifiableSet(i);
	}


	public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.addAll(setB);
		return tmp;
	}

	public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>();
		for (T x : setA)
			if (setB.contains(x))
				tmp.add(x);
		return tmp;
	}
	//new
	public static <T> Set<T> difference(Set<T> setA, T setB) {
		Set<T> tmpNew = new TreeSet<T>();
		tmpNew.add(setB);
		
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.removeAll(tmpNew);
		return tmp;
	}
	
	public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
		Set<T> tmp = new TreeSet<T>(setA);
		tmp.removeAll(setB);
		return tmp;
	}

	public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
		Set<T> tmpA;
		Set<T> tmpB;

		tmpA = union(setA, setB);
		tmpB = intersection(setA, setB);
		return difference(tmpA, tmpB);
	}

	public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
		return setB.containsAll(setA);
	}

	public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
		return setA.containsAll(setB);
	}

	//new
	public static <T> boolean hasOneElement(Set<T> setA) {
		return setA.size() == 1;
	}
	//new
	public static <T> boolean isEmpty(Set<T> setA) {
		return setA.isEmpty();
	}
	//new
	public static <T> Set<T> cleanSet(Set<T> setA) {
		setA.clear();
		return setA;
	}
	//new
	public static boolean isFirstInter(String parameter, Map<String, Integer> map) {
		Integer value = map.get(parameter);
		if (value.intValue() == 0) {
			return true;
		}
		return false;
	}
	//new
	public static  Map<String, Integer> addValue(String parameter, Map<String, Integer> map) {
		Integer value = map.get(parameter);
		map.put(parameter, ++value);
		return map;
	}
	
	public static boolean containsEqualDiff(Set<String> p) {
		return (p.contains("A") || p.contains("!A"));
	} 
}
