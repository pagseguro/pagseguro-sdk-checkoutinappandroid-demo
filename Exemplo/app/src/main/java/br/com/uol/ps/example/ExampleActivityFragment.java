package br.com.uol.ps.example;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import java.math.BigDecimal;

import br.com.uol.ps.library.business.PSCheckout;
import br.com.uol.ps.library.business.PSCheckoutConfig;
import br.com.uol.ps.library.business.PSCheckoutRequest;
import br.com.uol.ps.library.business.PagSeguroResponse;

/**
 * @author Universo Online Corp.
 *         Fragment contendo example com funções das Lib Checkout in app
 *         <p/>
 *         METODOS PRINCIPAIS - PSCheckout
 *         PSCheckout.init()
 *         PSCheckout.pay()
 *         PSCheckout.getMainCard()
 *         PSCheckout.getShowListCards()
 *         PSCheckout.logout()
 */
public class ExampleActivityFragment extends Fragment {

    private String TAG = "TAG";

    //Configuração Seller
    /**
     * Seller Email
     */
    private static final String SELLER_EMAIL = "<insira aqui seu e-mail pagseguro>";

    /**
     * Seller Token
     * Este token deve ser obtido no ibanking do PagSeguro
     * www.pagseguro.com.br
     * -> Mais informações consulte a documentação.
     */
    private static final String SELLER_TOKEN = "<insira aqui token de sua conta pagseguro>";


    private Button btnPay;
    private Button btnGetMainCard;
    private Button btnShowListCards;
    private Button btnLogout;
    private ImageView imgFlagCard;
    private TextView tvFinalCard;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);

        btnPay = (Button) view.findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(pay());
        btnGetMainCard = (Button) view.findViewById(R.id.btn_main_card);
        btnGetMainCard.setOnClickListener(getMainCard());
        btnShowListCards = (Button) view.findViewById(R.id.btn_show_list_cards);
        btnShowListCards.setOnClickListener(showListCards());
        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(logout());

        imgFlagCard = (ImageView) view.findViewById(R.id.img_flag_card);
        tvFinalCard = (TextView) view.findViewById(R.id.tv_final_card);
        //Cria instância da Library Checkout in App PagSeguro UOL
        configLib();

        return view;
    }

    /**
     * Metodo de "Exemplo" que inicializa a lib com as configurações necessarias.
     * obs: vide documentação para mais detalhes.
     */
    private void configLib() {
        //Inicialização a lib com parametros necessarios
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
        //Utilize production - para utilização em ambiente de produção
        psCheckoutConfig.setEnvironment(PSCheckout.Environment.PRODUCTION);
        //Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);

        PSCheckout.init(getActivity(), psCheckoutConfig);
    }

    /**
     * PSCheckout listerner
     * Este listener será o ponto focal para validar os resultados de sua transação;
     */
    PSCheckout.PSCheckoutListener psCheckoutListener = new PSCheckout.PSCheckoutListener() {
        @Override
        public void onSuccess(PagSeguroResponse pagSeguroResponse, Context context) {
            //Sucesso na transação
            stopLoading();
            Log.v(TAG, "Sucesso na transação");
            View view = LayoutInflater.from(getContext()).inflate(R.layout.payment_success, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("App Exemplo");
            alert.setView(view);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        @Override
        public void onFailure(PagSeguroResponse pagSeguroResponse, Context context) {
            //Falha na transação
            stopLoading();
            Log.v(TAG, "Falha na transação, " + " Erro: " + pagSeguroResponse.getErrorCode() + ", " + pagSeguroResponse.getMessage());
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("App Exemplo");
            alert.setMessage(pagSeguroResponse.getMessage() + "\n" + "Cod: " + pagSeguroResponse.getErrorCode());
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }

        @Override
        public void onProcessing(Context context) {
            //Listener para UI Thread
            //Ex. Dialogs, Loading, animações, etc;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Processando...");
            progressDialog.show();
        }
    };

    private void stopLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private View.OnClickListener pay() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Valor do produto / serviço
                BigDecimal amount = new BigDecimal(1.0);
                //quantidade de parcelas
                int quantityParcel = 1;
                //id do produto
                String productId = "001";
                //Descrição do produto
                String description = "Produto Exemplo";

                PSCheckoutRequest psCheckoutRequest =
                        new PSCheckoutRequest().withReferenceCode("123")
                                .withNewItem(description, String.valueOf(quantityParcel), amount, productId);

                PSCheckout.pay(psCheckoutRequest, psCheckoutListener);
            }
        };
    }

    private View.OnClickListener getMainCard() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* Obtem o cartão principal setado na lib, caso queira deixar visivel no App.
                 *
                 * @Return int ImgResource
                 * @Return String Final cartão
                 */
                if (PSCheckout.getMainCard() != null) {
                    imgFlagCard.setImageResource(PSCheckout.getMainCard().getImageResource());
                    tvFinalCard.setText(PSCheckout.getMainCard().getFinalCard());
                } else {
                    Snackbar.make(getView(), "Sem cartão principal no momento...", Snackbar.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener showListCards() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PSCheckout.showListCards();
            }
        };
    }

    private View.OnClickListener logout() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Faz Logout do usuario na libray Checkout in App
                PSCheckout.logout(getActivity());
                //"Your APP" does things
                //Aqui por exemplo como fizemos o logout estaremos limpando o cartão principal
                imgFlagCard.setImageResource(R.drawable.ic_credit_card_grey600_48dp);
                tvFinalCard.setText("Cartão");
                Snackbar.make(getView(), "Logout", Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        //Nesse exemplo colocamos o metodo de busca do cartão principal com o proposito de deixar a UI atualizada,
        //quando a lib PSCheckout voltar nas ações de pagamento
        //Isso pode ser necessario dependendo do que for preciso
        if (PSCheckout.getMainCard() != null) {
            imgFlagCard.setImageResource(PSCheckout.getMainCard().getImageResource());
            tvFinalCard.setText(PSCheckout.getMainCard().getFinalCard());
        }
    }
}