package sketch.dataset;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sketch.algorithm.Levenshtein;
import sketch.exception.SketchException;

public class ActionGestures extends DataSet {
	
	private int numberSketchers = 5;
	
	private int numberGesturesByClass = 10;
	
	public ActionGestures(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		for(Control control: parent.getChildren())
			if(control instanceof ActionGestures && control != this)
				throw new SketchException("GeometricalShapes DataSet already exists in the Sketch Context.");
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
	
	public dataset.DataSet dataSet() {
		return dataset.DataSet.getActionGestures(numberSketchers, numberGesturesByClass);
	}
	
}
