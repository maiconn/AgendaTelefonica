package com.android.AgendaTelefonicaBoa;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.utils.ConstantsUtils;
import com.android.utils.ImagemUtils;

public class TelaNovoContato extends Activity {
	
	private Button bt_addcontato_Add_Contato, bt_addcontato_Cancelar, bt_carregarimagem;
	
	private EditText ed_addcontato_codigo, ed_addcontato_nome, ed_addcontato_tel, ed_addcontato_cel, ed_addcontato_email, ed_addcontato_foto;
	
	private TextView tv_contatoSelecionado_nome, tv_contatoSelecionado_codigo, tv_contatoSelecionado_tel, tv_contatoSelecionado_cel, 
	tv_contatoSelecionado_email, tv_editar_novafoto;
	private ListView listaContatos;
	public showAlert alertas = new showAlert();
	private DBAcess database;
	private ImageView imagemLoad, imageView, imageAlter;
	private ArrayList<String> getContato;
	private Drawable setDrawable;
	private String url;
	String cod;
	boolean isFotoCarregada = false;
	boolean isCancelButton = false;
	boolean isNewFoto = false;
	boolean isFotoExcluida = false;
	private boolean estaEditando = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.tela_novo_contato);
			atribuiViews();
			bt_carregarimagem.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					url = ed_addcontato_foto.getText().toString();
					if (!url.equals("")){
						estaEditando = false;
						new Progresso().execute(url);
					} else {
						alertas.showSimpleDialog("Foto", "Digite um caminho da web para carregar a foto!", TelaNovoContato.this);
						isFotoCarregada = false;
						imagemLoad.setImageResource(R.drawable.icon);
					}
				}
			});

			bt_addcontato_Cancelar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			String codigo = Integer.toString(database.getProxCodigo());
			ed_addcontato_codigo.setText(codigo);

			bt_addcontato_Add_Contato.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nome = ed_addcontato_nome.getText().toString();
					String tel = ed_addcontato_tel.getText().toString();
					String cel = ed_addcontato_cel.getText().toString();
					String email = ed_addcontato_email.getText().toString();
					String numero = ed_addcontato_codigo.getText().toString();

					if (tel.equals("")){
						tel = "(N�o Cadastrado)";
					}
					if (cel.equals("")){
						cel = "(N�o Cadastrado)";
					}
					if (email.equals("")){
						email = "(N�o Cadastrado)";
					}

					if (!nome.equals("")){
						if (!database.isNomeExistente(nome)){
							if (!tel.equals("(N�o Cadastrado)") || !cel.equals("(N�o Cadastrado)") || !email.equals("(N�o Cadastrado)")){
								if (isFotoCarregada){
									ImagemUtils.downloadFromUrl(url, ConstantsUtils.CAMINHO_FOTO+numero+".jpg");
									database.insertContato(numero, nome, tel, cel, email, "true");								
									alertas.showSimpleDialog("Sucesso", "Contato Adicionado Com Sucesso!", TelaNovoContato.this);
									isFotoCarregada = false;
								} else {
									database.insertContato(numero, nome, tel, cel, email, "false");
									alertas.showSimpleDialog("Sucesso", "Contato Adicionado Com Sucesso!", TelaNovoContato.this);
								}
							} else {
								alertas.showSimpleDialog("Contato", "Precisa ter ao menos um meio de contato!", TelaNovoContato.this);
							}
						} else {
							alertas.showSimpleDialog("Nome", "Nome J� Cadastrado Na Lista!", TelaNovoContato.this);
						}
					} else {
						alertas.showSimpleDialog("Nome", "Digite um nome para o contato!", TelaNovoContato.this);
					}
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void atribuiViews() {
		bt_addcontato_Add_Contato = (Button) findViewById(R.adicionar_contato.botao_Adicionar);
		bt_addcontato_Cancelar = (Button) findViewById(R.adicionar_contato.botao_Cancelar);
		ed_addcontato_nome = (EditText) findViewById(R.adicionar_contato.campo_nome);
		ed_addcontato_codigo = (EditText) findViewById(R.adicionar_contato.campo_codigo);
		ed_addcontato_tel = (EditText) findViewById(R.adicionar_contato.campo_telefone);
		ed_addcontato_cel = (EditText) findViewById(R.adicionar_contato.campo_celular);
		ed_addcontato_email = (EditText) findViewById(R.adicionar_contato.campo_email);
		ed_addcontato_foto = (EditText) findViewById(R.adicionar_contato.EdFoto);
		bt_carregarimagem = (Button) findViewById(R.adicionar_contato.botao_carregarimagem);
		imagemLoad = (ImageView) findViewById(R.adicionar_contato.FotoLoad);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tela_novo_contato, menu);
		return true;
	}

	public class Progresso extends AsyncTask<String, String, String>{
		ProgressDialog pg;
		@Override
		protected void onPreExecute() {
			pg = new ProgressDialog(TelaNovoContato.this);
			pg.setTitle("Aguarde...");
			pg.setMessage("Carregando foto!");
			pg.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			setDrawable = ImagemUtils.loadImagemFromWebOperations(url);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			pg.dismiss();
			if(estaEditando){
				if (setDrawable != null){
					imageAlter.setImageDrawable(setDrawable);
					isNewFoto = true;
				} else {
					isNewFoto = false;
					imageAlter.setImageResource(R.drawable.icon);
					alertas.showSimpleDialog("Foto", "URL Inv�lido!", TelaNovoContato.this);
				}	
			}else{
				if (setDrawable != null){
					imagemLoad.setImageDrawable(setDrawable);
					isFotoCarregada = true;
				} else {
					isFotoCarregada = false;
					imagemLoad.setImageResource(R.drawable.icon);
					alertas.showSimpleDialog("Foto", "URL Inv�lido!", TelaNovoContato.this);
				}	
			}
					
		}		
	}
}
