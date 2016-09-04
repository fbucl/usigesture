package behavior;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.Event;

import sketch.SketchArea;

import behavior.exception.ResultEventAttributeException;
import behavior.exception.RuleActionException;
import behavior.exception.RuleEventException;
import behavior.exception.WidgetAttributeException;

public class Action {
	
	private Rule parentRule;
	private String action;
	private Widget targetWidget;
	private ActionType actionType;
	private Object[] arguments;
	
	public Action(Rule parentRule, String action) throws RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		this.parentRule = parentRule;
		this.action = action;
		
		this.parse();
	}
	
	public String toString() {
		return action;
	}
	
	private void parse() throws RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		//Event erased
		if(action.equals(""))
			return;
		//set null
		
		
		//Check if event start with '$'
		if(!action.startsWith("$"))
			throw new RuleActionException("Action \'" + action + "\' must be applied on a widget, starting by \'$\' character.");
		
		//Parse source widget name
		int firstDotIndex = action.indexOf('.');
		String targetWidgetString = firstDotIndex==-1?action.substring(1, action.length()):action.substring(1, firstDotIndex);
		parseTargetWidget(targetWidgetString);
		
		if(firstDotIndex == -1)
			throw new RuleActionException("No action specified on widget \'" + targetWidgetString + "\'.");
		
		int firstLeftParenthesisIndex = action.indexOf('(');
		String actionTypeString = firstLeftParenthesisIndex==-1?action.substring(firstDotIndex + 1, action.length()):action.substring(firstDotIndex + 1, firstLeftParenthesisIndex);
		parseActionType(actionTypeString);
		
		if(firstLeftParenthesisIndex == -1)
			return;
		
		int lastRightParenthesisIndex = action.lastIndexOf(')');
		if(lastRightParenthesisIndex == -1)
			throw new RuleActionException("Action \'" + action + "\' argument list must end with a right parenthesis.");
		if(lastRightParenthesisIndex != action.length()-1)
			throw new RuleActionException("Action \'" + action + "\' must end with a right parenthesis.");
		
		String argumentsString = action.substring(firstLeftParenthesisIndex + 1, lastRightParenthesisIndex);
		parseArguments(argumentsString);
		
	}
	
	private void parseTargetWidget(String targetWidgetString) throws RuleActionException {
		targetWidget = (Widget)XWT.findElementByName(parentRule, targetWidgetString);
		if(targetWidget == null)
			throw new RuleActionException("\'" + targetWidgetString + "\' is not a valid widget name or is not in the scope of the listener." );
	}
	
	private void parseActionType(String actionTypeString) throws RuleActionException {
		ActionType[] actionTypes = ActionType.values();
		for(int i = 0; i < actionTypes.length; i++)
			if(actionTypeString.equals(actionTypes[i].toString())) {
				actionType = actionTypes[i];
				i = actionTypes.length;
			}
		
		if(actionType == null)
			throw new RuleActionException("\'" + actionTypeString + "\' is not a valid action type.");
		
		if(actionType.getTargetSpec() != null) {
			boolean containsClass = false;
			for(Class c: actionType.getTargetSpec()) {
				containsClass = containsClass || targetWidget.getClass()==c;
			}
			if(!containsClass)
				throw new RuleActionException("Class type \'" + targetWidget.getClass() + "\' of target widget not supported for action type \'" + actionTypeString + "\'.");
		}
	}
	
	private void parseArguments(String argumentsString) throws RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		if(argumentsString.equals(""))
			return;
		String[] splittedArgumentsString = argumentsString.split(",");
		
		if(actionType.getArgumentSpec() != null)
			if(actionType.getArgumentSpec().length != splittedArgumentsString.length)
				throw new RuleActionException("Wrong number of argument for action \'" + this + "\'.");

		arguments = new Object[splittedArgumentsString.length];
		for(int i = 0; i < splittedArgumentsString.length; i++) {
			/*if(splittedArgumentsString[i].startsWith("$event"))
				arguments[i] = new ResultEventAttribute(splittedArgumentsString[i]);
			else */
			if(splittedArgumentsString[i].startsWith("$")) {
				WidgetAttribute widgetAttribute = new WidgetAttribute(parentRule, splittedArgumentsString[i]);
				if(actionType.getArgumentSpec() != null && widgetAttribute.getWidgetAttributeType().returnSpec() != null)
					if(actionType.getArgumentSpec()[i] != widgetAttribute.getWidgetAttributeType().returnSpec())
						throw new RuleActionException("Class type \'" + widgetAttribute.getWidgetAttributeType().returnSpec() + "\' of widget attribute \'" + widgetAttribute + "\' mismatch expected class type \'" + actionType.getArgumentSpec()[i] + "\' for argument number " + (i+1) + " in action \'" + this + "\'.");
				arguments[i] = widgetAttribute;
			}
			else {
				arguments[i] = splittedArgumentsString[i];
				if(actionType.getArgumentSpec() != null)
					try {
						actionType.getArgumentSpec()[i].cast(arguments[i]);
					}
					catch(ClassCastException cce) {
						throw new RuleActionException("Impossible to cast argument number " + (i+1) + " to expected class type \'" + actionType.getArgumentSpec()[i] + "\' in action \'" + this + "\'.");
					}
			}
		}		
	}
	
	public void perform(Event event) {
		if(targetWidget == null)
			return;
		if(actionType == null)
			return;
		
		if(targetWidget instanceof Button) {
			Button button = (Button)targetWidget;
			switch(actionType) {
			case SET_TEXT:
				if(arguments == null || arguments.length != 1)
					return;
				String text = (arguments[0] instanceof ResultEventAttribute)?((String)((ResultEventAttribute)arguments[0]).getResultEventAttribute(event)):(arguments[0] instanceof WidgetAttribute)?((String)((WidgetAttribute)arguments[0]).getWidgetAttribute()):(String)arguments[0];
				button.setText((text));
				break;
			}
		}
		
		if(targetWidget instanceof Label) {
			Label label = (Label)targetWidget;
			switch(actionType) {
			case SET_TEXT:
				if(arguments == null || arguments.length != 1)
					return;
				String text = (String)processArgument(arguments[0], event);
				label.setText((text));
				break;
			}
		}
		
		if(targetWidget instanceof Label) {
			Label label = (Label)targetWidget;
			switch(actionType) {
			case INCREMENT:
				int newVal = Integer.parseInt(label.getText()) + 1;
				label.setText(newVal+"");
				break;
			}
		}
		
		if(targetWidget instanceof Label) {
			Label label = (Label)targetWidget;
			switch(actionType) {
			case DECREMENT:
				int newVal = Integer.parseInt(label.getText()) - 1;
				label.setText(newVal+"");
				break;
			}
		}
		
		if(targetWidget instanceof Label) {
			Label label = (Label)targetWidget;
			switch(actionType) {
			case SET_TEXT:
				if(arguments == null || arguments.length != 1)
					return;
				String text = (String)processArgument(arguments[0], event);
				label.setText((text));
				break;
			}
		}
		
		if(targetWidget instanceof SketchArea) {
			SketchArea sketchArea = (SketchArea)targetWidget;
			switch(actionType) {
			case CLEAN_AREA:
				sketchArea.cleanArea();
				break;
			}
		}
	}
	
	private Object processArgument(Object argument, Event event) {
		return (argument instanceof ResultEventAttribute)?((ResultEventAttribute)argument).getResultEventAttribute(event):(argument instanceof WidgetAttribute)?((WidgetAttribute)argument).getWidgetAttribute():argument;
	}

}
