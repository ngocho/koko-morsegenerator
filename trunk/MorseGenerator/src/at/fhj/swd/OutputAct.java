package at.fhj.swd;

import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
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

/**
 * Handles the vibration- and sound-output 
 * @author Matthias Koinegg
 */
public class OutputAct extends Activity implements OnClickListener, OnCompletionListener{

	protected String morsecode;
	protected SoundVibTask soundVibTask;
	protected boolean vibOut, soundOut;
	private int dit, dah; // dit = "."    dah = "-"    see http://www.teachersparadise.com/ency/de/wikipedia/m/mo/morsecode.html
	private TextView tv_morsecode;
	
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
        
	    // Restore preferences
	    SharedPreferences settings = getSharedPreferences("MORSE", 0);
	    dit =  settings.getInt("dit", 0);
	    dah =  settings.getInt("dah", 0);
        
        // Various initializations...
	    tv_morsecode = ((TextView) findViewById(R.id.tv_morsecode));

	    tv_morsecode.setText(""); 
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

			//DEBUG
			long start = System.nanoTime();
			
			// ToneGenerator for Sound-Output
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 80);
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(getBaseContext().VIBRATOR_SERVICE);
			
			 for (int i=0; i<morsecode.length(); i++)
		        {
				 	// to update the current morsecode-char on the ui:
				 	publishProgress(morsecode.substring(0,i+1));
				 	
				 	if (canceled) break;  // output was canceled -> exit loop
		        	if (morsecode.substring(i,i+1).equals("·"))  // dit
		        	{
		        		if (soundOut) {
		        			Log.i("time", "before starttone " + String.valueOf(((System.nanoTime()-start))/1000000));
		        			tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
		        		}
		        		if (vibOut) 
		        			v.vibrate(dit);
		        		
						try {			
							Thread.sleep(dit); // play dit
							tg.stopTone();
		        			Log.i("time", "after stoptone " + String.valueOf(((System.nanoTime()-start))/1000000));
							Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
		        	}
		        	
		        	if (morsecode.substring(i,i+1).equals("-")) //dah
		        	{
		        		if (soundOut)
		        			tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
		        		if (vibOut)
		        			v.vibrate(dah);
						try {
							Thread.sleep(dah); // play dah
							tg.stopTone();
							Thread.sleep(dit); // abstand zwischen zwei symbolen ist ein dit lang
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//tg.stopTone();
		
		        	}
		        	
		        	if (morsecode.substring(i,i+1).equals(" ")) // space zwischen zwei buchstaben
		        	{
		        		try {
		        			Thread.sleep(dah); //zwischen Buchstaben in einem Wort wird ein dah (=3dit) Abstand gelassen 
						} catch (InterruptedException e) {
							// TODO Add better errorhandling here
							e.printStackTrace();
						}
		        	}
		        	
		        	if (morsecode.substring(i,i+1).equals("\\")) // abstand zwischen zwei wörtern "\"
		        	{
		        		try {
		        			Thread.sleep(dit*7); //zwischen Wörtern wird eine Pause von sieben dits gemacht.
						} catch (InterruptedException e) {
							// TODO Add better errorhandling here
							e.printStackTrace();
						}
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
		
		@Override
		protected void onProgressUpdate(String... values)  {
			// to update the current morsecode-char on the ui
			tv_morsecode.setText(values[0]);
		}

	}

	public void onCompletion(MediaPlayer mp) {
		// Playing finished - enable play-button again
		// Nur für die mediaplayer-variante relevant
		// ((Button) findViewById(R.id.bt_play)).setEnabled(true);
	}
}

// ##### Another version of playing sounds could be: ##### 

/*
MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.beep_mp3);
mp.prepare();
mp.start();
mp.stop();
*/
