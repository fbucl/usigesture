package algorithm.levenshtein;

import gesture.Dot;
import gesture.Gesture;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import algorithm.oneDollar.Recognizer;


/*
 * Classe definissant plusieurs methodes de transformation des gestes
 */
public class Utils {

	/*
	 * pre: -
	 * post: renvoie un string correspondant aux 8 directions de la rose des vents entre les points de gesture
	 */
	public static String transform(Gesture gesture){
		return transform(gesture, false);
	}

	/*
	 * pre: -
	 * post: renvoie un string correspondant aux 8 directions de la rose des vents entre les points de gesture
	 * 		 en tenant compte ou pas des trous
	 */
	public static String transform(Gesture gesture, boolean withHoles){
		String toReturn = "";
		
		
		Iterator<Dot> it = gesture.getDotRepresentation(withHoles).iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(withHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			if(current.x == next.x && current.y < next.y) toReturn += "0";
			if(current.x < next.x && current.y < next.y) toReturn += "1";
			if(current.x < next.x && current.y == next.y) toReturn += "2";
			if(current.x < next.x && current.y > next.y) toReturn += "3";
			if(current.x == next.x && current.y > next.y) toReturn += "4";
			if(current.x > next.x && current.y > next.y) toReturn += "5";
			if(current.x > next.x && current.y == next.y) toReturn += "6";
			if(current.x > next.x && current.y < next.y) toReturn += "7";

			current = next; 
		}
		return toReturn;
	}

