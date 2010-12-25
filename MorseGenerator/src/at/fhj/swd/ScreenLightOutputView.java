package at.fhj.swd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ScreenLightOutputView extends View implements OnClickListener{

	protected String morsecode, inputtext, currText, currMorse;
	protected int blackOrWhite;
	
	public ScreenLightOutputView(Context context, String morsecode1, String inputtext1) {
		super(context);
		this.morsecode = morsecode1;
		this.inputtext = inputtext1;
		this.currText = "";
		this.currMorse = "";
		blackOrWhite = Color.BLACK;
		this.setOnClickListener(this);

		new Thread(new Runnable() {
			public void run() {
				Log.i("LightDisView-Thread","Thread started, in: " + inputtext);

        		// Short wait before start 
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
					if (morsecode.substring(i,i+1).equals("·"))
					{
						blackOrWhite = Color.WHITE;
						postInvalidate();
						try {
							Thread.sleep(150);
							blackOrWhite = Color.BLACK;
							postInvalidate();  // redraw gui (from other thread!)
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (morsecode.substring(i,i+1).equals("-"))
					{
						blackOrWhite = Color.WHITE;
						postInvalidate();
						try {
							Thread.sleep(800);
							blackOrWhite = Color.BLACK;
							postInvalidate();
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
		        	if (morsecode.substring(i,i+1).equals(" "))  
		        	{
		        		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		        		blackOrWhite = Color.BLACK;
		        		if (currText.length() < inputtext.length()) {
		        			currText = currText + inputtext.substring(currChar,currChar+1);
		        			currChar++; }
		        		postInvalidate();
		        	}
				}
			}
		}).start();

	}
	
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

	public void onClick(View v) {
		this.setVisibility(GONE);	
	}
}
