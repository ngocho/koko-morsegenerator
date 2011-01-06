package at.fhj.swd;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Used in ScreenLightOuptutAct
 * Starts a thread which changes the screen between black and white - according to the morsecode
 * @author Matthias Koinegg
 */
public class ScreenLightOutputView extends View{

	protected String morsecode, inputtext, currText, currMorse;
	protected int blackOrWhite;
	protected DisOutRunnable outRunnable;
	Thread outThread;
	int dit, dah; // dit = "."    dah = "-"    see http://www.teachersparadise.com/ency/de/wikipedia/m/mo/morsecode.html

	public ScreenLightOutputView(Context context, String morsecode1, String inputtext1) 
	{
		super(context);
		this.morsecode = morsecode1;
		this.inputtext = inputtext1;
		this.currText = "";
		this.currMorse = "";
		blackOrWhite = Color.BLACK;
		
	    // Restore preferences
	    SharedPreferences settings =context.getSharedPreferences("MORSE", 0);
	    dit =  settings.getInt("dit", 0);
	    dah =  settings.getInt("dah", 0);

		// Start output immediately 
		if (outThread== null)
			outThread = new Thread( new DisOutRunnable() );
		if (!outThread.isAlive())
			outThread.start();
	}
	
	/**
	 * Redrawing every time when postInvalidate() in the Thread is called
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		// make the entire canvas white or black
		paint.setColor(blackOrWhite);
		canvas.drawPaint(paint);

		// write the current letter
		paint.setColor(Color.RED);
		paint.setTextSize(25);
		canvas.drawText(currText, 10, 75, paint);
		
		// write the current morsecode
		canvas.drawText(currMorse, 10, 175, paint);
	}

	protected class DisOutRunnable implements Runnable 
	{
		/**
		 * Does all the Work: Screen blinks according to the morsecode
		 */
		public void run() {
			Log.i("LightDisView-Thread","Thread started, in: " + inputtext);

    		// Short pause before start 
    		try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			// For drawing the current character on the canvas...
    		currText = inputtext.substring(0,1);
    		// To be aware of the character that is being printed
    		int currChar=1;
    		
    		// output for every morsecode-char
			for (int i=0; i<morsecode.length(); i++)
			{
				if (currMorse.length() < morsecode.length()) 
					currMorse = currMorse + morsecode.substring(i,i+1); 
				if (morsecode.substring(i,i+1).equals("·")) // dit
				{
					blackOrWhite = Color.WHITE;
					// after calling postInvalidat() onDraw(...) will be called (soon)
					postInvalidate();
					try {
						Thread.sleep(dit);
						blackOrWhite = Color.BLACK;
						postInvalidate();  // redraw gui (from other thread!)
						Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (morsecode.substring(i,i+1).equals("-")) // dah
				{
					blackOrWhite = Color.WHITE;
					postInvalidate();
					try {
						Thread.sleep(dah);
						blackOrWhite = Color.BLACK;
						postInvalidate();
						Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
	        	if (morsecode.substring(i,i+1).equals(" ")) // space zwischen zwei buchstaben
	        	{
	        		try {
						Thread.sleep(dah); //zwischen Buchstaben in einem Wort wird ein dah (=3dit) Abstand gelassen 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        		blackOrWhite = Color.BLACK;
	        		if (currText.length() < inputtext.length()) {
	        			currText = currText + inputtext.substring(currChar,currChar+1);
	        			currChar++; }
	        		postInvalidate();
				}
	        	
	        	if (morsecode.substring(i,i+1).equals("\\")) // abstand zwischen zwei wörtern "\"
	        	{
	        		blackOrWhite = Color.BLACK;
	        		postInvalidate();
	        		try {
						Thread.sleep(dit*7); //zwischen Wörtern wird eine Pause von sieben dits gemacht.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	     
			}
		}
	}
}
