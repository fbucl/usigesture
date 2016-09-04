package behavior;

import behavior.exception.RuleConditionException;
import behavior.exception.RuleException;
import behavior.exception.WidgetAttributeException;

public class Condition {
	
	private Rule parentRule;
	private String condition;
	private Object leftOperand, rightOperand;
	private ConditionOperator conditionOperator;
	
	public Condition(Rule parentRule, String condition) throws RuleConditionException, WidgetAttributeException {
		
		this.parentRule = parentRule;
		this.condition = condition;
		
		this.parse();
	}
	
	public String toString() {
		return condition;
	}
	
	private void parse() throws RuleConditionException, WidgetAttributeException {
		for(ConditionOperator conditionOperator: ConditionOperator.values()) {
			String operatorRepresentation = conditionOperator.getRepresentation();
			if(condition.indexOf(operatorRepresentation) != -1) {
				String[] operands = condition.split(operatorRepresentation);
				if(operands.length != 2)
					throw new RuleConditionException("Condition \'" + condition + "\' must have two operands.");
				
				this.conditionOperator = conditionOperator;
				leftOperand = operands[0].startsWith("$")?new WidgetAttribute(parentRule, operands[0]):operands[0];
				rightOperand = operands[1].startsWith("$")?new WidgetAttribute(parentRule, operands[1]):operands[1];
				
				return;
			}
		}
		throw new RuleConditionException("No valid operator in condition \'" + condition + "\'");
	}
	
	public boolean evaluate() {
		switch(conditionOperator){
		case ISEQUAL:
			String leftOperandString = (leftOperand instanceof WidgetAttribute)?(String)((WidgetAttribute)leftOperand).getWidgetAttribute():(String)leftOperand;
			String rightOperandString = (rightOperand instanceof WidgetAttribute)?(String)((WidgetAttribute)rightOperand).getWidgetAttribute():(String)rightOperand;
			return leftOperandString.equals(rightOperandString);
		}
		
		return false;
	}

}
