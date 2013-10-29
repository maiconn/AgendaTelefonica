package com.android.AgendaTelefonicaBoa;


import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.utils.ConstantsUtils;
import com.android.utils.ImagemUtils;

public class AgendaTelefonicaBoaActivity extends Activity {
	
	private Button bt_main_verContatos, bt_main_add_contato,
	bt_addcontato_Add_Contato, bt_addcontato_Cancelar,
	bt_contatoSelecionado_Editar, bt_contatoSelecionado_Excluir, bt_contatoSelecionado_Voltar,
	bt_editar_ok, bt_editar_cancelar, bt_carregarimagem,
	bt_editar_carregar, bt_editar_alterar, bt_editar_excluir_foto;
	private EditText ed_addcontato_codigo, ed_addcontato_nome, ed_addcontato_tel, ed_addcontato_cel, ed_addcontato_email,
	ed_editar_nome, ed_editar_tel, ed_editar_cel, ed_editar_email, ed_addcontato_foto,
	ed_editar_foto;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		database = new DBAcess(this);
		iniciaTelaInicial();		
	}

	private void iniciaTelaInicial(){
		bt_main_verContatos = (Button) findViewById(R.id.visualizar_contatos);
		bt_main_add_contato = (Button) findViewById(R.id.adicionar_contato);

		bt_main_add_contato.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				iniciaAdicionarContato();
			}
		});		
		bt_main_verContatos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				iniciaListaContatos();
			}
		});	
	}

	private void iniciaAdicionarContato(){
		startActivity(new Intent(this, TelaNovoContato.class));
	}

	private void iniciaListaContatos(){
		setContentView(R.layout.ver_contatos);		
		listaContatos = (ListView) findViewById(R.verContatos.listaContatos);

		ArrayList<String> nomes = new ArrayList<String>();

		nomes = database.mostrarNomes();

		if (nomes.size() != 0){
			final ArrayAdapter<String> listanomes = new ArrayAdapter<String>(AgendaTelefonicaBoaActivity.this, android.R.layout.simple_list_item_1, nomes);

			listaContatos.setAdapter(listanomes);		
			listaContatos.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					getContato = new ArrayList<String>();
					final String contatoSelecionado = listanomes.getItem(arg2);
					final int Codigo = database.getCodigo(contatoSelecionado);

					getContato = database.mostraDados(Codigo);

					String nome, codigo, tel, cel, email;
					final String isFoto;
					codigo = getContato.get(0);
					nome = getContato.get(1);
					tel = getContato.get(2);
					cel = getContato.get(3);
					email = getContato.get(4);
					isFoto = getContato.get(5);



					setContentView(R.layout.contato_selecionado);
					tv_contatoSelecionado_nome = (TextView) findViewById(R.contatoSelecionado.nome);
					tv_contatoSelecionado_codigo = (TextView) findViewById(R.contatoSelecionado.codigo);
					tv_contatoSelecionado_tel = (TextView) findViewById(R.contatoSelecionado.telefone);
					tv_contatoSelecionado_cel = (TextView) findViewById(R.contatoSelecionado.celular);
					tv_contatoSelecionado_email = (TextView) findViewById(R.contatoSelecionado.email);
					bt_contatoSelecionado_Editar = (Button) findViewById(R.contatoSelecionado.botao_editar);
					bt_contatoSelecionado_Excluir = (Button) findViewById(R.contatoSelecionado.botao_excluir);
					bt_contatoSelecionado_Voltar = (Button) findViewById(R.contatoSelecionado.botao_voltar);
					imageView = (ImageView) findViewById(R.ContatoSelecionado.foto);



					tv_contatoSelecionado_codigo.setText(codigo);
					tv_contatoSelecionado_nome.setText(nome);
					tv_contatoSelecionado_tel.setText(tel);
					tv_contatoSelecionado_cel.setText(cel);
					tv_contatoSelecionado_email.setText(email);

					Bitmap bm = null;
					File file = new File(ConstantsUtils.CAMINHO_FOTO+codigo+".jpg");
					if (file.exists()){
						bm = BitmapFactory.decodeFile(ConstantsUtils.CAMINHO_FOTO+codigo+".jpg");
						imageView.setImageBitmap(bm);
					} else {
						imageView.setImageResource(R.drawable.icon);
					}



					bt_contatoSelecionado_Editar.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							setContentView(R.layout.editar);
							final String codigo;
							final String nome;
							String tel, cel, email;

							bt_editar_cancelar = (Button) findViewById(R.editar.botao_cancelar);
							bt_editar_ok = (Button) findViewById(R.editar.botao_ok);
							ed_editar_nome = (EditText) findViewById(R.editar.ed_nome);
							ed_editar_tel = (EditText) findViewById(R.editar.ed_tel);
							ed_editar_cel = (EditText) findViewById(R.editar.ed_cel);
							ed_editar_email = (EditText) findViewById(R.editar.ed_email);
							bt_editar_carregar = (Button) findViewById(R.editar.foto_carregar);
							bt_editar_alterar = (Button) findViewById(R.editar.foto_alterar);
							bt_editar_excluir_foto = (Button) findViewById(R.editar.foto_excluir);
							ed_editar_foto = (EditText) findViewById(R.editar.campo_novafoto);
							imageAlter = (ImageView) findViewById(R.editar.foto);
							tv_editar_novafoto = (TextView) findViewById(R.editar.tv_novafoto);

							bt_editar_excluir_foto.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									alertas.showYesNoDialog("Apagar", "Deseja Realmente Excluir Foto?", AgendaTelefonicaBoaActivity.this, 
											new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											isFotoExcluida = true;	
											imageAlter.setImageResource(R.drawable.icon);
										}
									}, new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {


										}
									});

								}
							});

							if (isFoto.equals("true")){
								whitPic();
							} else {
								whithoutPic();
							}						

							bt_editar_cancelar.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									iniciaListaContatos();

								}
							});

							codigo = getContato.get(0);
							cod = codigo;
							nome = getContato.get(1);
							tel = getContato.get(2);
							cel = getContato.get(3);
							email = getContato.get(4);

							if (tel.equals("(N�o Cadastrado)")){
								tel = "";
							}
							if (cel.equals("(N�o Cadastrado)")){
								cel = "";
							}
							if (email.equals("(N�o Cadastrado)")){
								email = "";
							}

							ed_editar_nome.setText(nome);
							ed_editar_tel.setText(tel);
							ed_editar_cel.setText(cel);
							ed_editar_email.setText(email);

							bt_editar_ok.setOnClickListener(new View.OnClickListener() {

								

								@Override
								public void onClick(View v) {
									if (isFotoExcluida){
										File file = new File(ConstantsUtils.CAMINHO_FOTO+cod+".jpg");
										if (file.exists()){
											file.delete();
											imageAlter.setImageResource(R.drawable.icon);	
										}	
									}
									String newName, newTel, newCel, newEmail, newFoto;
									newName = ed_editar_nome.getText().toString();
									newTel = ed_editar_tel.getText().toString();
									newCel = ed_editar_cel.getText().toString();
									newEmail = ed_editar_email.getText().toString();

									if (newTel.equals("")){
										newTel = "(N�o Cadastrado)";
									}
									if (newCel.equals("")){
										newCel = "(N�o Cadastrado)";
									}
									if (newEmail.equals("")){
										newEmail = "(N�o Cadastrado)";
									}
									if (isNewFoto){
										ImagemUtils.downloadFromUrl(url, ConstantsUtils.CAMINHO_FOTO+codigo+".jpg");
										newFoto = "true";
									} else {
										newFoto = "false";
									}

									if (!nome.equals("")){
										if (!database.isNameExist4DifCodigo(newName, codigo)){
											if (!newTel.equals("(N�o Cadastrado)") || !newCel.equals("(N�o Cadastrado)") || !newEmail.equals("(N�o Cadastrado)")){
												database.updateContato(codigo, newName, newTel, newCel, newEmail, newFoto);												
												alertas.showSimpleDialog("Editado", "Contato Editado Com Sucesso!", AgendaTelefonicaBoaActivity.this);
												iniciaListaContatos();
											} else {
												alertas.showSimpleDialog("Contato", "Precisa ter ao menos um meio de contato!", AgendaTelefonicaBoaActivity.this);
											}
										} else {
											alertas.showSimpleDialog("Nome", "Este nome j� existe em sua lista!", AgendaTelefonicaBoaActivity.this);
										}

									}else {
										alertas.showSimpleDialog("Nome", "Digite um nome para o contato!", AgendaTelefonicaBoaActivity.this);
									}

								}
							});
						}
					});

					bt_contatoSelecionado_Voltar.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							iniciaListaContatos();						
						}
					});

					bt_contatoSelecionado_Excluir.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							alertas.showYesNoDialog("Excluir", "Deseja realmente Excluir "+contatoSelecionado+" da lista?", AgendaTelefonicaBoaActivity.this, 
									new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									database.ExcluirContato(Codigo);
									File file = new File(ConstantsUtils.CAMINHO_FOTO+Codigo+".jpg");
									if (file.exists()){
										file.delete();
									}
									iniciaListaContatos();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							});

						}
					});
				}

			});
		} else {
			alertas.showSimpleDialog("Nenhum Contato", "Voc� n�o tem nenhum contato cadastrado!", AgendaTelefonicaBoaActivity.this);
		}
	}

	public void whitPic(){
		imageAlter.setImageDrawable(imageView.getDrawable());
		tv_editar_novafoto.setVisibility(TextView.INVISIBLE);
		bt_editar_carregar.setVisibility(View.INVISIBLE);
		ed_editar_foto.setVisibility(View.INVISIBLE);

		bt_editar_alterar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isCancelButton){
					tv_editar_novafoto.setVisibility(TextView.VISIBLE);
					bt_editar_carregar.setVisibility(View.VISIBLE);
					ed_editar_foto.setVisibility(View.VISIBLE);
					bt_editar_alterar.setText("Cancelar");
					isCancelButton = true;		

					bt_editar_carregar.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							url = ed_editar_foto.getText().toString();

							if (!url.equals("")){
								estaEditando = true;
								new Progresso().execute(url);
							}	else {
								alertas.showSimpleDialog("Foto", "Digite um caminho da web para carregar a foto!", AgendaTelefonicaBoaActivity.this);
								isNewFoto = false;
								Bitmap bm = null;

								File file = new File(ConstantsUtils.CAMINHO_FOTO+cod+".jpg");

								if (file.exists()){
									bm = BitmapFactory.decodeFile(file.getPath());
									imageAlter.setImageBitmap(bm);
								} else {
									imageAlter.setImageResource(R.drawable.icon);	
								}


							}


						}
					});
				} else {
					ed_editar_foto.setText("");
					tv_editar_novafoto.setVisibility(TextView.INVISIBLE);
					bt_editar_carregar.setVisibility(View.INVISIBLE);
					ed_editar_foto.setVisibility(View.INVISIBLE);
					bt_editar_alterar.setText("Alterar");
					isNewFoto = false;
					isCancelButton = false;
					Bitmap bm = null;

					File file = new File(ConstantsUtils.CAMINHO_FOTO+cod+".jpg");
					if (file.exists()){
						bm = BitmapFactory.decodeFile(file.getPath());
						imageAlter.setImageBitmap(bm);
					} else {
						imageAlter.setImageResource(R.drawable.icon);	
					}


				}
			}
		});
	}
	public void whithoutPic(){
		tv_editar_novafoto.setVisibility(TextView.INVISIBLE);
		bt_editar_carregar.setVisibility(View.INVISIBLE);
		ed_editar_foto.setVisibility(View.INVISIBLE);
		bt_editar_excluir_foto.setVisibility(View.INVISIBLE);
		bt_editar_alterar.setText("Adicionar");

		bt_editar_alterar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isCancelButton){
					tv_editar_novafoto.setVisibility(TextView.VISIBLE);
					bt_editar_carregar.setVisibility(View.VISIBLE);
					ed_editar_foto.setVisibility(View.VISIBLE);		
					bt_editar_alterar.setText("Cancelar");
					isCancelButton = true;

					bt_editar_carregar.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							url = ed_editar_foto.getText().toString();

							if (!url.equals("")){
								estaEditando = true;
								new Progresso().execute(url);
							} else {
								alertas.showSimpleDialog("Foto", "Digite um caminho da web para carregar a foto!", AgendaTelefonicaBoaActivity.this);
								isNewFoto = false;
								Bitmap bm = null;

								File file = new File(ConstantsUtils.CAMINHO_FOTO+cod+".jpg");

								if (file.exists()){
									bm = BitmapFactory.decodeFile(file.getPath());
									imageAlter.setImageBitmap(bm);
								} else {
									imageAlter.setImageResource(R.drawable.icon);	
								}
							}
						}
					});
				} else {
					tv_editar_novafoto.setVisibility(TextView.INVISIBLE);
					bt_editar_carregar.setVisibility(View.INVISIBLE);
					ed_editar_foto.setVisibility(View.INVISIBLE);
					bt_editar_alterar.setText("Alterar");
					isCancelButton = false;
				}
			}
		});

		bt_editar_carregar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				url = ed_editar_foto.getText().toString();

				if (!url.equals("")){
					estaEditando = true;
					new Progresso().execute(url);
				}	else {
					alertas.showSimpleDialog("Foto", "Digite um caminho da web para carregar a foto!", AgendaTelefonicaBoaActivity.this);
					isNewFoto = false;
					Bitmap bm = null;

					File file = new File(ConstantsUtils.CAMINHO_FOTO+cod+".jpg");

					if (file.exists()){
						bm = BitmapFactory.decodeFile(file.getPath());
						imageAlter.setImageBitmap(bm);
					} else {
						imageAlter.setImageResource(R.drawable.icon);	
					}


				}


			}
		});
	}



	public class Progresso extends AsyncTask<String, String, String>{
		ProgressDialog pg;
		@Override
		protected void onPreExecute() {
			pg = new ProgressDialog(AgendaTelefonicaBoaActivity.this);
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
					alertas.showSimpleDialog("Foto", "URL Inv�lido!", AgendaTelefonicaBoaActivity.this);
				}	
			}else{
				if (setDrawable != null){
					imagemLoad.setImageDrawable(setDrawable);
					isFotoCarregada = true;
				} else {
					isFotoCarregada = false;
					imagemLoad.setImageResource(R.drawable.icon);
					alertas.showSimpleDialog("Foto", "URL Inv�lido!", AgendaTelefonicaBoaActivity.this);
				}	
			}
					
		}		
	}
}





