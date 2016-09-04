package behavior;

import org.eclipse.swt.widgets.Button;

@SuppressWarnings("rawtypes")
public enum ConditionOperator {
	
	ISEQUAL("=="),
	ISDIFFERENT("!=");
	
	
	private String representation;
	private Class leftOperand;
	private Class rightOperand;
	
	ConditionOperator(String representation, Class leftOperand, Class rightOperand) {
		this.representation = representation;
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}
	
	ConditionOperator(String representation) {
		this(representation, null, null);
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public Class getLeftOperand() {
		return leftOperand;
	}
	
	public Class getRightOperand() {
		return rightOperand;
	}
	
	public static ConditionOperator getConditionOperator(String representation) {
		for(ConditionOperator conditionOperator: ConditionOperator.values())
			if(conditionOperator.getRepresentation().equals(representation))
				return conditionOperator;
		
		return null;
	}
}
