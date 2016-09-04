package sketch;

import gesture.Gesture;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import sketch.algorithm.Algorithm;
import sketch.dataset.DataSet;
import sketch.exception.SketchException;


public class SketchContext extends Composite {
	
	private List<Algorithm> algorithms;
	private List<DataSet> dataSets;
	
	public SketchContext(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		this.setVisible(false);
				
		if(!(parent instanceof SketchSpace))
			throw new SketchException("Sketch Context must belong to Sketch Space.");
		
		algorithms = new ArrayList<Algorithm>();
		dataSets = new ArrayList<DataSet>();
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void addAlgorithm(Algorithm algorithm) {
		algorithms.add(algorithm);
	}
	
	public void addDataSet(DataSet dataSet) {
		dataSets.add(dataSet);
	}
	
	public void trainAlgorithm() throws SketchException {
		if(algorithms.size() == 0)
			throw new SketchException("No algorithm specified for Sketch Context \'" + this.getData("name") + "\'");
		if(dataSets.size() == 0)
			throw new SketchException("No dataset specified for Sketch Context \'" + this.getData("name") + "\'");
		
		algorithms.get(0).train(dataSets.get(0).dataSet());
	}
	
	public String getGestureClass(Gesture gesture) {
		return algorithms.get(0).recognize(gesture);
	}

}
