package at.fhj.swd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingsAct extends Activity implements OnSeekBarChangeListener, OnClickListener{

	SeekBar seekBarSpeed;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	// set layout as defined in xml-file 
		setContentView(R.layout.settings);		
		
        // Provide a button to get back to the main activity
        Button bt_back = (Button) findViewById(R.id.bt_set_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   setResult(RESULT_OK);
        	   finish();
           }
        });
        
        //initialization for save-button
        ((Button) findViewById(R.id.bt_set_save)).setOnClickListener(this);

        // Read current values from preferences
	    SharedPreferences settings = getSharedPreferences("MORSE", 0);
	    int dit =  settings.getInt("dit", 0); // default = 200 
	    int dah =  settings.getInt("dah", 0);
        
        //initialization for seekbar
        seekBarSpeed = (SeekBar) findViewById(R.id.sb_speed);
        seekBarSpeed.setMax(5);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        // allowed range for dit: 100 - 500 
        seekBarSpeed.setProgress((dit/100)-1);
        
        //initialization of the current seekbar value
        ((TextView) findViewById(R.id.tv_set_speed)).setText(
        		String.valueOf((seekBarSpeed.getProgress()+1)*100));
    }

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	      ((TextView) findViewById(R.id.tv_set_speed)).setText(
	        		String.valueOf((seekBarSpeed.getProgress()+1)*100));
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		if (v.equals(findViewById(R.id.bt_set_save)))
		{
			//write current settings to preferences
	        SharedPreferences settings = getSharedPreferences("MORSE", MODE_WORLD_READABLE );
        	SharedPreferences.Editor editor = settings.edit();
        	int dit = (seekBarSpeed.getProgress()+1)*100;
	        editor.putInt("dit", dit);
        	editor.putInt("dah", dit*3); //ein dah ist üblicherweise dreimal so lang wie ein dit,
        	editor.commit();
		}		
	}	
}
