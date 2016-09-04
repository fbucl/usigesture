package behavior;

public enum EventExpressionOperator {

	AND("&&"),
	OR("||");
	
	private String representation;
	
	EventExpressionOperator(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public static EventExpressionOperator eventExpressionOperator(String representation) {
		for(EventExpressionOperator eventExpressionOperator: EventExpressionOperator.values())
			if(representation.equals(eventExpressionOperator.representation))
				return eventExpressionOperator;
		
		return null;
	}
	
	public String toString() {
		return representation;
	}
	
}
