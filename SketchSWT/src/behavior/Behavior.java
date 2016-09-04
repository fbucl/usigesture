package behavior;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import behavior.exception.BehaviorException;

public class Behavior extends Composite {
	
	private boolean cuiValidation = true;
	
	private Map<Event, org.eclipse.swt.widgets.Listener> widgetListeners;
	
	public Behavior(Composite parent, int style) throws BehaviorException {
		super(parent, style);
		
		this.setVisible(false);
		
		if(!(parent instanceof Shell))
			throw new BehaviorException("Behavior must belong to the root Shell.");
		
		for(Control control: parent.getChildren())
			if(control instanceof Behavior && control != this)
				throw new BehaviorException("Behavior already exists.");
		
		this.widgetListeners = new Hashtable<Event, org.eclipse.swt.widgets.Listener>();
		
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public boolean isCuiValidation() {
		return cuiValidation;
	}

	public void setCuiValidation(boolean cuiValidation) {
		this.cuiValidation = cuiValidation;
	}
	
	public void addRule(Rule rule) {
		
		final EventExpression eventExpression = rule.eventExpression();
		final ActionExpression actionExpression = rule.actionExpression();
		final ConditionExpression conditionExpression = rule.conditionExpression();
		
		for(Event event: eventExpression.getEvents()) {
			if(widgetListeners.get(event) != null)
				event.getSourceWidget().removeListener(event.getEventType().getSwtEvent(), widgetListeners.get(event));
			
			org.eclipse.swt.widgets.Listener swtListener = new org.eclipse.swt.widgets.Listener() {
				
				public void handleEvent(org.eclipse.swt.widgets.Event event) {
					if(eventExpression != null && conditionExpression != null && actionExpression != null) {
						eventExpression.eventOccurred(event);
						if(eventExpression.evaluate() && conditionExpression.evaluate()) {
							actionExpression.perform(event);
							eventExpression.deleteOccurredEvents();
						}
					}
				}
			};
			
			event.getSourceWidget().addListener(event.getEventType().getSwtEvent(), swtListener);
			widgetListeners.put(event, swtListener);
		}

		
	}

}
