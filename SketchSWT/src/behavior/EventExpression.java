package behavior;

import java.util.ArrayList;
import java.util.List;

import utils.BinaryTree;
import utils.Node;

import behavior.exception.ResultEventAttributeException;
import behavior.exception.RuleEventException;
import behavior.exception.RuleEventExpressionException;
import behavior.exception.WidgetAttributeException;

public class EventExpression {
	
	private Rule parentRule;
	private String eventExpression;
	private BinaryTree eventExpressionTree;
	private List<Event> events;
	private List<org.eclipse.swt.widgets.Event> occurredSwtEvents;
	
	public EventExpression(Rule parentRule, String eventExpression) throws RuleEventExpressionException, RuleEventException, WidgetAttributeException, ResultEventAttributeException {
		this.parentRule = parentRule;
		this.eventExpression = eventExpression;
		this.events = new ArrayList<Event>();
		this.occurredSwtEvents = new ArrayList<org.eclipse.swt.widgets.Event>();
		
		this.parse();
	}
	
	public String toString() {
		return eventExpression;
	}
	
	private void parse() throws RuleEventException, WidgetAttributeException, ResultEventAttributeException, RuleEventExpressionException {
		eventExpressionTree = new BinaryTree(parseEventExpression(eventExpression, getEventExpressionOperators()));
	}
	
	private Node parseEventExpression(String eventExpression, ArrayList<EventExpressionOperator> eventExpressionOperators) throws RuleEventExpressionException, RuleEventException, WidgetAttributeException, ResultEventAttributeException {
		
		int firstLeftBracketPosition = eventExpression.indexOf("[");
		if(firstLeftBracketPosition == 0) {
			int lastRightBracketPosition = eventExpression.lastIndexOf("]");
			if(lastRightBracketPosition == -1)
				throw new RuleEventExpressionException("(Sub)event expression \'" + eventExpression + "\' not closed with a right bracket.");
			
			//eventExpression = [...] 
			if(lastRightBracketPosition == eventExpression.length()-1) 
				return parseEventExpression(eventExpression.substring(1, eventExpression.length()-1), getEventExpressionOperators());
			
			//eventExpression = [...]...
			EventExpressionOperator eventExpressionOperator = EventExpressionOperator.eventExpressionOperator(eventExpression.substring(lastRightBracketPosition+2, lastRightBracketPosition+4));
			Node leftChild = parseEventExpression(eventExpression.substring(1, lastRightBracketPosition), getEventExpressionOperators());
			Node rightChild = parseEventExpression(eventExpression.substring(lastRightBracketPosition+5, eventExpression.length()), eventExpressionOperators);
			return new Node(eventExpressionOperator, leftChild, rightChild);
		}
		
		
		ArrayList<EventExpressionOperator> eventExpressionOperatorsClone = (ArrayList<EventExpressionOperator>)eventExpressionOperators.clone();
		while(eventExpressionOperatorsClone.size() > 0) {
			EventExpressionOperator eventExpressionOperator = eventExpressionOperatorsClone.get(0);
			eventExpressionOperatorsClone.remove(0);
			int eventExpressionOperatorPosition = eventExpression.indexOf(" " + eventExpressionOperator.getRepresentation() + " ");
			if(eventExpressionOperatorPosition!=-1 && (eventExpressionOperatorPosition < firstLeftBracketPosition || firstLeftBracketPosition == -1)) {
				return new Node(eventExpressionOperator, parseEventExpression(eventExpression.substring(0, eventExpressionOperatorPosition), eventExpressionOperatorsClone), parseEventExpression(eventExpression.substring(eventExpressionOperatorPosition+4, eventExpression.length()), eventExpressionOperators));
			}
		}
		Event event = new Event(parentRule, eventExpression);
		this.events.add(event);
		return new Node(event);
	}
	
	private ArrayList<EventExpressionOperator> getEventExpressionOperators() {
		ArrayList<EventExpressionOperator> eventExpressionOperators = new ArrayList<EventExpressionOperator>();
		eventExpressionOperators.add(EventExpressionOperator.OR);
		eventExpressionOperators.add(EventExpressionOperator.AND);
		return eventExpressionOperators;
	}
	
	public void eventOccurred(org.eclipse.swt.widgets.Event event) {
		occurredSwtEvents.add(event);
	}
	
	public void deleteOccurredEvents() {
		while(occurredSwtEvents.size() > 0)
			occurredSwtEvents.remove(0);
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	public boolean evaluate() {
		return evaluateNode(eventExpressionTree.getRoot());
	}
	
	private boolean evaluateNode(Node node) {
		if(node.isLeaf()) {
			Event event = (Event)node.data;
			for(org.eclipse.swt.widgets.Event occurredSwtEvent: occurredSwtEvents) {
				//System.out.println("--");
				//System.out.println(occurredSwtEvent.widget == event.getSourceWidget());
				//System.out.println(occurredSwtEvent.type == event.getEventType().getSwtEvent());
				if(occurredSwtEvent.widget == event.getSourceWidget() && occurredSwtEvent.type == event.getEventType().getSwtEvent())
					return true;
			}
			return false;
		}
		else {
			EventExpressionOperator eventExpressionOperator = (EventExpressionOperator)node.data;
			switch(eventExpressionOperator) {
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
