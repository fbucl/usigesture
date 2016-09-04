package behavior;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

@SuppressWarnings("rawtypes")
public enum ActionType {
	
	SET_TEXT(new Class[]{Label.class}, new Class[]{String.class}),
	INCREMENT(new Class[]{Label.class}, new Class[]{String.class}),
	DECREMENT(new Class[]{Label.class}, new Class[]{String.class}),
	CLEAN_AREA;
	
	
	private Class[] targetSpec;
	private Class[] argumentSpec; 
	
	ActionType(Class[] targetSpec, Class[] argumentSpec) {
		this.targetSpec = targetSpec;
		this.argumentSpec = argumentSpec;
	}
	
	ActionType(Class[] targetSpec) {
		this(targetSpec, null);
	}
	
	ActionType() {
		this(null, null);
	}
	
	public Class[] getTargetSpec() {
		return targetSpec;
	}
	
	public Class[] getArgumentSpec() {
		return argumentSpec;
	}
}
