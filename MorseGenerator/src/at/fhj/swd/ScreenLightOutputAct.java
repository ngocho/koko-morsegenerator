package at.fhj.swd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ScreenLightOutputAct extends Activity {
	
	ScreenLightOutputView scrLightOut;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		setContentView(R.layout.out_light_dis);
		
		LinearLayout lin_layout = (LinearLayout) findViewById(R.id.linLayout01);
		
		scrLightOut = 
			new ScreenLightOutputView(this, getIntent().getExtras().getString("morsecode"),
					getIntent().getExtras().getString("inputtext"));
		lin_layout.addView(scrLightOut);
		
        // Provide a button to get back to the main activity
        Button bt_back = (Button) findViewById(R.id.bt_back_light_dis);
        bt_back.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   setResult(RESULT_OK);
        	   finish();
           }
        });
        
    }

}
