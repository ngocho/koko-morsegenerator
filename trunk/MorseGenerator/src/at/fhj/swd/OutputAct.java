package at.fhj.swd;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OutputAct extends Activity implements OnClickListener, OnCompletionListener{

	protected String morsecode;
	protected SoundVibTask soundVibTask;
	protected boolean vibOut, soundOut;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out);

        // Get the output Text and Morsecode
        morsecode = getIntent().getExtras().getString("morsecode");
        String inputtext = getIntent().getExtras().getString("inputtext");        
        
        // Get options (sound and/or vibration-output)
        vibOut = getIntent().getExtras().getBoolean("vibrationoutput");
        soundOut = getIntent().getExtras().getBoolean("soundoutput");
        
        ((TextView) findViewById(R.id.tv_morsecode)).setText(morsecode);
        ((TextView) findViewById(R.id.tv_inputtext)).setText(inputtext);

        Log.i("SoundOutputAct","Acitivty created");
        
        ((Button) findViewById(R.id.bt_play)).setOnClickListener(this);
        ((Button) findViewById(R.id.bt_sound_back)).setOnClickListener(this);
        
    }

	public void onClick(View v) {
     
		// Play clicked
		if (v.equals(findViewById(R.id.bt_play))) {
			// Play-Button inaktiv setzen
			((Button) findViewById(R.id.bt_play)).setEnabled(false);
			// Ausgabe starten
			soundVibTask = new SoundVibTask();
			soundVibTask.execute();
		}
		
		// Back clicked
		if (v.equals(findViewById(R.id.bt_sound_back))) {
			{   // Provide a button to get back to the main activity				
				if (soundVibTask!=null)	soundVibTask.cancel(true); // cancel output if still running
				setResult(RESULT_OK);
				finish();
			}
		}
	}
	
	private class SoundVibTask extends AsyncTask<String, String, String>
	{
		private volatile boolean canceled = false;
		
		@Override
		protected String doInBackground(String... params) {
			Log.i("SoundTask", "Async task started...");

			// ToneGenerator for Sound-Output
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(getBaseContext().VIBRATOR_SERVICE);
			
			 for (int i=0; i<morsecode.length(); i++)
		        {
				 	if (canceled) break;  // output was canceled -> exit loop
		        	if (morsecode.substring(i,i+1).equals("·"))
		        	{
		        		Log.i("play",".");
		        		if (soundOut) 
		        			tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP , 200);
		        		if (vibOut) 
		        			v.vibrate(200);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tg.stopTone();
		        	}
		        	if (morsecode.substring(i,i+1).equals(" "))
		        	{
		        		Log.i("play","space");
		        		try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Add better errorhandling here
							e.printStackTrace();
						}
		        	}
		        	if (morsecode.substring(i,i+1).equals("\\"))
		        	{
		        		Log.i("play","\\");
		        		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Add better errorhandling here
							e.printStackTrace();
						}
		        	}
		        	if (morsecode.substring(i,i+1).equals("-"))
		        	{
		        		Log.i("play","-");
		        		if (soundOut)
		        			tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP , 1000);
		        		if (vibOut)
		        			v.vibrate(1000);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tg.stopTone();
		
		        	}
		        	// Played all beeps: finished!
		        	if (i==morsecode.length()-1) {     		        				
		        		Log.i("mp","finished");
		        	}
		        }
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Playing finished - enable play-button again
			((Button) findViewById(R.id.bt_play)).setEnabled(true);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();     
			canceled = true;  // output should be stopped
    		Log.i("mp","canceled");
		}
		
	}

	public void onCompletion(MediaPlayer mp) {
		// Playing finished - enable play-button again
		((Button) findViewById(R.id.bt_play)).setEnabled(true);		
	}
}




// ##### Another version with nearly the same result: ##### 

/*
MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.beep_mp3);
mp.setOnCompletionListener(act); 
for (int i=0; i<morsecode.length(); i++)
{
	if (morsecode.substring(i,i+1).equals("·"))
	{
		Log.i("play",".");
		try {
			mp.prepare();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mp.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Add better errorhandling here
			e.printStackTrace();
		}
		mp.stop();

	}
	if (morsecode.substring(i,i+1).equals(" "))
	{
		Log.i("play","space");
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Add better errorhandling here
			e.printStackTrace();
		}
	}
	if (morsecode.substring(i,i+1).equals("\\"))
	{
		Log.i("play","\\");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Add better errorhandling here
			e.printStackTrace();
		}
	}
	if (morsecode.substring(i,i+1).equals("-"))
	{
		Log.i("play","-");
		try {
			mp.prepare();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mp.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Add better errorhandling here
			e.printStackTrace();
		}
		mp.stop();

	}
	// Played all beeps: finished!
	if (i==morsecode.length()-1) {
		mp.stop();	        		
		mp.release();	        		        				
		Log.i("mp","finished");
	}
}*/