	public static String transformWithProcessing(Gesture gesture, boolean resample, boolean rotate, boolean scale, boolean translate){
		return transformWithProcessing(gesture.getDotRepresentation(true), resample, rotate, scale, translate);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant aux 8 directions de la rose des vents entre les points de gesture
	 * 		 prealablement traite (reechantillonnage, rotation, redimensionnement, translation)
	 */
	public static String transformWithProcessing(List<Dot> gesture, boolean resample, boolean rotate, boolean scale, boolean translate){
		String toReturn = "";
		ArrayList<Point> alp = new ArrayList<Point>();
		Iterator<Dot> it = gesture.iterator();
		while(it.hasNext()){
			Dot dot = it.next();
			if(!dot.hole)
				alp.add(new Point(dot.x,dot.y));
		}

		if(resample)
			alp = algorithm.oneDollar.Utils.Resample(alp, algorithm.oneDollar.Recognizer.NumResamplePoints);

		if(rotate){
			double radians = algorithm.oneDollar.Utils.AngleInRadians(algorithm.oneDollar.Utils.Centroid(alp), alp.get(0), false);
			alp = algorithm.oneDollar.Utils.RotateByRadians(alp, -radians);
		}

		if(scale)
			alp = algorithm.oneDollar.Utils.ScaleTo(alp, algorithm.oneDollar.Recognizer.ResampleScale);

		if(translate)
			alp = algorithm.oneDollar.Utils.TranslateCentroidTo(alp, Recognizer.ResampleOrigin);

		/*String a = "a";
		if(a.equals("a"))
			throw new IllegalArgumentException(""+alp.size());*/
		if(alp.size() == 0)
			return "";
			
		Iterator<Point> it2 = alp.iterator();

		Point current = it2.next();

		while(it2.hasNext()){
			Point next = it2.next();
			if(current.x == next.x && current.y < next.y) toReturn += "0";
			if(current.x < next.x && current.y < next.y) toReturn += "1";
			if(current.x < next.x && current.y == next.y) toReturn += "2";
			if(current.x < next.x && current.y > next.y) toReturn += "3";
			if(current.x == next.x && current.y > next.y) toReturn += "4";
			if(current.x > next.x && current.y > next.y) toReturn += "5";
			if(current.x > next.x && current.y == next.y) toReturn += "6";
			if(current.x > next.x && current.y < next.y) toReturn += "7";


			current = next; 
		}
		return toReturn;
	}

	public static String transformWithResampleAndHoles(Gesture gesture) {
		return transformWithResampleAndHoles(gesture.getDotRepresentation(true));
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant aux 8 directions de la rose des vents entre les points de gesture
	 * 		 prealablement reechantillonne et en tenant compte des trous 
	 */
	public static String transformWithResampleAndHoles(List<Dot> gesture){
		String toReturn = "";
		ArrayList<Point> alp = new ArrayList<Point>();
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot currentDot = it.next();
		if(!currentDot.hole)
			alp.add(new Point(currentDot.x,currentDot.y));
		if(currentDot.hole && it.hasNext()){
			currentDot = it.next();
			alp.add(new Point(currentDot.x,currentDot.y));
		}
		while(it.hasNext()){
			Dot nextDot = it.next();
			if(!nextDot.hole)
				alp.add(new Point(nextDot.x,nextDot.y));
			else{
				if(algorithm.oneDollar.Recognizer.NumResamplePoints*alp.size()/gesture.size() > 1){
					alp = algorithm.oneDollar.Utils.Resample(alp, algorithm.oneDollar.Recognizer.NumResamplePoints*alp.size()/gesture.size());

					Iterator<Point> it2 = alp.iterator();
					Point current = it2.next();

					while(it2.hasNext()){
						Point next = it2.next();
						if(current.x == next.x && current.y < next.y) toReturn += "0";
						if(current.x < next.x && current.y < next.y) toReturn += "1";
						if(current.x < next.x && current.y == next.y) toReturn += "2";
						if(current.x < next.x && current.y > next.y) toReturn += "3";
						if(current.x == next.x && current.y > next.y) toReturn += "4";
						if(current.x > next.x && current.y > next.y) toReturn += "5";
						if(current.x > next.x && current.y == next.y) toReturn += "6";
						if(current.x > next.x && current.y < next.y) toReturn += "7";

						current = next; 
					}
				}
				
				alp = new ArrayList<Point>();

				if(it.hasNext()){
					nextDot = it.next();
					int squareDist = (int)Math.sqrt((nextDot.x-currentDot.x)*(nextDot.x-currentDot.x) + (nextDot.y-currentDot.y)*(nextDot.y-currentDot.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			currentDot = nextDot;
		}
		return toReturn;
	}

	public static String transformPressure(Gesture gesture, boolean whithHoles){
		return transformPressure(gesture.getDotRepresentation(true), whithHoles);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant a la pression normalisee en 8 morceaux de gesture
	 */
	public static String transformPressure(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){

			if(current.hole && it.hasNext()){
				Dot next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
				current = next;
			}

			toReturn += ""+(int)(current.pressure/128);
			current = it.next(); 
		}
		return toReturn;
	}

	
	public static String transformPressureDifference(Gesture gesture, boolean whithHoles){
		return transformPressureDifference(gesture.getDotRepresentation(true), whithHoles);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant a la difference de pressions entre chaque point 
	 * 		 de gesture (0 pour une augmentation, 1 pour une egalite, 2 pour une diminution)
	 */
	public static String transformPressureDifference(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			if(current.pressure < next.pressure) toReturn += "0";
			if(current.pressure == next.pressure) toReturn += "1";
			if(current.pressure > next.pressure) toReturn += "2";

			current = next; 
		}
		return toReturn;
	}

	public static String transformAngle(Gesture gesture, boolean whithHoles){
		return transformAngle(gesture.getDotRepresentation(true), whithHoles);
	}

	/*
	 * pre: -
	 * post: renvoie un string correspondant a l'angle normalise en 8 morceaux de gesture
	 */
	public static String transformAngle(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			toReturn += next.angle*8/90;
			current = next; 
		}
		return toReturn;
	}

	
	public static String transformAngleDifference(Gesture gesture, boolean whithHoles){
		return transformAngleDifference(gesture.getDotRepresentation(true), whithHoles);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant a la difference d'angles entre chaque point 
	 * 		 de gesture (0 pour une augmentation, 1 pour une egalite, 2 pour une diminution)
	 */
	public static String transformAngleDifference(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			if(current.angle < next.angle) toReturn += "0";
			if(current.angle == next.angle) toReturn += "1";
			if(current.angle > next.angle) toReturn += "2";

			current = next; 
		}
		return toReturn;
	}

	public static String transformOrientation(Gesture gesture, boolean whithHoles){
		return transformOrientation(gesture.getDotRepresentation(true), whithHoles);
	}

	/*
	 * pre: -
	 * post: renvoie un string correspondant a l'orientation normalisee en 8 morceaux de gesture
	 */
	public static String transformOrientation(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "8";
				}
			}
			toReturn += next.orientation*8/360;
			current = next; 
		}
		return toReturn;
	}

	public static String transformOrientationDifference(Gesture gesture, boolean whithHoles){
		return transformOrientationDifference(gesture.getDotRepresentation(true), whithHoles);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant a la difference d'orientations entre chaque point 
	 * 		 de gesture (0 pour une augmentation, 1 pour une egalite, 2 pour une diminution)
	 */
	public static String transformOrientationDifference(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}
			if(current.orientation < next.orientation) toReturn += "0";
			if(current.orientation == next.orientation) toReturn += "1";
			if(current.orientation > next.orientation) toReturn += "2";

			current = next; 
		}
		return toReturn;
	}

	public static String transformDirectionAndPressure(Gesture gesture, boolean whithHoles){
		return transformDirectionAndPressure(gesture.getDotRepresentation(true), whithHoles);
	}
	
	/*
	 * pre: -
	 * post: renvoie un string correspondant a une combinaison entre les 8 directions de la rose des vents 
	 * 		 ainsi qu'aux differences de pressions entre les points de gesture
	 */
	public static String transformDirectionAndPressure(List<Dot> gesture, boolean whithHoles){
		String toReturn = "";
		Iterator<Dot> it = gesture.iterator();
		if(!it.hasNext())
			return "";
		Dot current = it.next();
		if(current.hole && it.hasNext())
			current = it.next();
		while(it.hasNext()){
			Dot next = it.next();
			if(next.hole && it.hasNext()){
				next = it.next();
				if(whithHoles){
					int squareDist = (int)Math.sqrt((next.x-current.x)*(next.x-current.x) + (next.y-current.y)*(next.y-current.y));
					for(int i = 0; i < squareDist/100; i++)
						toReturn += "x";
				}
			}

			if(current.x == next.x && current.y < next.y && current.pressure < next.pressure) toReturn += "a";
			if(current.x < next.x && current.y < next.y && current.pressure < next.pressure) toReturn += "b";
			if(current.x < next.x && current.y == next.y && current.pressure < next.pressure) toReturn += "c";
			if(current.x < next.x && current.y > next.y && current.pressure < next.pressure) toReturn += "d";
			if(current.x == next.x && current.y > next.y && current.pressure < next.pressure) toReturn += "e";
			if(current.x > next.x && current.y > next.y && current.pressure < next.pressure) toReturn += "f";
			if(current.x > next.x && current.y == next.y && current.pressure < next.pressure) toReturn += "g";
			if(current.x > next.x && current.y < next.y && current.pressure < next.pressure) toReturn += "h";

			if(current.x == next.x && current.y < next.y && current.pressure == next.pressure) toReturn += "i";
			if(current.x < next.x && current.y < next.y && current.pressure == next.pressure) toReturn += "j";
			if(current.x < next.x && current.y == next.y && current.pressure == next.pressure) toReturn += "k";
			if(current.x < next.x && current.y > next.y && current.pressure == next.pressure) toReturn += "l";
			if(current.x == next.x && current.y > next.y && current.pressure == next.pressure) toReturn += "m";
			if(current.x > next.x && current.y > next.y && current.pressure == next.pressure) toReturn += "n";
			if(current.x > next.x && current.y == next.y && current.pressure == next.pressure) toReturn += "o";
			if(current.x > next.x && current.y < next.y && current.pressure == next.pressure) toReturn += "p";

			if(current.x == next.x && current.y < next.y && current.pressure > next.pressure) toReturn += "q";
			if(current.x < next.x && current.y < next.y && current.pressure > next.pressure) toReturn += "r";
			if(current.x < next.x && current.y == next.y && current.pressure > next.pressure) toReturn += "s";
			if(current.x < next.x && current.y > next.y && current.pressure > next.pressure) toReturn += "t";
			if(current.x == next.x && current.y > next.y && current.pressure > next.pressure) toReturn += "u";
			if(current.x > next.x && current.y > next.y && current.pressure > next.pressure) toReturn += "v";
			if(current.x > next.x && current.y == next.y && current.pressure > next.pressure) toReturn += "w";
			if(current.x > next.x && current.y < next.y && current.pressure > next.pressure) toReturn += "y";


			current = next; 
		}
		return toReturn;
	}

}
