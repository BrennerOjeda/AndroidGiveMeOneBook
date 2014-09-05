package com.rest.android;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.rest.android.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
 
public class bookActivityREST extends Activity {

    private  String SERVICE_URL = "";
    private String codLibro = "";
    
 
    private static final String TAG = "AndroidRESTClientActivity";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.book_activity);
        String host = getIntent().getExtras().getString("host");
        SERVICE_URL = "http://"+host+":8080/GiveMeOneBookApp/rest/library";
        
        
    }
 
    public void buscarData(View vw) {
    	EditText edIDRecurso = (EditText) findViewById(R.id.idRecurso);
        codLibro = edIDRecurso.getText().toString();
        
        if(!codLibro.equals("")){
 
        String sampleURL = SERVICE_URL + "/book/"+codLibro;
        Log.d("url enviada: ", sampleURL);
        MyServiceTask wst = new MyServiceTask(MyServiceTask.GET_TASK, this, "GETting data...");
         
        wst.execute(new String[] { sampleURL });
        }
        else{
        	Toast.makeText(this, "Campo vacio: Ingrese ID ",Toast.LENGTH_LONG).show();
        }
         
    }
 
    public void clearControls(View vw) {
    	EditText edIDRecurso = (EditText) findViewById(R.id.idRecurso);
        EditText edTitRecurso = (EditText) findViewById(R.id.titRecurso);
        EditText edEditRecurso = (EditText) findViewById(R.id.editRecurso);
        EditText edAnioRecurso = (EditText) findViewById(R.id.anioRecurso);
        EditText edEstadoRecurso = (EditText) findViewById(R.id.estRecurso);
 
        edIDRecurso.setText("");
        edTitRecurso.setText("");
        edEditRecurso.setText("");
        edAnioRecurso.setText("");
        edEstadoRecurso.setText("");
                 
    }    
 
    public void handleResponse(String response) {
    	EditText edIDRecurso = (EditText) findViewById(R.id.idRecurso);
        EditText edTitRecurso = (EditText) findViewById(R.id.titRecurso);
        EditText edEditRecurso = (EditText) findViewById(R.id.editRecurso);
        EditText edanioRecurso = (EditText) findViewById(R.id.anioRecurso);
        EditText edEstadoRecurso = (EditText) findViewById(R.id.estRecurso);
        
        edTitRecurso.setText("");
        edEditRecurso.setText("");
        edanioRecurso.setText("");
        edEstadoRecurso.setText("");
         
        try {
             
            JSONObject jso = new JSONObject(response);
            if(!jso.equals(null)){
	            String idPerson = jso.getString("ideRec");
	            String titRecurso = jso.getString("titRec");
	            String editRecurso = jso.getString("editRec");
	            String anioRecurso = jso.getString("anioEdicRec");
	            String estadoRecurso = jso.getString("estRec");
	             
	            edIDRecurso.setText(idPerson);
	            edTitRecurso.setText(titRecurso);
	            edEditRecurso.setText(editRecurso);
	            edanioRecurso.setText(anioRecurso);
	            edEstadoRecurso.setText(estadoRecurso);
            }
            else{
            	Toast.makeText(getApplicationContext(),	"No hay registro", Toast.LENGTH_SHORT).show();
            }
             
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            Toast.makeText(getApplicationContext(),	"No hay registro", Toast.LENGTH_SHORT).show();
        }
         
    }
 
    private void hideKeyboard() {
 
        InputMethodManager inputManager = (InputMethodManager) bookActivityREST.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
 
        inputManager.hideSoftInputFromWindow(
                bookActivityREST.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
     

    private class MyServiceTask extends AsyncTask<String, Integer, String> {
 
        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;
         
        private static final String TAG = "MyServiceTask";
 
        // tiempo de espera de conexion
        private static final int CONN_TIMEOUT = 6000;
         
        // tiempo de espera para la data
        private static final int SOCKET_TIMEOUT = 8000;
         
        private int taskType = GET_TASK;
        private Context mContext = null;
        private String processMessage = "Procesando...";
 
        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
 
        private ProgressDialog pDlg = null;
 
        public MyServiceTask(int taskType, Context mContext, String processMessage) {
 
            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }
 
        private void showProgressDialog() {
             
            pDlg = new ProgressDialog(mContext);
            pDlg.setMessage(processMessage);
            pDlg.setProgressDrawable(mContext.getWallpaper());
            pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDlg.setCancelable(false);
            pDlg.show();
 
        }
 
        @Override
        protected void onPreExecute() {
 
            hideKeyboard();
            showProgressDialog();
 
        }
 
        protected String doInBackground(String... urls) {
 
            String url = urls[0];
            String result = "";
 
            HttpResponse response = doResponse(url);
 
            if (response == null) {
                return result;
            } else {
 
                try {
 
                    result = inputStreamToString(response.getEntity().getContent());
 
                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
 
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                } 
            } 
            return result;
        }
 
        @Override
        protected void onPostExecute(String response) {
             
            handleResponse(response);
            pDlg.dismiss();
             
        }

        private HttpParams getHttpParams() {
             
            HttpParams htpp = new BasicHttpParams();
             
            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
             
            return htpp;
        }
         
        private HttpResponse doResponse(String url) {
             
            HttpClient httpclient = new DefaultHttpClient(getHttpParams()); 
            HttpResponse response = null;
 
            try {
                switch (taskType) {
 
                case POST_TASK:
                    HttpPost httppost = new HttpPost(url);
                    // Add parameters
                    httppost.setEntity(new UrlEncodedFormEntity(params));
 
                    response = httpclient.execute(httppost);
                    break;
                case GET_TASK:
                    HttpGet httpget = new HttpGet(url);
                    response = httpclient.execute(httpget);
                    break;
                }
            } catch (Exception e) {
 
                Log.e(TAG, e.getLocalizedMessage(), e);
 
            }
 
            return response;
        }
         
        private String inputStreamToString(InputStream is) {
 
            String line = "";
            StringBuilder total = new StringBuilder();
 
            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 
            try {
                // Read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
 
            // Return full string
            return total.toString();
        }
 
    }
}