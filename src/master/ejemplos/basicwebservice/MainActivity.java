package master.ejemplos.basicwebservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

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

public class MainActivity extends Activity {
	String respuesta;
	private final int INSERCION = 0;
   	private final int CONSULTA = 0;
	private EditText dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dni = (EditText)findViewById(R.id.editText1);
    }
    
    public void conectar(View v){
    	if (dni.getText().length() > 8){
        	Toast.makeText(this, "El DNI introducido no es valido", Toast.LENGTH_LONG).show();
    	}else{
    		new ConsultaBD().execute(dni.getText().toString());
    	}
    }
    
    private void irConsulta(String respuesta) {
		Intent i = new Intent(MainActivity.this, ConsultaActivity.class);	
		i.putExtra("respuesta", respuesta.toString());
		startActivityForResult(i,CONSULTA);	
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
			respuesta = getString(R.string.sin_respuesta);
			String url_final =url;
			if(!dnis[0].equals("")){
				url_final +="/"+dnis[0];
			}
			AndroidHttpClient httpclient = null;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
				e.printStackTrace();
			
			}
			return respuesta;
		}
		
    	protected void onPostExecute(String respuesta) {
    		pDialog.dismiss();
    		irConsulta(respuesta);
		}
    }
	
	private class ConsultaDNIInsertar extends AsyncTask<String, String, String>{
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
			respuesta = getString(R.string.sin_respuesta);
			String url_final =url;
			if(!dnis[0].equals("")){
				url_final +="/"+dnis[0];
			}
			AndroidHttpClient httpclient = null;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
				e.printStackTrace();		
			}
			return respuesta;
		}
		
    	protected void onPostExecute(String respuesta) {
    		pDialog.dismiss();
    		irInsertar(respuesta);
		}
    }
    
	public void insertar(View v){
		if (dni.getText().length() > 8 || dni.getText().length() == 0){
        	Toast.makeText(this, "El DNI introducido no es valido", Toast.LENGTH_LONG).show();
    	}else{
    		new ConsultaDNIInsertar().execute(dni.getText().toString());
    	}
    }
	
    public void irInsertar(String respuesta){
    	try{
    		JSONArray json = new JSONArray(respuesta);
    		int registros = Integer.parseInt(json.getJSONObject(0).getString("NUMREG"));
    		Log.d("MainActivity", respuesta);
    		if(registros == 0){
    			Intent i = new Intent(this, InsercionActivity.class);	
    			i.putExtra("dni", dni.getText().toString());
    			startActivityForResult(i,INSERCION);
    		}else{
    			Toast.makeText(this, "El DNI introducido ya existe", Toast.LENGTH_LONG).show();
    		}
    	}catch (Exception e){
			Log.e("BasicWebService",e.toString());
			e.printStackTrace();		
		}
    	
    }
    
    
    public void borrar(View v){
		if (dni.getText().length() > 8 || dni.getText().length() == 0){
        	Toast.makeText(this, "El DNI introducido no es valido", Toast.LENGTH_LONG).show();
    	}else{
    		new ConsultaDNIBorrar().execute(dni.getText().toString());
    	}
    }
    
    private void irBorrado(String respuesta) {
		Intent i = new Intent(MainActivity.this, BorradoActivity.class);	
		i.putExtra("respuesta", respuesta.toString());
		startActivityForResult(i,CONSULTA);	
	}

	private class ConsultaDNIBorrar extends AsyncTask<String, String, String>{
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
			respuesta = getString(R.string.sin_respuesta);
			String url_final =url;
			if(!dnis[0].equals("")){
				url_final +="/"+dnis[0];
			}
			AndroidHttpClient httpclient = null;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
				e.printStackTrace();
			
			}
			return respuesta;
		}
		
    	protected void onPostExecute(String respuesta) {
    		pDialog.dismiss();
    		irBorrado(respuesta);
		}
    }
	
    public void modificar(View v){
		if (dni.getText().length() > 8 || dni.getText().length() == 0){
        	Toast.makeText(this, "El DNI introducido no es valido", Toast.LENGTH_LONG).show();
    	}else{
    		new ConsultaDNIModificar().execute(dni.getText().toString());
    	}
    }
    
    private void irModificado(String respuesta) {
		Intent i = new Intent(MainActivity.this, ModificacionActivity.class);	
		i.putExtra("respuesta", respuesta.toString());
		startActivityForResult(i,CONSULTA);	
	}

	private class ConsultaDNIModificar extends AsyncTask<String, String, String>{
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
			respuesta = getString(R.string.sin_respuesta);
			String url_final =url;
			if(!dnis[0].equals("")){
				url_final +="/"+dnis[0];
			}
			AndroidHttpClient httpclient = null;
			try{
				httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			}catch (Exception e){
				Log.e("BasicWebService",e.toString());
				e.printStackTrace();
			
			}
			return respuesta;
		}
		
    	protected void onPostExecute(String respuesta) {
    		pDialog.dismiss();
    		irModificado(respuesta);
		}
    }

}
