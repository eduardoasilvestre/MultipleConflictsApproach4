package src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import util.SetUtil;

public class ConflictCheckerActionApproach4 {
	
	private List<List<Norm>> groupNorms  = null;
	private Map<String,Set<String>> mapOfParameters = null;
	
	private Map<Integer,Norm> normsInPermitted = null;
	
	public final static String EQUAL = "A";
	public final static String DIFF = "!A";
	
	public static int counter = 0;	//total of combinations

	public ConflictCheckerActionApproach4() {
		groupNorms = new ArrayList<List<Norm>>();
		normsInPermitted = new HashMap<Integer, Norm>();
		
		mapOfParameters = new HashMap<String, Set<String>>();
		mapOfParameters.put("garment", SetUtil.GARMENT_SET_ELEMENTS);
		mapOfParameters.put("color", SetUtil.COLOR_ELEMENTS);
		mapOfParameters.put("ironingtype", SetUtil.IRONING_ELEMENTS);
		mapOfParameters.put("picture", SetUtil.PICTURE_ELEMENTS);
		
		mapOfParameters.put("g", SetUtil.G_SET_ELEMENTS);
		mapOfParameters.put("c", SetUtil.C_ELEMENTS);
		mapOfParameters.put("bodypart", SetUtil.BODY_ELEMENTS);
	}
	
	public void verifyConflicts(List<Norm> norms) {
		this.insertAndClassifyAllNorms(norms);
		
		List<List<Norm>> conflicts  = new ArrayList<List<Norm>>();
		
		for(int i = 0; i < groupNorms.size(); i++) {
			List<Norm> normsTemp = groupNorms.get(i);	
			if (normsTemp.size() < 2) {
				continue;
			}
			//convert all norms to permitted. this conversion is done once
			List<Norm> ret = this.convertNormsToPermitted(normsTemp);
			for (Norm n : ret) {
				normsInPermitted.put(n.getId(), n);
			}			
			
			//change the parameter j to change the length of analysis
			for (int j = 2; j <= normsTemp.size();j++) {
				List<List<Norm>> normsNtoN = this.generateAllCombinations(normsTemp, j);
				List<List<Norm>> normsRet = this.verifyConflictsCase4(normsNtoN);
				conflicts.addAll(normsRet);
				normsNtoN.clear();
			}
		}
		//System.out.println("THE FOLLOWING NORMS ARE IN CONFLICT:");
		this.printNorms(conflicts);
	}
	

	private List<List<Norm>> verifyConflictsCase4(List<List<Norm>> norms) {
		List<List<Norm>> normsRet  = new ArrayList<List<Norm>>();
		for (int i = 0; i < norms.size(); i++) {
			counter++; 
			List<Norm> normsTemp = norms.get(i);
			
			/* ESTE CODIGO IMPRIME TODAS AS COMBINACOES POSSIVEIS DE NORMAS (BASTA COMENTAR ABAIXO)
			for (Norm no : normsTemp) {
				System.out.println(no);
			}
			System.out.println();*/
			
			String basic = this.isThereBasicConflict(normsTemp);
			if ("CONFLICT".equals(basic)) {
				normsRet.add(normsTemp);
				continue;
			} else if ("WITHOUT".equals(basic)) {
				continue;
			}
			
			//get the norms in permitted form			
			List<Norm> normsPerm = new ArrayList<Norm>();
			for (Norm no : normsTemp) {
				normsPerm.add(normsInPermitted.get(no.getId()));
				
			}
			//here is UNIDENTIFIED

			//apagar este for e o sysout
			/*for (Norm no : normsPerm) {
				System.out.println(no);
			}
			System.out.println();*/
			
			if (this.isThereConflict(normsPerm)) {
				normsRet.add(normsTemp);
			}
		}
		return normsRet;
	}
	
