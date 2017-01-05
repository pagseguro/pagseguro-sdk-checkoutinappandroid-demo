package br.com.uol.ps.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.uol.ps.library.business.PSCheckout;

/**
 * @author Universo Online Corp.
 *         <p/>
 *         Checkout in App Example
 *         <p/>
 *         Todos os direitos reservados. O UOL é uma marca comercial do UNIVERSO ONLINE S / A.
 *         O logotipo do UOL é uma marca comercial do UNIVERSO ONLINE S / A.
 *         Outras marcas, nomes, logotipos e marcas são de propriedade de seus respectivos proprietários.
 *         As informações contidas neste documento pertencem ao UNIVERSO ONLINE S/A. Todos os direitos reservados.
 *         UNIVERSO ONLINE S/A. - Av. Faria Lima, 1384, 6º andar, São Paulo / SP, CEP 01452-002, Brasil.
 *         O serviço PagSeguro não é, nem pretende ser comparável a serviços financeiros oferecidos por instituições
 *         financeiras ou administradoras de cartões de crédito, consistindo apenas de uma forma de facilitar e monitorar
 *         a execução das transações de comércio electrónico através da gestão de pagamentos. Qualquer transação efetuada
 *         através do PagSeguro está sujeita e deve estar em conformidade com as leis da República Federativa do Brasil.
 *         Aconselhamos que você leia os termos e condições cuidadosamente.
 */
public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ExampleActivityFragment())
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Fornece controle para LIB de Activity results
        PSCheckout.onActivityResult(this, requestCode, resultCode, data);//Controle Lib Activity Life Cycle
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Android 6+ fornece controle para LIB para request de permissões
        PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults);//Controle Lib Activity Life Cycle
    }

    @Override
    public void onBackPressed() {
        if (PSCheckout.onBackPressed(this)) { //Controle Lib Button back
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PSCheckout.onHomeButtonPressed(this); //Controle Lib Home Button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PSCheckout.onDestroy(); //Controle Lib Activity Life Cycle
    }
}