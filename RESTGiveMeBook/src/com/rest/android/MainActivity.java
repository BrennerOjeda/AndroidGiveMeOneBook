package com.rest.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_principal);
    }

    
    public void conectar(View view) {
    	EditText edHost = (EditText) findViewById(R.id.edHost);
    	String host = edHost.getText().toString();
        Intent intent = new Intent(this, bookActivityREST.class );
        intent.putExtra("host", host);
        startActivity(intent);
  }    
    
}