	private boolean isThereVariableInANorm(Norm norm) {
		Map<String,Set<String>> mapParameters = norm.getBehavior().getMap();
		for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) {
			String key = entry.getKey();
	  		Set<String> x = norm.getBehavior().getElements(key);
	  		if (SetUtil.hasOneElement(x) && SetUtil.containsEqualDiff(x) ){
	  			return true;
	  		}
	  	}
		return false;
	}
	
	private String getTheParameterWithVariableInANorm(Norm norm) {
		Map<String,Set<String>> mapParameters = norm.getBehavior().getMap();
		for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) {
			String key = entry.getKey();
			Set<String> x = norm.getBehavior().getElements(key);
			if (SetUtil.hasOneElement(x) && SetUtil.containsEqualDiff(x) ){
				return key;
			}
		}
		return null;
	}
	
	private boolean isThereVariableInASet(List<Norm> norms) {
  		for (Norm norm : norms) {
  			if (this.isThereVariableInANorm(norm)) {
  				return true;
  			}
  		}
		return false;
	}
	
	private String isThereBasicConflict(List<Norm> norms) {
		/**		CASE 1 
		 verify the norm set in the case that all norms are permissions. Conflicts with permission can happens if:
		 1 - has variable; 2 - has not. Another cases don't have conflicts
		 In this version is not considered the expression of NOT.*/
		int normsCounter = 0;
		for (int i = 0; i < norms.size(); i++) {
			if (norms.get(i).getDeonticConcept().equals(DeonticConcept.PERMISSION)) {
				normsCounter++;
			}
		}
		if (normsCounter == norms.size()) {
			if (!this.isThereVariableInASet(norms)) {
				return "WITHOUT";
			}
		}
		
		/**CASE 2 and CASE 3 (preparation)
		after the the maps will contain the distribution of equal-diff*/
		Map <String, Integer> mapCountEqualDiff = new HashMap<String, Integer>();
		Map <String, Integer> mapCountEqualDiff2 = new HashMap<String, Integer>();
		for (Norm norm : norms) {
			String root = norm.getBehavior().getObject();
  			if (!this.isThereVariableInANorm(norm)) {
  				if(mapCountEqualDiff.get(root) == null) {
  					mapCountEqualDiff.put(root, 1);
  				} else {
  					Integer currentValue = mapCountEqualDiff.get(root);
  					mapCountEqualDiff.put(root, ++currentValue);
  				}
  			} else {
  				if(mapCountEqualDiff2.get(root) == null) {
  					mapCountEqualDiff2.put(root, 1);
  				} else {
  					Integer currentValue = mapCountEqualDiff2.get(root);
  					mapCountEqualDiff2.put(root, ++currentValue);
  				}
  			}
  		}
		
		/**	CASE 2 
		 Imagine the case...
		 my set of norms don't have variable and all roots are different
		 in this case the algorithm must return that there isn't conflict*/
		if (norms.size() == mapCountEqualDiff.size()) { //it means the set of norms doesn't have variable
			boolean varGreaterThenOne = false;
			for (Map.Entry<String,Integer> entry : mapCountEqualDiff.entrySet()) {
				Integer value = entry.getValue();
				if (value != 1) {
					varGreaterThenOne = true;
					break; //there isn't a variable. it is necessary one root greater than 1
				}
			}
			if (!varGreaterThenOne) { //it doesn't have conflict
				return "WITHOUT";
			}
		}

		/**	CASE 3
		 There is a variable for a specified root. But only one norm has the root.
		 It is impossible the unifcation. Automatically the set of norms has a conflict.*/
		for (Map.Entry<String,Integer> entry : mapCountEqualDiff2.entrySet()) {
			//at this moment the key represents a root with variable (the quantity doesn't matter) 
			String key = entry.getKey();

			//it means that there is at least a variable, but there isn't a norm without variable
			if (mapCountEqualDiff.get(key) == null) { 
				return "CONFLICT"; //its is impossible the unification. there is a conflict
			}
		}
		
		/**	CASE 4
		 PART 1
		 There is variable for 2 different parameters. It is not permitted
		 Imagine color = A, ironing = A and picture = !A.
		 PART 2
		 It is necessary that the parameter value be the same(A or A)*/
		String parameterName = null;
		Set<String> parameterValueHistory = null;
		for (Norm norm : norms) {
			//PART 1
			String parameter = this.getTheParameterWithVariableInANorm(norm);
			if (parameter == null) {
				continue;
			}
			//at this moment the parameter is != null
			if (parameterName != null && !parameter.equals(parameterName)) {
				return "CONFLICT";
			}
			parameterName = (String) this.deepClone(parameter);
			//PART 2
			Set<String> parameterValue = norm.getBehavior().getElements(parameter);
			if (parameterValueHistory != null) {
				if (!parameterValueHistory.containsAll(parameterValue)) { //verify the equality
					return "CONFLICT";
				}
			}
			parameterValueHistory = parameterValue;
		}
		return "UNIDENTIFIED";
	}
	
	private List<Norm> convertNormsToPermitted(List<Norm> norms) {
		List<Norm> normsCopy  = new ArrayList<Norm>(); //contains a clone of list norms
		
		List<Norm> normsPermTemp  = new ArrayList<Norm>();
		
		for (Norm norm : norms) {
			normsCopy.add((Norm)this.deepClone(norm));
		}
		
		for (Norm norm : normsCopy) {
			if (norm.getDeonticConcept().equals(DeonticConcept.PERMISSION)) {
				normsPermTemp.add(norm);
			} else if (norm.getDeonticConcept().equals(DeonticConcept.OBLIGATION)) {
				norm.setDeonticConcept(DeonticConcept.PERMISSION);
				normsPermTemp.add(norm);
			} else if (norm.getDeonticConcept().equals(DeonticConcept.PROHIBITION)) {
				Map<String,Set<String>> mapParameters = norm.getBehavior().getMap();
				
				BehaviorMultipleParameters bTemp = new BehaviorMultipleParameters(norm.getBehavior().getName(),norm.getBehavior().getObject(), norm.getBehavior().getObjectType());

				for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) {
			  		String key = entry.getKey();
			  		Set<String> x = norm.getBehavior().getElements(key);
			  		if (SetUtil.hasOneElement(x) && SetUtil.containsEqualDiff(x) ){
			  			Set<String> vars = new HashSet<String>();
			  			if (x.contains(EQUAL)) {
			  				vars.add(DIFF);
			  			} else {
			  				vars.add(EQUAL);
			  			}
			  			bTemp.addSetOfElements(key, vars);
			  			continue;
			  		}
			  		
			  		Set<String> notElement = SetUtil.difference(mapOfParameters.get(key), entry.getValue());
			  		bTemp.addSetOfElements(key, notElement);				
				}
				norm.setDeonticConcept(DeonticConcept.PERMISSION);
				norm.setBehavior(bTemp);
				normsPermTemp.add(norm);
			}
		}
		return normsPermTemp;
	}

	private boolean isThereConflict(List<Norm> norms) {
		norms = this.removeDuplicateNorms(norms);	//remove the norms with the same action
		
		Map<String,Set<String>> mapParameters = norms.get(0).getBehavior().getMap(); //all the norms have the same parameters
		//TODO comentado para testes
		//String root = norms.get(0).getBehavior().getObject(); //all norms here have the same root (but the value of the root can be different)
		
		//in this version in only permitted one parameter with variable in a set of norms (its is permitted more than one norm with variable)
		String parameterWithVariableName = null;	//stores the parameter with variable (if exists)
		String parameterWithVariableValue = null;	//stores the value of the parameter
		
		Set<String> normRootValueList = new HashSet<>();	//stores the values contained in root
		
		String parameterNormsEqualDiff = null;	//stores the parameter with equal diff
		
		List<Norm> normsWithVariables = new ArrayList<Norm>();
		
		Set<String> rootValues = new HashSet<String>(); //stores the root values of the norms with variables
		
		//stores in a list the norms the have parameter
		//PS.: To safely remove from a collection while iterating over it you should use an Iterator.
		Iterator<Norm> iterator = norms.iterator();
		while(iterator.hasNext()) {
			Norm ned = iterator.next();
			
			parameterNormsEqualDiff = this.getTheParameterWithVariableInANorm(ned);
			if (parameterNormsEqualDiff == null) { //it means that the norm doesn't have parameter
				normRootValueList.add(ned.getBehavior().getObject());
				continue;
			}
			//here means the the norm has variable
	    	Norm normEqualDiff = (Norm)deepClone(ned);
	    	normsWithVariables.add(normEqualDiff);
	    	
	    	rootValues.add(normEqualDiff.getBehavior().getObject());
	    	
	    	iterator.remove();
	    	parameterNormsEqualDiff = null;
		}

		//this code iterates over the norms and makes the intersection between the parameters values.
		//all the norms here have parameter. The parameter is the same and the value of the parameter too.
		Map<String,Set<String>> intersectionsEqualDiff = new HashMap<>();
		Map<String,Map<String,Set<String>>> intersectionsVariables = new HashMap<String, Map<String,Set<String>>>();
		
		if (!normsWithVariables.isEmpty()) {
			Set<String> intersectionParameter = new HashSet<String>();
			String parameterEqualDiff = this.getTheParameterWithVariableInANorm(normsWithVariables.get(0));//all norms have parameter
			
			parameterWithVariableName = parameterEqualDiff;
			parameterWithVariableValue = normsWithVariables.get(0).getBehavior().getElements(parameterWithVariableName).toString();
			
			Iterator<String> itera = rootValues.iterator();
		    while (itera.hasNext()) {
		    	String rootValue = itera.next();
		    	
		    	for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) {
			  		String key = entry.getKey();
			  		if (parameterEqualDiff.equals(key)) {
			  			continue;
			  		}
			  		for (Norm norm : normsWithVariables) {
						boolean firstTime = true;
						Set<String> values = norm.getBehavior().getElements(key);
						if (firstTime) {
				    		intersectionParameter = SetUtil.union(intersectionParameter, values);
				    		firstTime = false;
				    	} else {
				    		intersectionParameter = SetUtil.intersection(intersectionParameter, values);
				    	}
					}
				    Set<String> copy = new HashSet<String>();
				    Iterator<String> it = intersectionParameter.iterator();
				    while (it.hasNext()) {
				    	copy.add((String)this.deepClone(it.next()));
				    }
				    intersectionsEqualDiff.put(key, copy);
			  		intersectionParameter = SetUtil.cleanSet(intersectionParameter);
				}
		    	intersectionsVariables.put(rootValue, intersectionsEqualDiff);
		    }
		}
		
  		//Discover the quantity of different. This list contains norms with variables.
  		//The variables are all A or !A. It is imporant identify how many values of root there are
  		Norm normT = null;
  		if (!normsWithVariables.isEmpty()) {
  			normT = normsWithVariables.get(0);
  		}
  		boolean varEqualDiff = false;
  		for(Norm norm : normsWithVariables) {
  			int counter = 0;
  			String root = norm.getBehavior().getObject();
  			String normTRoot = normT.getBehavior().getObject();
  			
  			if (!root.equals(normTRoot)) {
  				counter++;
  			}
  			if (counter > 0) {
  				varEqualDiff = true;
  				break;
  			}
  		}
		
		Map<String,Integer> mapCounter = new HashMap<String,Integer>();
		Map<String,Map<String,Set<String>>> intersections = new HashMap<String, Map<String,Set<String>>>();

		//iterates over each parameter. The parameter is the root of the norm
		Iterator<String> it = normRootValueList.iterator();
	    while (it.hasNext()) {
	    	String normRootElement = it.next();
	    	
	    	//inserts in normTemp each norm that has 'parameter'
	    	List<Norm> normsTemp  = new ArrayList<>();
	    	for (Norm normTemp: norms)  {
	    		String normRootCurrent = normTemp.getBehavior().getObject();
	    		if (normRootElement.equals(normRootCurrent)) {
	    			normsTemp.add(normTemp);
	    		}
	    	}
	    	//if the normsTemp for a specified parameter has at least one element
	    	if (normsTemp.size() > 0) {
	    		Map<String,Set<String>> mapTemp = this.createIntersections(normsTemp); 
	    		intersections.put(normRootElement, mapTemp);
	    		
	    		//stores the quantity of norms that interaction was done
	    		int normsTempSize = normsTemp.size();
	    		mapCounter.put(normRootElement, normsTempSize);
	    	}
	    }
	    /**
	     * 3 cases of conflicts in parameters
	     * 1 - conflict with variable and one root value. Can be only A
	     * 2 - conflict with variable and several root values.
	     * 3 - conflict with no variable 
	     * */
	    
	    Set<String> intersectionParameter = new HashSet<String>();
	    
	  	boolean varDiffIntersection = true; //this variable marks if some partial set is different of 1. This case is necessary for the analysis of DIFF
	  	
	  	for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) {	  	//iteraction over all parameters
	  		
	  		String key = entry.getKey();

	  		boolean parameterWithVariableIsValid = parameterWithVariableName != null && parameterWithVariableName.equals(key);
	  		
	  		if (!varEqualDiff && parameterWithVariableIsValid) {
		  		/*****************************************************************
		  		 * 							CASE 1 OF CONFLICT
		  		*****************************************************************/	
	  			
	  			String valueTemp = normT.getBehavior().getObject(); //all roots with variable have the same value
	  			Map<String, Set<String>> mapCurrent = intersections.get(valueTemp);
	  			intersectionParameter = mapCurrent.get(parameterWithVariableName);
	  			
	  			if(intersectionParameter.isEmpty()) {
	  				return true;
	  			}
	  		} else if (!normsWithVariables.isEmpty() && parameterWithVariableIsValid) {
		  		/*****************************************************************
		  		 * 							CASE 2 OF CONFLICT
		  		*****************************************************************/	
	  			
	  			Map<String,String> mapParamEqualDiff = new HashMap<String, String>();
	  			for (Norm norm : normsWithVariables) {
	  				String rootValue = norm.getBehavior().getObject();
	  				mapParamEqualDiff.put(rootValue, rootValue);
	  			}

	  			int count = 0;
				for (Map.Entry<String,String> entryParameterEqualDiff : mapParamEqualDiff.entrySet()) {
	  				Map<String, Set<String>> mapCurrent = intersections.get(entryParameterEqualDiff.getValue());  //map of shirt, pant, ....
		  			Set<String> values = mapCurrent.get(parameterWithVariableName);
		  			if (count++ == 0) {
		  				intersectionParameter = SetUtil.union(intersectionParameter, values);
		  			} else {
		  				intersectionParameter = SetUtil.intersection(intersectionParameter, values);
		  			}
	  			}
				if (parameterWithVariableValue.contains(EQUAL)) {
					if (SetUtil.isEmpty(intersectionParameter)) {
						return true;
					}
				} else if (SetUtil.hasOneElement(intersectionParameter) && varDiffIntersection) {
					//TODO must be implemented
					return true; //DIFF 
				}
			} else {
				/*****************************************************************
				 * 							CASE 3 OF CONFLICT
				*****************************************************************/
				
				for (Map.Entry<String,Map<String,Set<String>>> entryParameter : intersections.entrySet()) {	//iteraction over the values of the parameter
		  			String keyParameter = entryParameter.getKey();
			  		Map<String,Set<String>> interTemp = entryParameter.getValue();
			  		
					if (mapCounter.get(keyParameter) == 1 && intersectionsVariables.get(keyParameter) == null) {
						continue;
					}
					intersectionParameter = interTemp.get(key);
					
					if (!normsWithVariables.isEmpty() && intersectionsVariables.get(keyParameter) != null) {
						Map<String, Set<String>> mapVariables = intersectionsVariables.get(keyParameter);
						if (mapVariables != null) {
							Set<String> valuesEqualDiff = intersectionsVariables.get(keyParameter).get(key);
							intersectionParameter = SetUtil.intersection(intersectionParameter, valuesEqualDiff);
						}
					}
		  			
					if (SetUtil.isEmpty(intersectionParameter)) {
						return true;
					}
		  		}
			}
			varDiffIntersection = true;
			intersectionParameter = SetUtil.cleanSet(intersectionParameter);
	  	}
		return false;
	}
	
	private List<Norm> removeDuplicateNorms(List<Norm> norms) {
		List<Norm> normsCopy  = new ArrayList<Norm>(); //contains a clone of list norms
		
		for (Norm norm : norms) {
			normsCopy.add((Norm)this.deepClone(norm));
		}
		
		List<Norm> normsR  = new ArrayList<Norm>();
		
		Set<Norm> set = new HashSet<>();
		for (Norm norm : normsCopy) {
			norm.setId(1);
			set.add(norm);
		}
		normsR.addAll(set);
		return (normsR.size()  != norms.size()) ? normsR : norms; 
	}
	
	//Makes all intersections
	private Map<String,Set<String>> createIntersections(List<Norm> norms) {
		Map<String,Set<String>> intersections = new HashMap<String, Set<String>>();
		
		Map<String,Set<String>> mapParameters = norms.get(0).getBehavior().getMap(); //all the norms have the same parameters
		//TODO
		String root = norms.get(0).getBehavior().getObject(); //all norms have the same root 
		
		Set<String> intersectionParameter = new HashSet<String>();
		int countParameters = 0;
		
		for (Map.Entry<String,Set<String>> entry : mapParameters.entrySet()) { //runs map of parameters
		    String key = entry.getKey(); //take a parameter
		    
		    for (Norm n: norms) {
		    	Map<String,Set<String>> mapTemp = n.getBehavior().getMap();
		    	Set<String> values = mapTemp.get(key); //take the set with parameter name 'key'
		    	
		    	//realizes the intersection between the values of a specific parameter
		    	if (countParameters++ == 0) {
		    		intersectionParameter = SetUtil.union(intersectionParameter, values);
		    	} else {
		    		intersectionParameter = SetUtil.intersection(intersectionParameter, values);
		    	}
		    }
		    Set<String> copy = new HashSet<String>();
		    Iterator<String> it = intersectionParameter.iterator();
		    while (it.hasNext()) {
		    	copy.add((String)this.deepClone(it.next()));
		    }
		    intersections.put(key, copy);
		    countParameters = 0;
		    intersectionParameter = SetUtil.cleanSet(intersectionParameter);
		}	
		return intersections;
	}
	
	private List<List<Norm>> generateAllCombinations(List<Norm> norms, int i) {
		//System.out.println("Análise será feita com  o seguinte número de normas: " + i);
		ICombinatoricsVector<Norm> initialVector = Factory.createVector(norms);
		   
		Generator<Norm> gen = Factory.createSimpleCombinationGenerator(initialVector, i);
		
		List<List<Norm>> r = new ArrayList<List<Norm>>();
		
		for (ICombinatoricsVector<Norm> combination : gen) {
			r.add(combination.getVector());
		}
		return r;
	}

	public void insertAndClassifyAllNorms (List<Norm> normSet) {
		for (Norm norm: normSet) {
			this.separateNormsInSetsByInsertion(norm);
		}
	}
	
	private void separateNormsInSetsByInsertion(Norm norm) {
		int n = groupNorms.size();
		if (n == 0) {
			List<Norm> temp = new ArrayList<Norm>();
			temp.add(norm);
			groupNorms.add(temp);
			return;
		}
		
		for (int i = 0; i < groupNorms.size(); i++) {
			List<Norm> temp = groupNorms.get(i);
			if (this.isThereEquivalenceBetweenNorms(temp.get(0), norm)) {
				groupNorms.get(i).add(norm);
				return;
			}
		}
		
		//there is no equivalence previously
		List<Norm> temp = new ArrayList<Norm>();
		temp.add(norm);
		groupNorms.add(temp);
	}

	private boolean isThereEquivalenceBetweenNorms(Norm norm1, Norm norm2) {
		// returns true if the context of the norms are the same
		boolean conflictContext = contextChecker(norm1, norm2);
		if (!conflictContext) {
			return false;
		}

		// returns true if the if the entities are the same OR one is ALL
		boolean conflictEntity = entityChecker(norm1, norm2);
		if (!conflictEntity) {
			return false;
		}

		// returns true if there is not conflict between activation and deactivation constraint
		boolean conflictConstraint = constraintChecker(norm1, norm2);
		if (!conflictConstraint) {
			return false;
		}

		// returns true if the action are the same
		boolean conflictAction = actionChecker(norm1, norm2);
		if (!conflictAction) {
			return false;
		}

		// at this moment all conditions are valid and the norms are in conflict
		return true;
	}
	
	private boolean contextChecker(Norm norm1, Norm norm2) {
		Context c1 = norm1.getContext();
		Context c2 = norm2.getContext();

		if (c1 == null || c1.getName() == null || c1.getContextType() == null) {
			c1 = new Context("context", ContextType.ORGANIZATION);
			norm1.setContext(c1);
		}

		if (c2 == null || c2.getName() == null || c2.getContextType() == null) {
			c2 = new Context("context", ContextType.ORGANIZATION);
			norm2.setContext(c2);
		}

		if (norm1.getContext().equals(norm2.getContext())) {
			return true;
		}
		return false;
	}
	
	private boolean entityChecker(Norm norm1, Norm norm2) {

		Entity e1 = norm1.getEntity();
		Entity e2 = norm2.getEntity();
		//it is implementation is different from the last version. Here
		//we consider if the entity is null it can be modified
		
		boolean flag1 = false;
		boolean flag2 = false;

		if (e1 == null || e1.getName() == null || e1.getEntityType() == null) {
			e1 = new Entity("entity", EntityType.ALL);
			norm1.setEntity(e1);
			flag1 = true;
		}
		if (e2 == null || e2.getName() == null || e2.getEntityType() == null) {
			e2 = new Entity("entity", EntityType.ALL);
			norm1.setEntity(e1);
			flag2 = true;
		}
		
		if (flag1 && flag2) {
			return true;
		}
		//if flag's are false
		// if the execution arrived here means that all fields are filled
		if (e1.getEntityType().equals(EntityType.ALL)) {
			e1.setEntityType(e2.getEntityType());
			norm2.setEntity(e2);
		}
		if (e2.getEntityType().equals(EntityType.ALL)) {
			e2.setEntityType(e1.getEntityType());
			norm2.setEntity(e2);
		}

		// if the entities are equal
		if (norm1.getEntity().equals(norm2.getEntity())) {
			return true;
		}

		return false;
	}

	private boolean actionChecker(Norm norm1, Norm norm2) {
		//its implementation was changed from the last version
		if (norm1.getBehavior() == null || norm2.getBehavior() == null) {
			return false;
		}
		String actionName1 = norm1.getBehavior().getName();
		String actionName2 = norm2.getBehavior().getName();
		
		if (actionName1.equals(actionName2)) {
			return true;
		}
		return false;
		//its missing new cases for new behaviors
	}
	
	private boolean constraintChecker(Norm norm1, Norm norm2) {
		
		//for realize the comparisons all the fields must be filled, if one field is null then we don't have problem
		if (norm1.getActivationConstraint() == null || norm1.getDeactivationConstraint() == null ||
			norm2.getActivationConstraint() == null || norm2.getDeactivationConstraint() == null) {
			
			norm1.setActivationConstraint(null);
			norm2.setActivationConstraint(null);
			norm1.setDeactivationConstraint(null);
			norm2.setDeactivationConstraint(null);
			
			return true;
		}
		
		ConstraintType na1 = norm1.getActivationConstraint().getConstraintType();
		ConstraintType nd1 = norm1.getDeactivationConstraint().getConstraintType();
		
		ConstraintType na2 = norm2.getActivationConstraint().getConstraintType();
		ConstraintType nd2 = norm2.getDeactivationConstraint().getConstraintType();
		
		//it is necessary only 3 tests
		if (!na1.equals(nd1) || !na2.equals(nd2) || !na1.equals(na2)) {
			norm1.setActivationConstraint(null);
			norm2.setActivationConstraint(null);
			norm1.setDeactivationConstraint(null);
			norm2.setDeactivationConstraint(null);
			
			return true;
		}
		
		// If the activation conditions are actions
		if (norm1.getActivationConstraint().getConstraintType().equals(ConstraintType.ACTIONTYPE)
				&& norm1.getActivationConstraint().getConstraintType().equals(ConstraintType.ACTIONTYPE)) {

			//todo...o tratamento vai ser realizado no futuro, caso necessário
				
			return true;
		}
		
		//
		//at this moment the constrainttype are both DATETYPE, so it is not necessary more comparisons
		//
		
		DateTime d1Begin = ((ConstraintDate) norm1.getActivationConstraint()).getDate();
		DateTime d1End = ((ConstraintDate) norm1.getDeactivationConstraint()).getDate();
		DateTime d2Begin = ((ConstraintDate) norm2.getActivationConstraint()).getDate();
		DateTime d2End = ((ConstraintDate) norm2.getDeactivationConstraint()).getDate();
		
		boolean r = this.compareDateIntervals(d1Begin, d1End, d2Begin, d2End);
		return r;
	}
	
	private boolean compareDateIntervals(DateTime d1Begin, DateTime d1End, DateTime d2Begin, DateTime d2End){
		Interval i1 = new Interval(d1Begin,d1End);
		Interval i2 = new Interval(d2Begin,d2End);
		return i1.overlaps(i2);
		//http://stackoverflow.com/questions/17106670/how-to-check-a-timeperiod-is-overlapping-another-time-period-in-java
	}
	
	public void printNorms() {
		for(List<Norm> list: groupNorms) {
			for(Norm norm: list) {
				System.out.println(norm.toString());
			}
			System.out.println();
		}
	}
	
	public void printNorms(List<List<Norm>> norms) {
		for(List<Norm> list: norms) {
			for(Norm norm: list) {
				System.out.println(norm.toString());
			}
			System.out.println();
		}
	}
	
	private Object deepClone(Object object) {
	    try {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(baos);
	      oos.writeObject(object);
	      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	      ObjectInputStream ois = new ObjectInputStream(bais);
	      return ois.readObject();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	}
}
