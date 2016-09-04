package data;

import gesture.Dot;
import gesture.Gesture;
import gesture.Stroke;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/*
 * Classe permettant de parser un fichier texte au format adequat et de le transformer
 * en ensemble de Dot
 */
public class RecordParser {
	
	Gesture gesture;
	
	public RecordParser(String pathFile){
		try{
			gesture = new Gesture();
			
			BufferedReader br = new BufferedReader(new FileReader(pathFile));
			
			String tmp = br.readLine();
			while(tmp != null){
				Dot dot = new Dot(tmp);
				if(dot.hole)
					gesture.add(new Stroke());
				else
					gesture.add(dot);
				tmp = br.readLine();
			}
		}
		catch(IOException ioe){System.out.println("Impossible de parser le fichier "+pathFile);System.out.println(ioe);}
	}
	
	public Gesture parse(){
		return gesture;
	}
	
	public String toString(){
		String toReturn = "";
		Iterator<Dot> it = gesture.getDotRepresentation(true).iterator();
		while(it.hasNext()){
			toReturn += it.next() + "\n";
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		System.out.println(new RecordParser("records\\1\\i\\i1.txt"));
	}
	
}
