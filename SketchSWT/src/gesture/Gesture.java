package gesture;

import java.util.ArrayList;
import java.util.List;

public class Gesture extends ArrayList<Stroke> {
	
	private String gestureClass;
	
	public int numberStrokes() {
		return this.size();
	}
	
	public void add(Dot dot) {
		if(this.numberStrokes()==0)
			this.add(new Stroke());
		this.get(this.numberStrokes()-1).add(dot);
	}
	
	public List<Dot> getDotRepresentation(boolean holes) {
		List<Dot> dotRepresentation = new ArrayList<Dot>();
		for(Stroke stroke: this) {
			for(Dot dot: stroke) {
				dotRepresentation.add(dot);
			}
			if(holes)
				dotRepresentation.add(new Dot(-1, -1, true));
		}
		
		return dotRepresentation;
	}

	public String getGestureClass() {
		return gestureClass;
	}

	public void setGestureClass(String gestureClass) {
		this.gestureClass = gestureClass;
	}

}
