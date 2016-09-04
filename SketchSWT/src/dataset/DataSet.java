package dataset;

import gesture.Gesture;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import algorithm.levenshtein.Utils;
import data.RecordParser;

public class DataSet extends ArrayList<Gesture> {
	
	
	public static DataSet getDigits(int numberSketchers, int numberGesturesByClass) {
		
		DataSet digits = new DataSet();
		
		Map<String, String> classTranslations = new Hashtable<String, String>();
		List<String> classes = new ArrayList<String>();
		classes.add("zero");
		classTranslations.put("zero", "0");
		classes.add("one");
		classTranslations.put("one", "1");
		classes.add("two");
		classTranslations.put("two", "2");
		classes.add("three");
		classTranslations.put("three", "3");
		classes.add("four");
		classTranslations.put("four", "4");
		classes.add("five");
		classTranslations.put("five", "5");
		classes.add("six");
		classTranslations.put("six", "6");
		classes.add("seven");
		classTranslations.put("seven", "7");
		classes.add("eight");
		classTranslations.put("eight", "8");
		classes.add("nine");
		classTranslations.put("nine", "9");
		
		for(int i = 1; i <= numberSketchers; i++) {
			for(int j = 0; j < classes.size(); j++) {
				for(int k = 1; k <= numberGesturesByClass; k++) {
					Gesture gesture = new RecordParser("records/"+i+"/"+classes.get(j)+"/"+classes.get(j)+k+".txt").parse();
					gesture.setGestureClass(classTranslations.get(classes.get(j)));
					digits.add(gesture);
				}
			}
		}
	
		return digits;
	}


	public static DataSet getLetters(int numberSketchers, int numberGesturesByClass) {
	
		DataSet letters = new DataSet();
	
		List<String> classes = new ArrayList<String>();
		classes.add("a");
		classes.add("b");
		classes.add("c");
		classes.add("d");
		classes.add("e");
		classes.add("f");
		classes.add("g");
		classes.add("h");
		classes.add("i");
		classes.add("j");
		classes.add("k");
		classes.add("l");
		classes.add("m");
		classes.add("n");
		classes.add("o");
		classes.add("p");
		classes.add("q");
		classes.add("r");
		classes.add("s");
		classes.add("t");
		classes.add("u");
		classes.add("v");
		classes.add("w");
		classes.add("x");
		classes.add("y");
		classes.add("z");
		
		for(int i = 1; i <= numberSketchers; i++) {
			for(int j = 0; j < classes.size(); j++) {
				for(int k = 1; k <= numberGesturesByClass; k++) {
					Gesture gesture = new RecordParser("records/"+i+"/"+classes.get(j)+"/"+classes.get(j)+k+".txt").parse();
					gesture.setGestureClass(classes.get(j));
					letters.add(gesture);
				}
			}
		}

		return letters;
	}
	
public static DataSet getGeometricalShapes(int numberSketchers, int numberGesturesByClass) {
		
		DataSet geometricalShapes = new DataSet();

		List<String> classes = new ArrayList<String>();
		classes.add("triangle");
		classes.add("square");
		classes.add("rectangle");
		classes.add("circle");
		classes.add("pentagon");
		classes.add("parallelogram");
		classes.add("doubleSquare");
		classes.add("doubleCircle");
		
		for(int i = 1; i <= numberSketchers; i++) {
			for(int j = 0; j < classes.size(); j++) {
				for(int k = 1; k <= numberGesturesByClass; k++) {
					Gesture gesture = new RecordParser("records/"+i+"/"+classes.get(j)+"/"+classes.get(j)+k+".txt").parse();
					gesture.setGestureClass(classes.get(j));
					geometricalShapes.add(gesture);
				}
			}
		}
	
		return geometricalShapes;
	}


	public static DataSet getActionGestures(int numberSketchers, int numberGesturesByClass) {
		
		DataSet actionGestures = new DataSet();
	
		List<String> classes = new ArrayList<String>();
		classes.add("rightArrow");
		classes.add("leftArrow");
		classes.add("downArrow");
		classes.add("upArrow");
		classes.add("rightDownArrow");
		classes.add("leftDownArrow");
		classes.add("rightUpArrow");
		classes.add("leftUpArrow");
		classes.add("rightArrowDownArrow");
		classes.add("leftArrowDownArrow");
		classes.add("rightArrowUpArrow");
		classes.add("leftArrowUpArrow");
		classes.add("downArrowRightArrow");
		classes.add("downArrowLeftArrow");
		classes.add("upArrowRightArrow");
		classes.add("upArrowLeftArrow");
		
		for(int i = 1; i <= numberSketchers; i++) {
			for(int j = 0; j < classes.size(); j++) {
				for(int k = 1; k <= numberGesturesByClass; k++) {
					Gesture gesture = new RecordParser("records/"+i+"/"+classes.get(j)+"/"+classes.get(j)+k+".txt").parse();
					gesture.setGestureClass(classes.get(j));
					actionGestures.add(gesture);
				}
			}
		}
	
		return actionGestures;
	}
	
	public static DataSet getGestures(int numberSketchers, int numberGesturesByClass) {
		try {new RecordParser("records/0/a/a1.txt"); }
		catch(Exception e) {}
		return null;
	}
	
	public static void main(String[] args) {
		DataSet.getGestures(0, 0);
	}
	

}


