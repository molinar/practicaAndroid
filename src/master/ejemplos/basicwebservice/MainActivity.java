package master.ejemplos.basicwebservice;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText dni;
	ArrayList<Personas> listaPersonas = new ArrayList<Personas>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dni = (EditText)findViewById(R.id.editText1);
    }
    
    public void conectar(View v){
    	new ConsultaBD().execute(dni.getText().toString());

    }
    
    private class ConsultaBD extends AsyncTask<String, String, String>{
       	private ProgressDialog pDialog;
    	private final String url = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw26/fichas";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final =url;
			if(!dnis[0].equals("")){
				url_final +="/"+dnis[0];
			}
			try{
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());
				
				JSONObject json = new JSONObject(respuesta);
				JSONArray jsonArray = json.optJSONArray("personas");
				for(int i = 0; i < jsonArray.length();i++){
					Personas personas = new Personas();
					JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
					personas.setDni(jsonArrayChild.optString("dni"));
					personas.setNombre(jsonArrayChild.optString("nombre"));
					personas.setApellidos(jsonArrayChild.optString("apellidos"));
					personas.setDireccion(jsonArrayChild.optString("direccion"));
					personas.setTelefono(jsonArrayChild.optString("telefono"));
					personas.setEquipo(jsonArrayChild.optString("equipo"));
					listaPersonas.add(personas);
				}
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
			}
			return respuesta;
		}
		
    	protected void onPostExecute(String respuesta) {
    		pDialog.dismiss();
    		Toast.makeText(getBaseContext(), respuesta, Toast.LENGTH_LONG).show();
		}
    }

}
