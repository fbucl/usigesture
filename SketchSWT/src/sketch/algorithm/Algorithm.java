package sketch.algorithm;

import gesture.Gesture;

import java.util.List;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import dataset.DataSet;

import sketch.SketchContext;
import sketch.exception.SketchException;

public class Algorithm extends Composite {

	public Algorithm(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		if(!(parent instanceof SketchContext))
			throw new SketchException("Sketch Algorithm must belong to Sketch Context.");
		
		((SketchContext)parent).addAlgorithm(this);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void train(DataSet dataSet) {
	}
	
	public void train(List<DataSet> dataSetList) {
	}
	
	public String recognize(Gesture gesture) {
		return null;
	}
	
}
