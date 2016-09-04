package behavior;

public enum ActionExpressionOperator {

	ENABLING(">>");
	
	private String representation;
	
	ActionExpressionOperator(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public static ActionExpressionOperator actionExpressionOperator(String representation) {
		for(ActionExpressionOperator actionExpressionOperator: ActionExpressionOperator.values())
			if(representation.equals(actionExpressionOperator.representation))
				return actionExpressionOperator;
		
		return null;
	}
	
	public String toString() {
		return representation;
	}
	
}
