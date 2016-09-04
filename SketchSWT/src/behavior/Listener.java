package behavior;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import sketch.SketchEvent;
import behavior.exception.ListenerException;

public class Listener extends Composite {
	
	private Behavior parent;
	
	private Map<Event, org.eclipse.swt.widgets.Listener> widgetListeners;
	
	public Listener(Composite parent, int style) throws ListenerException {
		super(parent, style);
		
		this.setVisible(false);
		
		this.widgetListeners = new Hashtable<Event, org.eclipse.swt.widgets.Listener>();
		
		if(!(parent instanceof Behavior))
			throw new ListenerException("Listener must belong to Behavior.");
		
		this.parent = (Behavior)parent;
	}
	
	
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
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
		
		
		
		/*
		
		if(widgetListeners.get(event.getSourceWidget()) != null)
			event.getSourceWidget().removeListener(event.getEventType().getSwtEvent(), widgetListeners.get(event.getSourceWidget()));
		
		org.eclipse.swt.widgets.Listener swtListener = new org.eclipse.swt.widgets.Listener() {
			
			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				if(actionExpression != null && conditionExpression != null) {
					if(conditionExpression.evaluate())
						actionExpression.perform(event);
				}
			}
		};
		
		event.getSourceWidget().addListener(event.getEventType().getSwtEvent(), swtListener);
		widgetListeners.put(event.getSourceWidget(), swtListener);
		
		*/
		
	}
	
	public Behavior parent() {
		return parent;
	}
	
	/*protected Map<String, Widget> getWidgetsInListenerContext() {
		Map<String, Widget> widgetsInListenerContext = new Hashtable<String, Widget>();
		if(XWT.getElementName(parent) != null)
			widgetsInListenerContext.put(XWT.getElementName(parent), parent);
		addChildrenWidgets(parent, widgetsInListenerContext);
		return widgetsInListenerContext;
		
	}
	
	private void addChildrenWidgets(Composite composite, Map<String, Widget> widgetsInListenerContext) {
		for(Widget widget: composite.getChildren()) {
			System.out.println("obj name: " + XWT.getElementName(widget) + widget + "sdqf");
			widgetsInListenerContext.put("test", (Widget)XWT.findElementByName(parent, "button"));
			if(XWT.getElementName(widget) != null)
				widgetsInListenerContext.put(XWT.getElementName(widget), widget);
			if(widget instanceof Composite)
				addChildrenWidgets((Composite)widget, widgetsInListenerContext);
		}
	}*/
	

}
