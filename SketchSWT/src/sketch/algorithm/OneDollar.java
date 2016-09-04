package sketch.algorithm;

import java.util.List;

import gesture.Gesture;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dataset.DataSet;

import sketch.SketchSpace;
import sketch.exception.SketchException;

public class OneDollar extends Algorithm {
	
	private algorithm.oneDollar.Recognizer recognizer;
	
	private int knn = 1;
	
	public OneDollar(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		for(Control control: parent.getChildren())
			if(control instanceof OneDollar && control != this)
				throw new SketchException("Levenshtein Algorithm already exists in the Sketch Context.");
		
		recognizer = new algorithm.oneDollar.Recognizer();
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void train(DataSet dataSet) {
		for(Gesture gesture: dataSet)
			recognizer.addTemplate(gesture);
	}
	
	public void train(List<DataSet> dataSetList) {
		for(DataSet dataSet: dataSetList)
			for(Gesture gesture: dataSet)
				recognizer.addTemplate(gesture);
	}
	
	public String recognize(Gesture gesture) {
		if(knn == 0)
			return null;
		if(knn == 1)
			return recognizer.recognize(gesture);
		else
			return recognizer.recognizeKnn(gesture, knn);
	}

	public int getKnn() {
		return knn;
	}

	public void setKnn(int knn) {
		this.knn = knn;
	}
	
}
