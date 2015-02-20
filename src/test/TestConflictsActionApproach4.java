package test;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import src.BehaviorMultipleParameters;
import src.ConflictCheckerActionApproach4;
import src.Constraint;
import src.Context;
import src.ContextType;
import src.DeonticConcept;
import src.Entity;
import src.EntityType;
import src.Norm;

public class TestConflictsActionApproach4 {
	public static void main(String[] args) {
		List<Norm> normSet = buildSomeNorms();
		ConflictCheckerActionApproach4 apv = new ConflictCheckerActionApproach4();
		apv.verifyConflicts(normSet);
	}
	
	/**
	 * This method builds a set of norms por the analysis of conflicts
	 * @author eduardo.silvestre
	 */
	private static List<Norm> buildSomeNorms() {
		List<Norm> normSet = new ArrayList<>();
		
		//praticamnete igual aos prox
		/*Context context1 = new Context("home", ContextType.ORGANIZATION);
		Entity entity1 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action1 = new BehaviorMultipleParameters("dress","garment");
		action1.addElement("garment","shirt");
		action1.addElement("color","white");
		Constraint aConstraint1 = null; 
		Constraint dConstraint1 = null; 
		Norm norm1 = new Norm(1, DeonticConcept.OBLIGATION, context1, entity1, action1, aConstraint1, dConstraint1);
		normSet.add(norm1);
		
		Context context2 = new Context("home", ContextType.ORGANIZATION);
		Entity entity2 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action2 = new BehaviorMultipleParameters("dress","garment");
		action2.addElement("garment","shirt");
		action2.addElement("color","blue");
		Constraint aConstraint2 = null; 
		Constraint dConstraint2 = null; 
		Norm norm2 = new Norm(2, DeonticConcept.OBLIGATION, context2, entity2, action2, aConstraint2, dConstraint2);
		normSet.add(norm2);

		Context context3 = new Context("home", ContextType.ORGANIZATION);
		Entity entity3 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action3 = new BehaviorMultipleParameters("dress","garment");
		action3.addElement("garment","pant");
		action3.addElement("color","black");
		Constraint aConstraint3 = null; 
		Constraint dConstraint3 = null; 
		Norm norm3 = new Norm(3, DeonticConcept.PERMISSION, context3, entity3, action3, aConstraint3, dConstraint3);
		normSet.add(norm3);
				
		Context context6 = new Context("home", ContextType.ORGANIZATION);
		Entity entity6 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action6 = new BehaviorMultipleParameters("dress","garment");
		action6.addElement("garment","shirt");
		action6.addElement("color", ConflictChecker_APV.EQUAL);
		Constraint aConstraint6 = null; 
		Constraint dConstraint6 = null; 
		Norm norm6 = new Norm(6, DeonticConcept.OBLIGATION, context6, entity6, action6, aConstraint6, dConstraint6);
		normSet.add(norm6);
		
		Context context7 = new Context("home", ContextType.ORGANIZATION);
		Entity entity7 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action7 = new BehaviorMultipleParameters("dress","garment");
		action7.addElement("garment","pant");
		action7.addElement("color", ConflictChecker_APV.EQUAL);
		Constraint aConstraint7 = null; 
		Constraint dConstraint7 = null; 
		Norm norm7 = new Norm(7, DeonticConcept.OBLIGATION, context7, entity7, action7, aConstraint7, dConstraint7);
		normSet.add(norm7);*/
		
		
		//TODO EXEMPLO DESCOMENTADO COM 6 NORMAS
		/*Context context1 = new Context("home", ContextType.ORGANIZATION);
		Entity entity1 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action1 = new BehaviorMultipleParameters("dress","shirt", "garment");
		action1.addElement("color","white");
		action1.addElement("picture", "smooth");
		Constraint aConstraint1 = null; 
		Constraint dConstraint1 = null; 
		Norm norm1 = new Norm(1, DeonticConcept.OBLIGATION, context1, entity1, action1, aConstraint1, dConstraint1);
		normSet.add(norm1);
		
		Context context2 = new Context("home", ContextType.ORGANIZATION);
		Entity entity2 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action2 = new BehaviorMultipleParameters("dress","shirt", "garment");
		action2.addElement("color","blue");
		action2.addElement("picture", "vertical");
		Constraint aConstraint2 = null; 
		Constraint dConstraint2 = null; 
		Norm norm2 = new Norm(2, DeonticConcept.PROHIBITION, context2, entity2, action2, aConstraint2, dConstraint2);
		normSet.add(norm2);

		Context context3 = new Context("home", ContextType.ORGANIZATION);
		Entity entity3 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action3 = new BehaviorMultipleParameters("dress","pant", "garment");
		action3.addElement("color","black");
		action3.addElement("picture", "horizontal");
		Constraint aConstraint3 = null; 
		Constraint dConstraint3 = null; 
		Norm norm3 = new Norm(3, DeonticConcept.PROHIBITION, context3, entity3, action3, aConstraint3, dConstraint3);
		normSet.add(norm3);
				
		Context context6 = new Context("home", ContextType.ORGANIZATION);
		Entity entity6 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action6 = new BehaviorMultipleParameters("dress","shirt", "garment");
		action6.addElement("color", ConflictCheckerActionApproach4.EQUAL);
		action6.addElement("picture", "smooth");
		Constraint aConstraint6 = null; 
		Constraint dConstraint6 = null; 
		Norm norm6 = new Norm(6, DeonticConcept.OBLIGATION, context6, entity6, action6, aConstraint6, dConstraint6);
		normSet.add(norm6);
		
		Context context7 = new Context("home", ContextType.ORGANIZATION);
		Entity entity7 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action7 = new BehaviorMultipleParameters("dress","pant", "garment");
		action7.addElement("color", ConflictCheckerActionApproach4.EQUAL);
		action7.addElement("picture", "smooth");
		Constraint aConstraint7 = null; 
		Constraint dConstraint7 = null; 
		Norm norm7 = new Norm(7, DeonticConcept.PERMISSION, context7, entity7, action7, aConstraint7, dConstraint7);
		normSet.add(norm7);*/
		
		
		Context context201 = new Context("home", ContextType.ORGANIZATION);
		Entity entity201 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action201 = new BehaviorMultipleParameters("dress","shirt");
		action201.addElement("color","white");
		action201.addElement("picture", "smooth");
		action201.addElement("ironingtype", "ironing");
		Constraint aConstraint201 = null; 
		Constraint dConstraint201 = null; 
		Norm norm201 = new Norm(201, DeonticConcept.OBLIGATION, context201, entity201, action201, aConstraint201, dConstraint201);
		normSet.add(norm201);
		
		Context context202 = new Context("home", ContextType.ORGANIZATION);
		Entity entity202 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action202 = new BehaviorMultipleParameters("dress","shirt");
		action202.addElement("color","blue");
		action202.addElement("picture", "vertical");
		action202.addElement("ironingtype", "ironing");
		Constraint aConstraint202 = null; 
		Constraint dConstraint202 = null; 
		Norm norm202 = new Norm(202, DeonticConcept.PROHIBITION, context202, entity202, action202, aConstraint202, dConstraint202);
		normSet.add(norm202);

		Context context203 = new Context("home", ContextType.ORGANIZATION);
		Entity entity203 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action203 = new BehaviorMultipleParameters("dress","pant");
		action203.addElement("color","black");
		action203.addElement("picture", "horizontal");
		action203.addElement("ironingtype", "ironing");
		Constraint aConstraint203 = null; 
		Constraint dConstraint203 = null; 
		Norm norm203 = new Norm(203, DeonticConcept.PROHIBITION, context203, entity203, action203, aConstraint203, dConstraint203);
		normSet.add(norm203);
				
		Context context204 = new Context("home", ContextType.ORGANIZATION);
		Entity entity204 = new Entity ("person", EntityType.ROLE);
		BehaviorMultipleParameters action204 = new BehaviorMultipleParameters("dress","shirt");
		action204.addElement("color", ConflictCheckerActionApproach4.EQUAL);
		action204.addElement("picture", "smooth");
		action204.addElement("ironingtype", "ironing");
		Constraint aConstraint204 = null; 
		Constraint dConstraint204 = null; 
		Norm norm204 = new Norm(204, DeonticConcept.OBLIGATION, context204, entity204, action204, aConstraint204, dConstraint204);
		normSet.add(norm204);
		
		
		
		return normSet;
	}
	
	/**
	 * This method receives a date in String format and returns a DateTime (JodaTime library)
	 * @author eduardo.silvestre
	 */
	private static DateTime buildDateTime(String data) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTime d = dtf.parseDateTime(data);
		return d;
	}

}
