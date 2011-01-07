package at.fhj.swd;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FlashOutputAct extends Activity implements OnClickListener{

	protected String morsecode;
	protected String inputtext;
	
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
			
			new AlertDialog.Builder(this).setMessage(
			"Not implemented yet!")
			.setNeutralButton("Schade...", null).show();
		}
	}
}
