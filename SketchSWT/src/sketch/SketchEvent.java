package sketch;

import gesture.Gesture;

import org.eclipse.swt.widgets.Event;

public class SketchEvent extends Event {

	private Gesture gesture;
	
	public SketchEvent(Gesture gesture) {
		super();
		this.gesture = gesture;
	}
	
	public Gesture getGesture() {
		return gesture;
	}
	
}
