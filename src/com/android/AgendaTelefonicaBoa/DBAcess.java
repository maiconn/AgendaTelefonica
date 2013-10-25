package com.android.AgendaTelefonicaBoa;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAcess extends SQLiteOpenHelper{
	private static final String BANCO = "AgendaTelefonica.db";
	private static final String PATH = "data/data/com.android.AgendaTelefonicaBoa/databases/"+BANCO;
	private static final String TABELA = "create table tb_contatos (" +
			"codigo integer not null primary key," +
			"nome text not null," +
			"telefone text," +
			"celular text," +
			"email text," +
			"foto text)";
	private static final String TABLE_NAME = "tb_contatos";
	private static final int VERSION = 1;
	private SQLiteDatabase db;
	public showAlert alertas = new showAlert();
	private Context context;

	public DBAcess(Context context) {
		super(context, BANCO, null, VERSION);
		this.context = context;
		if (!isCreateDB(db)){
			getWritableDatabase();
		}
	}

	private boolean isCreateDB(SQLiteDatabase db){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
			db.close();
			return true;
		}catch (SQLiteException ex){
			return false;			
		}
	}

	public void insertContato(String numero, String nome, String tel, String cel, String email, String isLoadFoto){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
			db.execSQL("insert into "+TABLE_NAME+" (codigo, nome, telefone, celular, email, foto) values " +
					"("+numero+", '"+nome+"', '"+tel+"', '"+cel+"', '"+email+"', '"+isLoadFoto+"')");
			db.close();
		} catch (SQLException ex){
			alertas.showSimpleDialog("Erro", "Erro ao inserir contato: "+ex, context);
			db.close();
		}		
	}

	public ArrayList<String> mostrarNomes(){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
			String[] tab = {"nome"};
			Cursor c = db.query(TABLE_NAME, tab, null, null, null, null, "nome");
			ArrayList<String> resultado = new ArrayList<String>();



			if (c.getCount() != 0){
				c.moveToFirst();
				while (!c.isLast()) {
					resultado.add(c.getString(0));
					c.moveToNext();
				}
				if (c.isLast()){
					resultado.add(c.getString(0));
				}
			}
			db.close();
			return resultado;
		} catch(SQLiteException ex){
			alertas.showSimpleDialog("Erro", "Erro ao selecionar nomes: "+ex, context);
			return null;
		}
	}

	public ArrayList<String> mostraDados(int codigo){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
			Cursor c = db.query(TABLE_NAME, null, "codigo = "+codigo, null, null, null, null);

			ArrayList<String> resultado = new ArrayList<String>();
			c.moveToFirst();
			resultado.add(Integer.toString(c.getInt(0)));
			resultado.add(c.getString(1));
			resultado.add(c.getString(2));
			resultado.add(c.getString(3));
			resultado.add(c.getString(4));
			resultado.add(c.getString(5));
			db.close();
			return resultado;
		}catch(SQLiteException ex){
			alertas.showSimpleDialog("Erro", "Erro ao selecionar contato: "+ex, context);
			db.close();
			return null;
		}


	}

	public int getCodigo (String nome){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
			String[] coodigo = {"codigo"};
			Cursor c = db.query(TABLE_NAME, coodigo, "nome = '"+nome+"'", null, null, null, null);
			c.moveToFirst();	
			int codigo = c.getInt(0);
			db.close();
			return codigo;		
		}catch(SQLiteException ex){
			db.close();
			return 0;
		}finally{
			db.close();
		}
	}

	public int getProxCodigo(){
		db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
		String[] tab = {"codigo"};
		Cursor c = db.query(TABLE_NAME, tab, null, null, null, null, null);

		if (c.getCount() == 0){
			db.close();
			return 1;
		} else {
			c.moveToLast();
			int ultimoCodigo = c.getInt(0);
			db.close();
			return ultimoCodigo+1;
		}		
	}

	public void ExcluirContato (int codigo){
		try{
			db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
			db.execSQL("DELETE FROM "+TABLE_NAME+" where codigo = "+codigo);
			db.close();
		} catch(SQLiteException ex){
			alertas.showSimpleDialog("Erro", "Não foi possível excluir contato! ERRO: "+ex, context);
		}
	}

	public boolean isNomeExistente(String nome){
		db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		String[] col = {"nome"};
		Cursor c = db.query(TABLE_NAME, col, "nome='"+nome+"'", null, null, null, null);


		if (c.getCount() == 1){
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}

	public void updateContato(String codigo, String nome, String tel, String cel, String email, String foto){		
		db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
		db.execSQL("UPDATE "+TABLE_NAME+" SET nome='"+nome+"', telefone='"+tel+"', celular='"+cel+"', email='"+email+"', " +
				"foto='"+foto+"' where codigo="+codigo);
		db.close();
	}

	public boolean isNameExist4DifCodigo(String nome, String codigo){
		db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
		String[] col = {"nome"};
		Cursor c = db.query(TABLE_NAME, col, "nome='"+nome+"' and codigo<>"+codigo, null, null, null, null);

		if (c.getCount() == 0){
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABELA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}


}
