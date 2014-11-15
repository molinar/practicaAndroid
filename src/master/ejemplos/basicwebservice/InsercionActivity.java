package master.ejemplos.basicwebservice;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsercionActivity extends Activity {
	String dni;
	JSONObject json = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insercion);
		
		Intent intent = getIntent();
        dni = intent.getStringExtra("dni");
        insertarDNI();
	}
	
	public void insertarDNI(){
		EditText campo_dni = (EditText)findViewById(R.id.editText1);
		campo_dni.setText(dni);
	}
	
	public void obtenerInformacion(View v){
        EditText nombre = (EditText)findViewById(R.id.EditText01);
        EditText apellidos = (EditText)findViewById(R.id.editText2);
        EditText direccion = (EditText)findViewById(R.id.editText3);
        EditText telefono = (EditText)findViewById(R.id.editText4);
        EditText equipo = (EditText)findViewById(R.id.EditText02);
        try{
        	json.put("DNI",dni);        	
        	json.put("Nombre",nombre.getText().toString());
        	json.put("Apellidos",apellidos.getText().toString());
        	json.put("Direccion",direccion.getText().toString());
        	json.put("Telefono",telefono.getText().toString());
        	json.put("Equipo",equipo.getText().toString());
        }catch (Exception e){
			Log.e("BasicWebService",e.toString());
			e.printStackTrace();
		}
		new InsercionBD().execute();
	}
	
	private class InsercionBD extends AsyncTask<String, String, String>{
		private ProgressDialog pDialog;
    	private final String url = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw26/fichas";

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(InsercionActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			AndroidHttpClient httpclient = null;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(url);
				StringEntity se = new StringEntity(json.toString());
				httppost.setEntity(se);
				httpclient.execute(httppost);
				httpclient.close();
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			terminar();
    		super.onPostExecute(result);
		}
	}
	
	public void terminar() {
		Intent i = new Intent(InsercionActivity.this, MainActivity.class);	
		setResult(RESULT_OK,i);
		Toast.makeText(this, "Inserción realizada", Toast.LENGTH_LONG).show();
	    finish();
	}
	
    @Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		Toast.makeText(this, "Inserción cancelada", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
}
