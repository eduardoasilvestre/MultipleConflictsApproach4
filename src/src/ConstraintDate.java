package src;

import org.joda.time.DateTime;

public class ConstraintDate extends Constraint {

	private DateTime date;

	public ConstraintDate(ConstraintType constraintType, DateTime date) {
		this.constraintType = constraintType;
		this.date = date;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}
}
