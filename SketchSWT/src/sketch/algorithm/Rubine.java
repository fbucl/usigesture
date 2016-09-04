package sketch.algorithm;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import gesture.Gesture;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dataset.DataSet;

import sketch.SketchSpace;
import sketch.exception.SketchException;

public class Rubine extends Algorithm {
	
	private algorithm.rubine.Classifier classifier;
	
	private boolean resampling;
	
	private boolean rotation;
	
	private boolean rescaling;
	
	private boolean translation;
	
	public Rubine(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		for(Control control: parent.getChildren())
			if(control instanceof Rubine && control != this)
				throw new SketchException("Rubine Algorithm already exists in the Sketch Context.");
		
		classifier = new algorithm.rubine.Classifier();
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void train(DataSet dataSet) {
		Map<String, algorithm.rubine.GestureClass> gestureClasses = new Hashtable<String, algorithm.rubine.GestureClass>();
		for(Gesture gesture: dataSet) {
			String c = gesture.getGestureClass();
			algorithm.rubine.GestureClass gestureClass = gestureClasses.get(c);
			if(gestureClass == null) {
				gestureClass = new algorithm.rubine.GestureClass(c);
				gestureClasses.put(c, gestureClass);
			}
			
			algorithm.rubine.Gesture rubGesture = new algorithm.rubine.Gesture();
			rubGesture.addGesture(gesture, false, false, false, false);
			
			gestureClass.addExample(rubGesture);
		}
		
		for(algorithm.rubine.GestureClass gestureClass: gestureClasses.values()) {
			classifier.addClass(gestureClass);
		}
		classifier.compile();
			
	}
	
	public void train(List<DataSet> dataSetList) {
		Map<String, algorithm.rubine.GestureClass> gestureClasses = new Hashtable<String, algorithm.rubine.GestureClass>();
		for(DataSet dataSet: dataSetList)
			for(Gesture gesture: dataSet) {
				String c = gesture.getGestureClass();
				algorithm.rubine.GestureClass gestureClass = gestureClasses.get(c);
				if(gestureClass == null) {
					gestureClass = new algorithm.rubine.GestureClass(c);
					gestureClasses.put(c, gestureClass);
				}
				
				algorithm.rubine.Gesture rubGesture = new algorithm.rubine.Gesture();
				rubGesture.addGesture(gesture, false, false, false, false);
				
				gestureClass.addExample(rubGesture);
			}
		
		for(algorithm.rubine.GestureClass gestureClass: gestureClasses.values()) {
			classifier.addClass(gestureClass);
		}
		classifier.compile();
				
	}
	
	public String recognize(Gesture gesture) {
		algorithm.rubine.Gesture rubGesture = new algorithm.rubine.Gesture();
		rubGesture.addGesture(gesture, false, false, false, false);
		return classifier.classify(rubGesture).getName();
	}

	public boolean isResampling() {
		return resampling;
	}

	public void setResampling(boolean resampling) {
		this.resampling = resampling;
	}

	public boolean isRotation() {
		return rotation;
	}

	public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	public boolean isRescaling() {
		return rescaling;
	}

	public void setRescaling(boolean rescaling) {
		this.rescaling = rescaling;
	}

	public boolean isTranslation() {
		return translation;
	}

	public void setTranslation(boolean translation) {
		this.translation = translation;
	}
	
}
