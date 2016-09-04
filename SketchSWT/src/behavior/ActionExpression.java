package behavior;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Event;

import utils.BinaryTree;
import utils.Node;

import behavior.exception.ResultEventAttributeException;
import behavior.exception.RuleActionException;
import behavior.exception.RuleActionExpressionException;
import behavior.exception.WidgetAttributeException;

public class ActionExpression {
	
	private Rule parentRule;
	private String actionExpression;
	private BinaryTree actionExpressionTree;
	
	public ActionExpression(Rule parentRule, String actionExpression) throws RuleActionExpressionException, RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		this.parentRule = parentRule;
		this.actionExpression = actionExpression;
		
		this.parse();
	}
	
	public String toString() {
		return actionExpression;
	}
	
	private void parse() throws RuleActionException, WidgetAttributeException, ResultEventAttributeException, RuleActionExpressionException {
		actionExpressionTree = new BinaryTree(parseActionExpression(actionExpression, getActionExpressionOperators()));
	}
	
	private Node parseActionExpression(String actionExpression, ArrayList<ActionExpressionOperator> actionExpressionOperators) throws RuleActionExpressionException, RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		
		int firstLeftBracketPosition = actionExpression.indexOf("[");
		if(firstLeftBracketPosition == 0) {
			int lastRightBracketPosition = actionExpression.lastIndexOf("]");
			if(lastRightBracketPosition == -1)
				throw new RuleActionExpressionException("(Sub)action expression \'" + actionExpression + "\' not closed with a right bracket.");
			
			//actionExpression = [...] 
			if(lastRightBracketPosition == actionExpression.length()-1) 
				return parseActionExpression(actionExpression.substring(1, actionExpression.length()-1), getActionExpressionOperators());
			
			//actionExpression = [...]...
			ActionExpressionOperator actionExpressionOperator = ActionExpressionOperator.actionExpressionOperator(actionExpression.substring(lastRightBracketPosition+2, lastRightBracketPosition+4));
			Node leftChild = parseActionExpression(actionExpression.substring(1, lastRightBracketPosition), getActionExpressionOperators());
			Node rightChild = parseActionExpression(actionExpression.substring(lastRightBracketPosition+5, actionExpression.length()), actionExpressionOperators);
			return new Node(actionExpressionOperator, leftChild, rightChild);
		}
		
		
		ArrayList<ActionExpressionOperator> actionExpressionOperatorsClone = (ArrayList<ActionExpressionOperator>)actionExpressionOperators.clone();
		while(actionExpressionOperatorsClone.size() > 0) {
			ActionExpressionOperator actionExpressionOperator = actionExpressionOperatorsClone.get(0);
			actionExpressionOperatorsClone.remove(0);
			int actionExpressionOperatorPosition = actionExpression.indexOf(" " + actionExpressionOperator.getRepresentation() + " ");
			if(actionExpressionOperatorPosition!=-1 && (actionExpressionOperatorPosition < firstLeftBracketPosition || firstLeftBracketPosition == -1)) {
				return new Node(actionExpressionOperator, parseActionExpression(actionExpression.substring(0, actionExpressionOperatorPosition), actionExpressionOperatorsClone), parseActionExpression(actionExpression.substring(actionExpressionOperatorPosition+4, actionExpression.length()), actionExpressionOperators));
			}
		}
		return new Node(new Action(parentRule, actionExpression));
	}
	
	private ArrayList<ActionExpressionOperator> getActionExpressionOperators() {
		ArrayList<ActionExpressionOperator> actionExpressionOperators = new ArrayList<ActionExpressionOperator>();
		actionExpressionOperators.add(ActionExpressionOperator.ENABLING);
		return actionExpressionOperators;
	}
	
	public void perform(Event event) {
		performNode(actionExpressionTree.getRoot(), event);
	}
	
	private void performNode(Node node, Event event) {
		if(node.isLeaf()) {
			((Action)node.data).perform(event);
		}
		else {
			ActionExpressionOperator actionExpressionOperator = (ActionExpressionOperator)node.data;
			switch(actionExpressionOperator) {
			case ENABLING:
				performNode(node.leftChild, event);
				performNode(node.rightChild, event);
				break;
			}
		}
	}
	
}
