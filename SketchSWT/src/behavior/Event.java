package behavior;

import java.util.List;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.widgets.Widget;

import behavior.exception.RuleEventException;
import behavior.exception.RuleException;


public class Event {
	
	private Rule parentRule;
	private String event;
	private EventType eventType;
	private Widget sourceWidget;
	
	public Event(Rule parentRule, String event) throws RuleEventException {
		this.parentRule = parentRule;
		this.event = event;
		
		this.parse();
	}
	
	public String toString() {
		return event;
	}
	
	
	private void parse() throws RuleEventException {
		//Event erased
		if(event.equals(""))
			return;
		//set null
		
		
		//Check if event start with '$'
		if(!event.startsWith("$"))
			throw new RuleEventException("Event \'" + event + "\' must be applied on a widget, starting by \'$\' character.");
		
		//Parse source widget name
		int firstDotIndex = event.indexOf('.');
		String sourceWidgetString = firstDotIndex==-1?event.substring(1, event.length()):event.substring(1, firstDotIndex);
		parseSourceWidget(sourceWidgetString);
		
		if(firstDotIndex == -1)
			throw new RuleEventException("No event specified on widget \'" + sourceWidgetString + "\'.");
		
		int firstLeftParenthesisIndex = event.indexOf('(');
		String eventTypeString = firstLeftParenthesisIndex==-1?event.substring(firstDotIndex + 1, event.length()):event.substring(firstDotIndex + 1, firstLeftParenthesisIndex);
		parseEventType(eventTypeString);
		
		if(firstLeftParenthesisIndex == -1)
			return;
		
		int lastRightParenthesisIndex = event.lastIndexOf(')');
		if(lastRightParenthesisIndex == -1)
			throw new RuleEventException("Event \'" + event + "\' argument list must end with a right parenthesis.");
		if(lastRightParenthesisIndex != event.length()-1)
			throw new RuleEventException("Event \'" + event + "\' must end with a right parenthesis.");
		
		
		
	}
	
	private void parseSourceWidget(String sourceWidgetString) throws RuleEventException {
		sourceWidget = (Widget)XWT.findElementByName(parentRule, sourceWidgetString);
		if(sourceWidget == null)
			throw new RuleEventException("\'" + sourceWidgetString + "\' is not a valid widget name or is not in the scope of the listener." );
	}
	
	private void parseEventType(String eventTypeString) throws RuleEventException {
		EventType[] eventTypes = EventType.values();
		for(int i = 0; i < eventTypes.length; i++)
			if(eventTypeString.equals(eventTypes[i].toString())) {
				eventType = eventTypes[i];
				i = eventTypes.length;
			}
		
		if(eventType == null)
			throw new RuleEventException("\'" + eventTypeString + "\' is not a valid event type.");
		
		if(eventType.getSourceSpec() != null) {
			boolean containsClass = false;
			for(Class c: eventType.getSourceSpec())
				containsClass = containsClass || sourceWidget.getClass()==c; 
			if(!containsClass)
				throw new RuleEventException("Class type \'" + sourceWidget.getClass() + "\' of source widget not supported for event type \'" + eventTypeString + "\'.");
		}
	}
	
	
	public Widget getSourceWidget() {
		return sourceWidget;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	
}


