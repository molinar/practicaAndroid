package master.ejemplos.basicwebservice;

import org.apache.http.client.methods.HttpDelete;
import org.json.JSONArray;
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

public class BorradoActivity extends Activity {
	JSONObject jsonBorrar = new JSONObject();
	Personas personas = new Personas();
	String dni_borrar;
	int registros = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrado);
		
		Intent intent = getIntent();
        String resultado = intent.getStringExtra("respuesta");
        stringToArray(resultado);
        
	}
	
	public void stringToArray(String resultado){
		try{
			JSONArray json = new JSONArray(resultado);
			registros = Integer.parseInt(json.getJSONObject(0).getString("NUMREG"));
			if(registros == 0){
				volverMain();
			}else{
				JSONObject jsonArrayChild = json.getJSONObject(1);
				personas.setDni(jsonArrayChild.getString("DNI"));
				personas.setNombre(jsonArrayChild.getString("Nombre"));
				personas.setApellidos(jsonArrayChild.getString("Apellidos"));
				personas.setDireccion(jsonArrayChild.getString("Direccion"));
				personas.setTelefono(jsonArrayChild.getString("Telefono"));
				personas.setEquipo(jsonArrayChild.getString("Equipo"));
				dni_borrar = personas.getDni();
				mostrar();
			}
		}catch (Exception e){
			Log.e("BasicWebService",e.toString());
			e.printStackTrace();
		}
	}
	
	public void mostrar(){
		EditText dni = (EditText)findViewById(R.id.editText1);
		EditText nombre = (EditText)findViewById(R.id.EditText01);
		EditText apellidos = (EditText)findViewById(R.id.editText2);
		EditText direccion = (EditText)findViewById(R.id.editText3);
		EditText telefono = (EditText)findViewById(R.id.editText4);
		EditText equipo = (EditText)findViewById(R.id.EditText02);
		nombre.setText(personas.getNombre());
		dni.setText(personas.getDni());
		apellidos.setText(personas.getApellidos());
		direccion.setText(personas.getDireccion());
		telefono.setText(personas.getTelefono());
		equipo.setText(personas.getEquipo());
	}
	
	
	public void volverMain(){
		Intent i = new Intent(this, MainActivity.class);	
	    setResult(RESULT_CANCELED, i);
		Toast.makeText(this, "Registro no existente", Toast.LENGTH_LONG).show();
	    finish();
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		Toast.makeText(this, "Borrado cancelado", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
	
	public void borrarInformacion(View v){
        try{
        	jsonBorrar.remove("DNI");        	
        	jsonBorrar.remove("Nombre");
        	jsonBorrar.remove("Apellidos");
        	jsonBorrar.remove("Direccion");
        	jsonBorrar.remove("Telefono");
        	jsonBorrar.remove("Equipo");
        }catch (Exception e){
			Log.e("BasicWebService",e.toString());
			e.printStackTrace();
		}
		new BorradoBD().execute();
	}
	
	private class BorradoBD extends AsyncTask<String, String, String>{
		private ProgressDialog pDialog;
    	private final String url = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw26/fichas";

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(BorradoActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			AndroidHttpClient httpclient = null;
			String url_final =url;
			url_final +="/"+dni_borrar;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpDelete httpdelete = new HttpDelete(url_final);
				httpclient.execute(httpdelete);
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
		Intent i = new Intent(this, MainActivity.class);	
		setResult(RESULT_OK,i);
		Toast.makeText(this, "Borrado realizado", Toast.LENGTH_LONG).show();
	    finish();
	}

	
}
