package behavior;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.Event;

import sketch.SketchEvent;

import behavior.exception.ResultEventAttributeException;
import behavior.exception.RuleEventException;
import behavior.exception.WidgetAttributeException;

public class ResultEventAttribute {

	private String resultEventAttribute;
	private ResultEventAttributeType resultEventAttributeType;
	
	public ResultEventAttribute(String resultEventAttribute) throws ResultEventAttributeException {
		this.resultEventAttribute = resultEventAttribute;
		
		this.parse();
	}
	
	public String toString() {
		return "ResultEventAttribute: " + resultEventAttribute;
	}
	
	private void parse() throws ResultEventAttributeException {
		
		
		if(!resultEventAttribute.startsWith("$event"))
			throw new ResultEventAttributeException("Event Result attribute \'" + resultEventAttribute + "\' must be applied on \'$event\' reserverd word.");
		
		//Parse widget name
		int firstDotIndex = resultEventAttribute.indexOf('.');
		String tmp = firstDotIndex==-1?resultEventAttribute.substring(0, resultEventAttribute.length()):resultEventAttribute.substring(0, firstDotIndex);
		if(!tmp.equals("$event"))
			throw new ResultEventAttributeException("Event Result attribute \'" + resultEventAttribute + "\' must be applied on \'$event\' reserverd word.");
		
		if(firstDotIndex == -1)
			throw new ResultEventAttributeException("No attribute specified on ResultEvent \'" + resultEventAttribute + "\'.");
		
		String resultEventAttributeTypeString = resultEventAttribute.substring(firstDotIndex + 1, resultEventAttribute.length());
		parseResultEventAttributeType(resultEventAttributeTypeString);
	}
	
	private void parseResultEventAttributeType(String resultEventAttributeTypeString) throws ResultEventAttributeException {
		ResultEventAttributeType[] resultEventAttributeTypes = ResultEventAttributeType.values();
		for(int i = 0; i < resultEventAttributeTypes.length; i++)
			if(resultEventAttributeTypeString.equals(resultEventAttributeTypes[i].toString())) {
				resultEventAttributeType = resultEventAttributeTypes[i];
				i = resultEventAttributeTypes.length;
			}
		
		if(resultEventAttributeType == null)
			throw new ResultEventAttributeException("\'" + resultEventAttributeTypeString + "\' is not a valid ResultEvent attribute type.");
	}
	
	public Object getResultEventAttribute(Event event) {
		if(resultEventAttributeType == null)
			return null;
		

		/*switch(resultEventAttributeType) {
		case RECOGNIZEDCLASS:
			SketchEvent sketchEvent = (SketchEvent)event;
			return sketchEvent.getRecognizedClass();
		}*/
	
		
		return null;
	}
	
}
