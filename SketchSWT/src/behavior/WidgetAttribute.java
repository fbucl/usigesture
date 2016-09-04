package behavior;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;

import sketch.SketchArea;

import behavior.exception.RuleEventException;
import behavior.exception.WidgetAttributeException;

public class WidgetAttribute {

	private Rule parentRule;
	private String widgetAttribute;
	private Widget widget;
	private WidgetAttributeType widgetAttributeType;
	
	public WidgetAttribute(Rule parentRule, String widgetAttribute) throws WidgetAttributeException {
		this.parentRule = parentRule;
		this.widgetAttribute = widgetAttribute;
		
		this.parse();
	}
	
	public String toString() {
		return widgetAttribute;
	}
	
	private void parse() throws WidgetAttributeException {
		if(widgetAttribute.equals(""))
			return;
		//set null
		
		
		//Check if widget attribute start with '$'
		if(!widgetAttribute.startsWith("$"))
			throw new WidgetAttributeException("Widget attribute \'" + widgetAttribute + "\' must be called on a widget, starting by \'$\' character.");
		
		//Parse widget name
		int firstDotIndex = widgetAttribute.indexOf('.');
		String widgetString = firstDotIndex==-1?widgetAttribute.substring(1, widgetAttribute.length()):widgetAttribute.substring(1, firstDotIndex);
		parseWidget(widgetString);
		
		if(firstDotIndex == -1)
			throw new WidgetAttributeException("No attribute specified on widget \'" + widgetString + "\'.");
		
		String widgetAttributeTypeString = widgetAttribute.substring(firstDotIndex + 1, widgetAttribute.length());
		parseWidgetAttributeType(widgetAttributeTypeString);
	}
	
	private void parseWidget(String widgetString) throws WidgetAttributeException {
		widget = (Widget)XWT.findElementByName(parentRule, widgetString);
		if(widget == null)
			throw new WidgetAttributeException("\'" + widgetString + "\' is not a valid widget name or is not in the scope of the listener." );
	}
	
	private void parseWidgetAttributeType(String widgetAttributeTypeString) throws WidgetAttributeException {
		WidgetAttributeType[] widgetAttributeTypes = WidgetAttributeType.values();
		for(int i = 0; i < widgetAttributeTypes.length; i++)
			if(widgetAttributeTypeString.equals(widgetAttributeTypes[i].toString())) {
				widgetAttributeType = widgetAttributeTypes[i];
				i = widgetAttributeTypes.length;
			}
		
		if(widgetAttributeType == null)
			throw new WidgetAttributeException("\'" + widgetAttributeTypeString + "\' is not a valid widget attribute type.");
		
		if(widgetAttributeType.getTargetSpec() != null) {
			boolean containsClass = false;
			for(Class c: widgetAttributeType.getTargetSpec())
				containsClass = containsClass || widget.getClass()==c; 
			if(!containsClass)
				throw new WidgetAttributeException("Class type \'" + widget.getClass() + "\' of widget not supported for attribute widget type \'" + widgetAttributeType + "\'.");
		}
	}
	
	public Object getWidgetAttribute() {
		if(widget == null)
			return null;
		if(widgetAttributeType == null)
			return null;
		
		if(widget instanceof Button) {
			Button button = (Button)widget;
			switch(widgetAttributeType) {
			case TEXT:
				return button.getText();
			}
		}
		
		if(widget instanceof SketchArea) {
			SketchArea sketchArea = (SketchArea)widget;
			switch(widgetAttributeType) {
			case RECOGNITION_RESULT:
				return sketchArea.recognitionResult();
			}
		}
		
		return null;
	}
	
	public WidgetAttributeType getWidgetAttributeType() {
		return widgetAttributeType;
	}
	
}
