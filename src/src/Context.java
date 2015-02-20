package src;

import java.io.Serializable;


public class Context implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private ContextType contextType;

	public Context(String name, ContextType contextType) {
		this.name = name;
		this.contextType = contextType;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ContextType getContextType() {
		return contextType;
	}
	
	public void setContextType(ContextType contextType) {
		this.contextType = contextType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contextType == null) ? 0 : contextType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Context other = (Context) obj;
		if (contextType != other.contextType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
