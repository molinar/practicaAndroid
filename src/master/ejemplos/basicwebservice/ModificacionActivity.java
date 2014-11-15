package master.ejemplos.basicwebservice;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

public class ModificacionActivity extends Activity {
	
	JSONObject jsonModificar = new JSONObject();
	Personas personas = new Personas();
	int registros = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificacion);
		
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
		Toast.makeText(this, "Modificación cancelada", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
	
	public void modificarInformacion(View v){
        EditText dni = (EditText)findViewById(R.id.editText1);
        EditText nombre = (EditText)findViewById(R.id.EditText01);
        EditText apellidos = (EditText)findViewById(R.id.editText2);
        EditText direccion = (EditText)findViewById(R.id.editText3);
        EditText telefono = (EditText)findViewById(R.id.editText4);
        EditText equipo = (EditText)findViewById(R.id.EditText02);
        try{
        	jsonModificar.put("DNI",dni.getText().toString());
        	jsonModificar.put("Nombre",nombre.getText().toString());
        	jsonModificar.put("Apellidos",apellidos.getText().toString());
        	jsonModificar.put("Direccion",direccion.getText().toString());
        	jsonModificar.put("Telefono",telefono.getText().toString());
        	jsonModificar.put("Equipo",equipo.getText().toString());
        }catch (Exception e){
			Log.e("BasicWebService",e.toString());
			e.printStackTrace();
		}
		new ModificacionBD().execute();
	}
	
	private class ModificacionBD extends AsyncTask<String, String, String>{
		private ProgressDialog pDialog;
    	private final String url = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw26/fichas";

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(ModificacionActivity.this);
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
				HttpPut httpput = new HttpPut(url);
				StringEntity se = new StringEntity(jsonModificar.toString());
				httpput.setEntity(se);
				httpclient.execute(httpput);
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
		Toast.makeText(this, "Modificación realizada", Toast.LENGTH_LONG).show();
	    finish();
	}
}
