package master.ejemplos.basicwebservice;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConsultaActivity extends Activity {
	ArrayList<Personas> listaPersonas = new ArrayList<Personas>();
	int numero_registro = 0;
	int registros = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);
		
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
				for(int i = 1; i < json.length();i++){
					Personas personas = new Personas();
					JSONObject jsonArrayChild = json.getJSONObject(i);
					personas.setDni(jsonArrayChild.getString("DNI"));
					personas.setNombre(jsonArrayChild.getString("Nombre"));
					personas.setApellidos(jsonArrayChild.getString("Apellidos"));
					personas.setDireccion(jsonArrayChild.getString("Direccion"));
					personas.setTelefono(jsonArrayChild.getString("Telefono"));
					personas.setEquipo(jsonArrayChild.getString("Equipo"));
					listaPersonas.add(personas);
					mostrar();
				}
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
		nombre.setText(listaPersonas.get(numero_registro).getNombre());
		dni.setText(listaPersonas.get(numero_registro).getDni());
		apellidos.setText(listaPersonas.get(numero_registro).getApellidos());
		direccion.setText(listaPersonas.get(numero_registro).getDireccion());
		telefono.setText(listaPersonas.get(numero_registro).getTelefono());
		equipo.setText(listaPersonas.get(numero_registro).getEquipo());
	}
	
	public void mostrarPrimero(View v){
		numero_registro = 0;
		mostrar();
	}
	
	public void mostrarUltimo(View v){
		numero_registro = registros-1;
		mostrar();
	}
	
	public void mostrarSiguiente(View v){
		if(numero_registro >= registros-1){
			Toast.makeText(this, "No hay registros posteriores", Toast.LENGTH_LONG).show();
		}else{
			numero_registro++;
			mostrar();
		}
	}
	
	public void mostrarAnterior(View v){
		if(numero_registro <= 0){
			Toast.makeText(this, "No hay registros anteriores", Toast.LENGTH_LONG).show();
		}else{
			numero_registro--;
			mostrar();
		}
	}
	
	public void volverMain() {
		Intent i = new Intent(this, MainActivity.class);
		setResult(RESULT_CANCELED, i);
		Toast.makeText(this, "Registro no existente", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		Toast.makeText(this, "Consulta finalizada", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
}
