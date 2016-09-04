package behavior;

import sketch.SketchArea;

@SuppressWarnings("rawtypes")
public enum WidgetAttributeType {
	TEXT,
	RECOGNITION_RESULT(new Class[]{SketchArea.class}, String.class);
	
	private Class[] targetSpec;
	private Class returnSpec;
	
	WidgetAttributeType(Class[] targetSpec, Class returnSpec) {
		this.targetSpec = targetSpec;
		this.returnSpec = returnSpec;
	}
	
	WidgetAttributeType(Class[] targetSpec) {
		this(targetSpec, null);
	}
	
	WidgetAttributeType(Class returnSpec) {
		this(null, returnSpec);
	}
	
	WidgetAttributeType() {
		this(null, null);
	}
	
	public Class[] getTargetSpec() {
		return targetSpec;
	}
	
	public Class returnSpec() {
		return returnSpec;
	}
}
