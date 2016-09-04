package behavior;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import behavior.exception.ResultEventAttributeException;
import behavior.exception.RuleActionException;
import behavior.exception.RuleActionExpressionException;
import behavior.exception.RuleConditionException;
import behavior.exception.RuleConditionExpressionException;
import behavior.exception.RuleEventException;
import behavior.exception.RuleEventExpressionException;
import behavior.exception.RuleException;
import behavior.exception.WidgetAttributeException;

import sketch.exception.SketchException;

public class Rule extends Composite {
	
	private Behavior parent;
		
	private String eventExpressionString;
	
	private String conditionExpressionString;
	
	private String actionExpressionString;
	
	private EventExpression eventExpression;
	
	private ConditionExpression conditionExpression;
	
	private ActionExpression actionExpression;


	public Rule(Composite parent, int style) throws Exception {
		super(parent, style);
				
		if(!(parent instanceof Behavior))
			throw new Exception("Rule must belong to Behavior.");
		
		this.parent = (Behavior)parent;
		
		conditionExpression = new ConditionExpression(this, "");
		
		this.setEnabled(false);
		this.setVisible(false);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}

	public String getEventExpression() {
		return eventExpressionString;
	}
	
	public EventExpression eventExpression() {
		return eventExpression;
	}

	public void setEventExpression(String eventExpressionString) throws RuleEventExpressionException, RuleEventException, WidgetAttributeException, ResultEventAttributeException {
		if(eventExpressionString.equals("")) {
			eventExpression = null;
			eventExpressionString = null;
			return;
		}
		
		this.eventExpressionString = eventExpressionString;
		
		try {
			eventExpression = new EventExpression(this, eventExpressionString);
		}
		catch(RuleEventExpressionException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(RuleEventException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(WidgetAttributeException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(ResultEventAttributeException e) {
			if(parent.isCuiValidation())
				throw e;
		}
				
		if(actionExpression != null) {		
			parent.addRule(this);
		}
	}

	public String getConditionExpression() {
		return conditionExpressionString;
	}
	
	public ConditionExpression conditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpressionString) throws RuleConditionExpressionException, RuleConditionException, WidgetAttributeException {
		if(conditionExpressionString.equals("")) {
			conditionExpression = new ConditionExpression(this, "");
			conditionExpressionString = null;
			return;
		}
		
		
		this.conditionExpressionString = conditionExpressionString;
		
		try {
			conditionExpression = new ConditionExpression(this, conditionExpressionString);
		}
		catch(RuleConditionExpressionException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(RuleConditionException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(WidgetAttributeException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		
		
		if(eventExpression != null && actionExpression != null) {
			parent.addRule(this);
		}
	}

	public String getActionExpression() {
		return actionExpressionString;
	}
	
	public ActionExpression actionExpression() {
		return actionExpression;
	}

	public void setActionExpression(String actionExpressionString) throws RuleActionExpressionException, RuleActionException, WidgetAttributeException, ResultEventAttributeException {
		
		if(actionExpressionString.equals("")) {
			actionExpression = null;
			actionExpressionString = null;
			return;
		}
		
		this.actionExpressionString = actionExpressionString;
		
		try {
			actionExpression = new ActionExpression(this, actionExpressionString);
		}
		catch(RuleActionExpressionException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(RuleActionException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(WidgetAttributeException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		catch(ResultEventAttributeException e) {
			if(parent.isCuiValidation())
				throw e;
		}
		
		if(eventExpression != null) {		
			parent.addRule(this);
		}
	}
	

	public String toString() {
		return "ON " + eventExpressionString + "\nIF " + conditionExpressionString + "\nTHEN " + actionExpressionString; 
	}
	

}
