package src;

import java.io.Serializable;

public abstract class Constraint implements Serializable {

	private static final long serialVersionUID = 1L;
	protected ConstraintType constraintType;

	public ConstraintType getConstraintType() {
		return constraintType;
	}

	public void setConstraintType(ConstraintType constraintType) {
		this.constraintType = constraintType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constraintType == null) ? 0 : constraintType.hashCode());
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
		Constraint other = (Constraint) obj;
		if (constraintType != other.constraintType)
			return false;
		return true;
	}
}
