package src;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Norm implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id; /* identificator */
	
	private DeonticConcept deonticConcept; /* deontic concept (obligation, prohibition or permission)*/

	private Context context; /* context where the norm is defined*/
	
	private Entity entity; /* entity whose behaviour is being regulated*/
	
	private BehaviorMultipleParameters behavior; /* behavior (action or state) being regulated*/
	
	private Constraint activationConstraint; /* condition that activates the norm*/
	
	private Constraint deactivationConstraint; /* condition that deactivates the norm */
	
	public Norm(int id, DeonticConcept deonticConcept, Context context, Entity entity,
			BehaviorMultipleParameters behavior, Constraint activationConstraint,
			Constraint deactivationConstraint) {
		this.id = id;
		this.deonticConcept = deonticConcept;
		this.context = context;
		this.entity = entity;
		this.behavior = behavior;
		this.activationConstraint = activationConstraint;
		this.deactivationConstraint = deactivationConstraint;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DeonticConcept getDeonticConcept() {
		return deonticConcept;
	}

	public void setDeonticConcept(DeonticConcept deonticConcept) {
		this.deonticConcept = deonticConcept;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public BehaviorMultipleParameters getBehavior() {
		return behavior;
	}

	public void setBehavior(BehaviorMultipleParameters behavior) {
		this.behavior = behavior;
	}

	public Constraint getActivationConstraint() {
		return activationConstraint;
	}

	public void setActivationConstraint(Constraint activationConstraint) {
		this.activationConstraint = activationConstraint;
	}

	public Constraint getDeactivationConstraint() {
		return deactivationConstraint;
	}

	public void setDeactivationConstraint(Constraint deactivationConstraint) {
		this.deactivationConstraint = deactivationConstraint;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String identificator =  "Id: " + id + " | ";
		builder.append(identificator);
		
		String dc =  deonticConcept == null ? "DeonticConcept: NULL | " : "DeonticConcept: " + deonticConcept + " | ";
		builder.append(dc);
		/*
	 	this code is commented for test effects (easier visualization)
	 	ESTE CÓDIGO ESTÁ COMENTADO PARA EFEITO DE TESTES E FACILITAR A VISUALIZAÇÃO
	 	
		String c = context == null ? "Context: NULL | " : "Context: " + context.getName() + " | ";
		builder.append(c);
		
		String e = entity == null ? "Entity: NULL | " : "Entity: " + entity.getName() + " | ";
		builder.append(e);
		*/
		
		String a = behavior == null ? "Behavior: NULL | " : "Behavior: name = " + behavior.getName() + " - object = " + behavior.getObject() + " | ";
		/* the code below is used to print the parameters of the behavior
		 * */
		builder.append(a);
		
		Map<String, Set<String>> temp = behavior.getMap();
		for (Map.Entry<String,Set<String>>   entry : temp.entrySet()) {
	  		String key = entry.getKey();
	  		Set<String> values = entry.getValue();
	  		Iterator<String> it = values.iterator();
	  		
	  		builder.append(key);
	  		builder.append("( ");
	  		
	  		while(it.hasNext()) {
	  			builder.append(it.next());
	  			builder.append(" ");
	  		}
	  		builder.append(") ");
	  	}

		/*
		 this code is commented for test effects (easier visualization)
		
		String ac = null;
		if (activationConstraint == null) {
			ac = "ActivationConstraint: NULL" + " | ";
		} else if (activationConstraint instanceof ConstraintDate) {
			ac = "ActivationConstraint Date: " + ((ConstraintDate) activationConstraint).getDate().toString("dd/MM/yyyy") + " | ";
		}
		builder.append(ac);
		
		String de = null;
		if (deactivationConstraint == null) {
			de = "DeactivationConstraint: NULL" + " | ";
		} else if (deactivationConstraint instanceof ConstraintDate) {
			de = "DeactivationConstraint Date: " + ((ConstraintDate) deactivationConstraint).getDate().toString("dd/MM/yyyy") + " | ";
		}
		builder.append(de);
		
		String s = state == null ? "State: NULL | " : "State: " + state;
		builder.append(s);
				*/
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activationConstraint == null) ? 0 : activationConstraint
						.hashCode());
		result = prime * result
				+ ((behavior == null) ? 0 : behavior.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime
				* result
				+ ((deactivationConstraint == null) ? 0
						: deactivationConstraint.hashCode());
		result = prime * result
				+ ((deonticConcept == null) ? 0 : deonticConcept.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + id;
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
		Norm other = (Norm) obj;
		if (activationConstraint == null) {
			if (other.activationConstraint != null)
				return false;
		} else if (!activationConstraint.equals(other.activationConstraint))
			return false;
		if (behavior == null) {
			if (other.behavior != null)
				return false;
		} else if (!behavior.equals(other.behavior))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (deactivationConstraint == null) {
			if (other.deactivationConstraint != null)
				return false;
		} else if (!deactivationConstraint.equals(other.deactivationConstraint))
			return false;
		if (deonticConcept != other.deonticConcept)
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (id != other.id)
			return false;

		return true;
	}
}
