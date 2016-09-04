package sketch;

import java.util.ArrayList;
import java.util.List;

import data.RecordParser;
import dataset.DataSet;
import enums.LineStyle;
import gesture.Dot;
import gesture.Gesture;
import gesture.Stroke;
import behavior.EventType;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import sketch.exception.SketchException;

import algorithm.levenshtein.Levenshtein;
import algorithm.levenshtein.Utils;



public class SketchArea extends Canvas {

	//public int numberChildren = 0;
	
	public Composite parent;
	
	private boolean trained;
	
	private static int numberTrainingSketchAreas = 0;
	
	private SketchContext sketchContext;
	
	private Gesture gesture;
	
	private boolean mouseDown, mouseInBounds;
	
	private Color strokeColor;
	
	private int strokeWidth = 4;
	
	private int strokeAlpha = 255;
	
	private LineStyle strokeStyle = LineStyle.LINE_SOLID;
	
	private boolean cleanAfterEachStroke = true;
	
	private boolean recognitionAfterEachStroke = true;
	
	private String sketchContextString;
	
	private RecognitionTrigger recognitionTrigger = RecognitionTrigger.AFTER_EACH_STROKE;

	public SketchArea(Composite parent, int style)  {
		super(parent, style);
		this.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		
		this.parent = parent;
		
		gesture = new Gesture();
		
		
		strokeColor = new Color(Display.getCurrent(), 255, 0, 0);
		
		this.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	        	
	        	if(!trained)
	        		e.gc.drawText("Training...", (getBounds().width-70)/2, getBounds().height/2);
	                	
	        	e.gc.setForeground(strokeColor);
	        	e.gc.setAlpha(strokeAlpha);
	        	e.gc.setLineWidth(strokeWidth);
	        	e.gc.setLineStyle(strokeStyle.swtEquivalence());
	        	
	        	for(Stroke stroke : gesture) {
	        		if(stroke.numberDots() == 1) {
	        			Dot dot = stroke.get(0);
	        			e.gc.drawPoint(dot.x, getBounds().height-dot.y-1);
	        		}
	        		else if (stroke.numberDots() > 1) {
	        			Dot dot1 = stroke.get(0);
	        			for(int i = 1; i < stroke.numberDots(); i++) {
	        				Dot dot2 = stroke.get(i);
	        				e.gc.drawLine(dot1.x, getBounds().height-dot1.y-1, dot2.x, getBounds().height-dot2.y-1);
	        				dot1 = dot2;
	        			}
	        		}		
	        	}        	
	        }
	    });
		
		
		this.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				//System.out.println("down" + e);
				mouseDown = mouseInBounds = true;
					
				gesture.add(new Stroke());
				gesture.add(new Dot(e.x, getBounds().height-e.y-1));
			}
			
			public void mouseUp(MouseEvent e) {
				//System.out.println("up" + e);
				
				if(recognitionAfterEachStroke)
					SketchArea.this.notifyListeners(EventType.ON_STROKE_FINISHED.getSwtEvent(), new SketchEvent(gesture));
				
				mouseDown = mouseInBounds = false;
				
				if(cleanAfterEachStroke) {
					SketchArea currentSketchArea = (SketchArea)e.widget;
					currentSketchArea.cleanArea();
				}
			}
			
			public void mouseDoubleClick(MouseEvent e) {
				
			}
			
	    });
		
		
		this.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if(mouseDown) {
					if(e.x <= getBounds().width && e.y <= getBounds().height) {
						if(!mouseInBounds) {
							gesture.add(new Stroke());
							mouseInBounds = true;
						}
						SketchArea currentSketchArea = (SketchArea)e.widget;
						gesture.add(new Dot(e.x, getBounds().height-e.y-1));
						
						currentSketchArea.redraw();
						
						//System.out.println("mouse move: " + e);
					}
					else {
						mouseInBounds = false;
					}
				}
			}
		});
	}
	
	/*public void performRecognition() {
		SketchArea.this.notifyListeners(EventType.ON_RECOGNITION.getSwtEvent(), new SketchEvent(sketchContext.getGestureClass(gesture)));
	}*/
	
	public String recognitionResult() {
		return sketchContext.getGestureClass(gesture);
	}
	
	public void cleanArea() {
		gesture = new Gesture();
		redraw();
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	public boolean isCleanAfterEachStroke() {
		return cleanAfterEachStroke;
	}

	public void setCleanAfterEachStroke(boolean cleanAfterEachStroke) {
		this.cleanAfterEachStroke = cleanAfterEachStroke;
	}

	public boolean isRecognitionAfterEachStroke() {
		return recognitionAfterEachStroke;
	}

	public void setRecognitionAfterEachStroke(boolean recognitionAfterEachStroke) {
		this.recognitionAfterEachStroke = recognitionAfterEachStroke;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public LineStyle getStrokeStyle() {
		return strokeStyle;
	}

	public void setLineStyle(LineStyle strokeStyle) {
		this.strokeStyle = strokeStyle;
	}

	public int getStrokeAlpha() {
		return strokeAlpha;
	}

	public void setStrokeAlpha(int strokeAlpha) {
		this.strokeAlpha = strokeAlpha;
	}

	public String getSketchContext() {
		return sketchContextString;
	}

	public void setSketchContext(String sketchContextString) throws SketchException {
		final Shell shell = XWT.findShell(this);
		shell.setEnabled(false);
		
		this.sketchContextString = sketchContextString;
		
		Widget widget = (Widget)XWT.findElementByName(this, sketchContextString);
		if(widget == null)
			throw new SketchException("\'" + sketchContextString + "\' does not exist or does not belong to the scope of the Sketch Area.");
		if(!(widget instanceof SketchContext))
			throw new SketchException("\'" + sketchContextString + "\' is not a Sketch Context.");
		
		sketchContext = (SketchContext)widget;
		sketchContext.setData("name", sketchContextString);
		
		
		
		final Runnable training = new Runnable() {
			public void run() {
				try {
					sketchContext.trainAlgorithm();
					numberTrainingSketchAreas--;
					trained = true;
					SketchArea.this.redraw();
					if(numberTrainingSketchAreas == 0)
						shell.setEnabled(true);
				}
				catch(SketchException se) {
					se.printStackTrace();
				}
			}
		};
		
		
		//Thread thread = new Thread(training);
		//thread.start();
		numberTrainingSketchAreas++;
		shell.getDisplay().asyncExec(training);
		
	}

	public RecognitionTrigger getRecognitionTrigger() {
		return recognitionTrigger;
	}

	public void setRecognitionTrigger(RecognitionTrigger recognitionTrigger) {
		this.recognitionTrigger = recognitionTrigger;
	}
	
	

}
