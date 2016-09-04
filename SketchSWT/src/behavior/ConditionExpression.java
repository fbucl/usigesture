package behavior;

import java.util.ArrayList;
import java.util.List;

import utils.BinaryTree;
import utils.Node;

import behavior.exception.RuleConditionException;
import behavior.exception.RuleConditionExpressionException;
import behavior.exception.WidgetAttributeException;

public class ConditionExpression {
	
	private Rule parentRule;
	private String conditionExpression;
	private BinaryTree conditionExpressionTree;
	
	public ConditionExpression(Rule parentRule, String conditionExpression) throws RuleConditionExpressionException, RuleConditionException, WidgetAttributeException {
		this.parentRule = parentRule;
		this.conditionExpression = conditionExpression;
		
		this.parse();
		
	}
	
	
	public String toString() {
		return conditionExpression; 
	}
	
	private void parse() throws RuleConditionExpressionException, RuleConditionException, WidgetAttributeException {
		if(conditionExpression.equals(""))
			return;
		
		conditionExpressionTree = new BinaryTree(parseConditionExpression(conditionExpression, getConditionExpressionOperators()));
	}
	
	
	private Node parseConditionExpression(String conditionExpression, ArrayList<ConditionExpressionOperator> conditionExpressionOperators) throws RuleConditionExpressionException, RuleConditionException, WidgetAttributeException {
		
		int firstLeftBracketPosition = conditionExpression.indexOf("[");
		if(firstLeftBracketPosition == 0) {
			int lastRightBracketPosition = conditionExpression.lastIndexOf("]");
			if(lastRightBracketPosition == -1)
				throw new RuleConditionExpressionException("(Sub)condition expression \'" + conditionExpression + "\' not closed with a right bracket.");
			
			//conditionExpression = [...] 
			if(lastRightBracketPosition == conditionExpression.length()-1) 
				return parseConditionExpression(conditionExpression.substring(1, conditionExpression.length()-1), getConditionExpressionOperators());
			
			//conditionExpression = [...]...
			ConditionExpressionOperator conditionExpressionOperator = ConditionExpressionOperator.conditionExpressionOperator(conditionExpression.substring(lastRightBracketPosition+2, lastRightBracketPosition+4));
			Node leftChild = parseConditionExpression(conditionExpression.substring(1, lastRightBracketPosition), getConditionExpressionOperators());
			Node rightChild = parseConditionExpression(conditionExpression.substring(lastRightBracketPosition+5, conditionExpression.length()), conditionExpressionOperators);
			return new Node(conditionExpressionOperator, leftChild, rightChild);
		}
		
		
		ArrayList<ConditionExpressionOperator> conditionExpressionOperatorsClone = (ArrayList<ConditionExpressionOperator>)conditionExpressionOperators.clone();
		while(conditionExpressionOperatorsClone.size() > 0) {
			ConditionExpressionOperator conditionExpressionOperator = conditionExpressionOperatorsClone.get(0);
			conditionExpressionOperatorsClone.remove(0);
			int conditionExpressionOperatorPosition = conditionExpression.indexOf(" " + conditionExpressionOperator.getRepresentation() + " ");
			if(conditionExpressionOperatorPosition!=-1 && (conditionExpressionOperatorPosition < firstLeftBracketPosition || firstLeftBracketPosition == -1)) {
				return new Node(conditionExpressionOperator, parseConditionExpression(conditionExpression.substring(0, conditionExpressionOperatorPosition), conditionExpressionOperatorsClone), parseConditionExpression(conditionExpression.substring(conditionExpressionOperatorPosition+4, conditionExpression.length()), conditionExpressionOperators));
			}
		}
		return new Node(new Condition(parentRule, conditionExpression));
	}
	
	private ArrayList<ConditionExpressionOperator> getConditionExpressionOperators() {
		ArrayList<ConditionExpressionOperator> conditionExpressionOperators = new ArrayList<ConditionExpressionOperator>();
		conditionExpressionOperators.add(ConditionExpressionOperator.OR);
		conditionExpressionOperators.add(ConditionExpressionOperator.AND);
		return conditionExpressionOperators;
	}
	
	public boolean evaluate() {
		if(conditionExpression.equals(""))
			return true;
				
		return evaluateNode(conditionExpressionTree.getRoot());
	}
	
	private boolean evaluateNode(Node node) {
		if(node.isLeaf())
			return ((Condition)node.data).evaluate();
		else {
			ConditionExpressionOperator conditionExpressionOperator = (ConditionExpressionOperator)node.data;
			switch(conditionExpressionOperator) {
			case AND:
				return evaluateNode(node.leftChild) && evaluateNode(node.rightChild);
			case OR:
				return evaluateNode(node.leftChild) || evaluateNode(node.rightChild);
			default:
				return false;
			}
		}
	}

}
