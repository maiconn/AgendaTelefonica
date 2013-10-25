package com.android.AgendaTelefonicaBoa;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class showAlert{
	EditText edteste;
	public void showSimpleDialog(String title, String message, Context context){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context); 
		alertDialog.setTitle(title); 
		alertDialog.setMessage(message); 
		alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int which) {           

			} 
		}); 
		alertDialog.show();
	}
	public void showYesNoDialog(String title, String message, Context context, OnClickListener positive, OnClickListener negative){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context); 
		alertDialog.setTitle(title); 
		alertDialog.setMessage(message); 
		alertDialog.setPositiveButton("Sim", positive);
		alertDialog.setNegativeButton("Não", negative);
		alertDialog.show();
	}
	public void showAlertAguardarEnquantoCarrega(String title, String message, Context context){
		final ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setTitle(title);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setButton("Cancelar", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressDialog.cancel();
			}
		});
		progressDialog.show();
	}
}
