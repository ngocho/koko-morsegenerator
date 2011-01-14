package at.fhj.swd;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FlashOutputAct extends Activity implements OnClickListener{

	protected String morsecode;
	protected String inputtext;
	protected String currMorse;
	
	//Create camera and parameter objects
	protected Camera mCamera;
	protected Camera.Parameters mParameters;
	//private boolean mbTorchEnabled = false;
	protected Thread outThread;
	int dit, dah; // dit = "."    dah = "-"    see http://www.teachersparadise.com/ency/de/wikipedia/m/mo/morsecode.html
	boolean notavailable = false;
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_flash);
        
        // Set listenter for buttons
        ((Button) findViewById(R.id.bt_flash_start)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_flash_back)).setOnClickListener(this);
        
        morsecode = getIntent().getExtras().getString("morsecode");
        inputtext = getIntent().getExtras().getString("inputtext");        
        ((TextView) findViewById(R.id.tv_flash_morsecode)).setText(morsecode);
        ((TextView) findViewById(R.id.tv_flash_inputtext)).setText(inputtext);       
        
        // Restore preferences
        SharedPreferences settings =this.getSharedPreferences("MORSE", 0);
        dit =  settings.getInt("dit", 0);
        dah =  settings.getInt("dah", 0);
        
        currMorse = "";
    }


	public void onClick(View v) {
		
		// Button back clicked
		if (v.equals((Button) findViewById(R.id.bt_flash_back))){
	        setResult(RESULT_OK);
	        finish();
		}
		
		// Button start clicked
		if (v.equals((Button) findViewById(R.id.bt_flash_start))){
			// TODO 
			// Do the action here :-)
			
			// this might help:
			// http://stackoverflow.com/questions/3878294/camera-parameters-flash-mode-torch-replacement-for-android-2-1
			// http://mobilecoder.wordpress.com/2010/12/06/android-torch-led/
			
				try
				{
					mCamera = Camera.open();
				
				
				
				
				//... later in a click handler or other location
				//Get camera parameters
				mParameters = mCamera.getParameters();
				 
				//Get supported flash modes
				List flashModes = mParameters.getSupportedFlashModes ();
				 
				//Make sure that torch mode is supported
				if(flashModes != null && flashModes.contains("torch"))
				{
//				    if(mbTorchEnabled){
//				        //Set the flash parameter to off
//				        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);     
//				    }
//				    else{
//				        //Set the flash parameter to use the torch
//				        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);  
//				    }
//				 
//					//Commit the camera parameters
//				    mCamera.setParameters(mParameters);
//				 
//				    mbTorchEnabled = !mbTorchEnabled;
				    
				    //Start Thread to activate Camera
			    	outThread = new Thread( new FlashOutRunnable() );
			    	if (!outThread.isAlive())
			    		outThread.start();
			    	
			        
				}else
				{
					notavailable = true;
					Toast.makeText( getApplicationContext(),
							 "Flash light not supported",
							 Toast.LENGTH_SHORT).show();
				}
					 
				}catch (Exception e)
				{
					if (notavailable)
						{
							Toast.makeText( getApplicationContext(),
									"Not available on this device",
							 Toast.LENGTH_SHORT).show();
						}
					e.printStackTrace();
				}
				//mCamera.release();
			
//			new AlertDialog.Builder(this).setMessage(
//			"Not implemented yet!")
//			.setNeutralButton("Schade...", null).show();
		}
	}
	
	
	
	
	protected class FlashOutRunnable implements Runnable 
	{
		/**
		 * Does all the Work: Flashlight blinks according to the morsecode
		 */
		public void run() 
		{
			Log.i("FlashDisView-Thread","Thread started, in: " + inputtext);

    		// Short pause before start 
    		try {
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			
    		
    		// output for every morsecode-char
			for (int i=0; i<morsecode.length(); i++)
			{
				if (currMorse.length() < morsecode.length()) 
					currMorse = currMorse + morsecode.substring(i,i+1); 
				if (morsecode.substring(i,i+1).equals("·")) // dit
				{
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(mParameters);
					
					try {
						Thread.sleep(dit);
						mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(mParameters);
						Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang
					} catch (InterruptedException e) {
						e.printStackTrace();
						mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(mParameters);
					}
				}
				if (morsecode.substring(i,i+1).equals("-")) // dah
				{
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(mParameters);
					try {
						Thread.sleep(dah);
						mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(mParameters);
						Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang
					} catch (InterruptedException e) {
						e.printStackTrace();
						mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(mParameters);
					}
				}
	        	if (morsecode.substring(i,i+1).equals(" ")) // space zwischen zwei buchstaben
	        	{
	        		try {
						Thread.sleep(dah); //zwischen Buchstaben in einem Wort wird ein dah (=3dit) Abstand gelassen 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
	        	if (morsecode.substring(i,i+1).equals("\\")) // abstand zwischen zwei wörtern "\"
	        	{
	        		try {
						Thread.sleep(dit*7); //zwischen Wörtern wird eine Pause von sieben dits gemacht.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	     
			}
			
			//Switch off flash at the end
			mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		    mCamera.setParameters(mParameters);
			
			// free camera for other applications
			mCamera.release();

		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try
		{
			mCamera.release();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
