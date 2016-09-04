package ui;

import java.net.URL;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.cocoa.NSAutoreleasePool;
import org.eclipse.swt.internal.cocoa.NSMutableDictionary;
import org.eclipse.swt.internal.cocoa.NSNumber;
import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.NSThread;
import org.eclipse.swt.internal.cocoa.id;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class Test {

	public static void main(String args[]) throws Exception {
		/*NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		  NSThread nsthread = NSThread.currentThread();
		  NSMutableDictionary dictionary = nsthread.threadDictionary();
		  NSString key = NSString.stringWith("SWT_NSAutoreleasePool");
		  id obj = dictionary.objectForKey(key);
		  if (obj == null) {
		          NSNumber nsnumber = NSNumber.numberWithInteger(pool.id);
		          dictionary.setObject(nsnumber, key);
		  } else {
		          pool.release();
		  }*/
		
		URL url = Test.class.getResource(Test.class.getSimpleName()
				+ IConstants.XWT_EXTENSION_SUFFIX);
		Control control = XWT.load(url);
		Shell shell = control.getShell();
		shell.layout();
		centerInDisplay(shell);
		// run events loop
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}
	}

	private static void centerInDisplay(Shell shell) {
		Rectangle displayArea = shell.getDisplay().getClientArea();
		shell.setBounds(displayArea.width / 4, displayArea.height / 4,
				displayArea.width / 2, displayArea.height / 2);
	}

}
