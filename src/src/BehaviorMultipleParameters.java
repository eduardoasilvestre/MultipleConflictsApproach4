package src;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BehaviorMultipleParameters implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String object;
	private String objectType;
	private Map<String, Set<String>> p = new HashMap<>();

	public BehaviorMultipleParameters(String name) {
		this.name = name;
	}

	public BehaviorMultipleParameters(String name, String object) {
		this.name = name;
		this.object = object;
	}

	public BehaviorMultipleParameters(String name, String object, String objectType) {
		this.name = name;
		this.object = object;
		this.objectType = objectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void addElement(String chave, String elemento) {
		Set<String> temp = (Set<String>) p.get(chave);
		if (temp == null) {
			Set<String> s = new HashSet<String>();
			s.add(elemento);
			p.put(chave, s);
		} else {
			temp.add(elemento);
			p.put(chave, temp);
		}
	}

	public Set<String> getElements(String chave) {
		return p.get(chave);
	}

	public Map<String, Set<String>> getMap() {
		return p;
	}

	public void addSetOfElements(String key, Set<String> set) {
		for (String value : set) {
			this.addElement(key, value);
		}
	}

	public void removeElement(String key) {
		p.remove(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BehaviorMultipleParameters other = (BehaviorMultipleParameters) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
}
