package behavior;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;

@SuppressWarnings("rawtypes")
public enum EventType {
	
	ON_SELECTION(SWT.Selection, new Class[]{Button.class}),
	ON_STROKE_FINISHED(1000);
	
	private int swtEvent;
	private Class[] sourceSpec;
	
	EventType(int swtEvent, Class[] sourceSpec) {
		this.swtEvent = swtEvent;
		this.sourceSpec = sourceSpec;
	}
	
	EventType(int swtEvent) {
		this(swtEvent, null);
	}
	
	public int getSwtEvent() {
		return swtEvent;
	}
	
	public Class[] getSourceSpec() {
		return sourceSpec;
	}
}
