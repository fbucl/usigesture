package enums;

import org.eclipse.swt.SWT;

public enum LineStyle {
	
	LINE_DASH(SWT.LINE_DASH),
	LINE_DASHDOT(SWT.LINE_DASHDOT),
	LINE_DASHDOTDOT(SWT.LINE_DASHDOTDOT),
	LINE_DOT(SWT.LINE_DOT),
	LINE_SOLID(SWT.LINE_SOLID);
	
	private final int swtEquivalence;
	
	LineStyle(int swtEquivalence) {
		this.swtEquivalence = swtEquivalence;
	}
	
	public int swtEquivalence() {
		return swtEquivalence;
	}
	
}
