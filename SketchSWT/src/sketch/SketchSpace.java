package sketch;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import sketch.exception.SketchException;

import behavior.exception.BehaviorException;

public class SketchSpace extends Composite {
	
	public SketchSpace(Composite parent, int style) throws SketchException {
		super(parent, style);
		
		this.setVisible(false);
		
		if(!(parent instanceof Shell))
			throw new SketchException("SketchSpace must belong to the root Shell.");
		
		for(Control control: parent.getChildren())
			if(control instanceof SketchSpace && control != this)
				throw new SketchException("SketchSpace already exists.");
		
	}
	
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}
	
	public void setBounds(Rectangle rect) {
		super.setBounds(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
	}

}
