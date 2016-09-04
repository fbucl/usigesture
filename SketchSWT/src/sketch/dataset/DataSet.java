package sketch.dataset;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import sketch.SketchContext;
import sketch.exception.SketchException;

public class DataSet extends Composite {
	
	public DataSet(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		if(!(parent instanceof SketchContext))
			throw new SketchException("Sketch DataSet must belong to Sketch Context.");
		
		((SketchContext)parent).addDataSet(this);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public dataset.DataSet dataSet() {
		return null;
	}
	
}
