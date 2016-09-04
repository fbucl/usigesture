package sketch.algorithm;

import java.util.List;

import gesture.Gesture;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import algorithm.levenshtein.Utils;
import algorithm.stochasticLevenshtein.ConditionalRecognizer;

import dataset.DataSet;

import sketch.SketchSpace;
import sketch.exception.SketchException;

public class StochasticLevenshtein extends Algorithm {
	
	private ConditionalRecognizer conditionalRecognizer;
	
	private int normalization = 0;
	
	private int knn = 1;
	
	private LevenshteinFeature feature = LevenshteinFeature.DIRECTION;
	
	private boolean resampling = false;
	
	private boolean rotation = false;
	
	private boolean rescaling = false;
	
	private boolean translation = false;
	
	public StochasticLevenshtein(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		for(Control control: parent.getChildren())
			if(control instanceof StochasticLevenshtein && control != this)
				throw new SketchException("Levenshtein Algorithm already exists in the Sketch Context.");
		
		String[] alphabet = {"0","1","2","3","4","5","6","7"};
		conditionalRecognizer = new ConditionalRecognizer(alphabet);
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void train(DataSet dataSet) {
		for(Gesture gesture: dataSet) {
			String template = null;
			switch(feature) {
			case DIRECTION: 
				template = Utils.transform(gesture);
				break;
			case DIRECTION_MULTISTROKE:
				template = Utils.transform(gesture, true);
				break;
			case DIRECTION_MULTISTROKE_RESAMPLING:
				template = Utils.transformWithResampleAndHoles(gesture);
				break;
			case DIRECTION_$1PREPROCESSING:
				template = Utils.transformWithProcessing(gesture, resampling, rotation, rescaling, translation);
				break;
			case PRESSURE:
				template = Utils.transformPressure(gesture, false);
				break;
			case DIRECTION_AND_PRESSURE:
				template = Utils.transformDirectionAndPressure(gesture, false);
				break;
			default:
				template = null;
				break;
			}
			conditionalRecognizer.addTemplate(gesture.getGestureClass(), template);
		}
		conditionalRecognizer.compile();
	}
	
	public void train(List<DataSet> dataSetList) {
		for(DataSet dataSet: dataSetList)
			train(dataSet);
		conditionalRecognizer.compile();
	}
	
	public String recognize(Gesture gesture) {
		String transformedGesture = null;
		switch(feature) {
		case DIRECTION: 
			transformedGesture = Utils.transform(gesture);
			break;
		case DIRECTION_MULTISTROKE:
			transformedGesture = Utils.transform(gesture, true);
			break;
		case DIRECTION_MULTISTROKE_RESAMPLING:
			transformedGesture = Utils.transformWithResampleAndHoles(gesture);
			break;
		case DIRECTION_$1PREPROCESSING:
			transformedGesture = Utils.transformWithProcessing(gesture, resampling, rotation, rescaling, translation);
			break;
		case PRESSURE:
			transformedGesture = Utils.transformPressure(gesture, false);
			break;
		case DIRECTION_AND_PRESSURE:
			transformedGesture = Utils.transformDirectionAndPressure(gesture, false);
			break;
		default:
			transformedGesture = null;
			break;
		}
		
		return conditionalRecognizer.recognize(transformedGesture, normalization, knn);
	}
	
	public int getNormalisation() {
		return normalization;
	}

	public void setNormalisation(int normalisation) throws SketchException {
		if(normalisation < 0 || normalisation > 3)
			throw new SketchException("Normalisation must be between 0 and 3.");
		this.normalization = normalisation;
	}

	public int getKnn() {
		return knn;
	}

	public void setKnn(int knn) throws SketchException {
		if(knn < 1)
			throw new SketchException("Knn must be equal or greater to 1.");
		this.knn = knn;
	}

	public LevenshteinFeature getFeature() {
		return feature;
	}

	public void setFeature(LevenshteinFeature feature) {
		this.feature = feature;
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
