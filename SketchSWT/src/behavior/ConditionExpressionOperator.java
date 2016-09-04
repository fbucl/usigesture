package behavior;

public enum ConditionExpressionOperator {

	AND("&&"),
	OR("||");
	
	private String representation;
	
	ConditionExpressionOperator(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public static ConditionExpressionOperator conditionExpressionOperator(String representation) {
		for(ConditionExpressionOperator conditionExpressionOperator: ConditionExpressionOperator.values())
			if(representation.equals(conditionExpressionOperator.representation))
				return conditionExpressionOperator;
		
		return null;
	}
	
	public String toString() {
		return representation;
	}
	
}
