package sketch.dataset;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sketch.algorithm.Levenshtein;
import sketch.exception.SketchException;

public class CustomUI extends DataSet {
	
	private int numberSketchers = 5;
	
	private int numberGesturesByClass = 10;
	
	private String gestureClasses;
	
	public CustomUI(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		for(Control control: parent.getChildren())
			if(control instanceof CustomUI && control != this)
				throw new SketchException("Digits DataSet already exists in the Sketch Context.");
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}

	public int getNumberSketchers() {
		return numberSketchers;
	}

	public void setNumberSketchers(int numberSketchers) {
		this.numberSketchers = numberSketchers;
	}

	public int getNumberGesturesByClass() {
		return numberGesturesByClass;
	}

	public void setNumberGesturesByClass(int numberGesturesByClass) {
		this.numberGesturesByClass = numberGesturesByClass;
	}
	
	public String getGestureClasses() {
		return gestureClasses;
	}

	public void setGestureClasses(String gestureClasses) {
		this.gestureClasses = gestureClasses;
	}

	public dataset.DataSet dataSet() {
		return null;
	}
	
}
