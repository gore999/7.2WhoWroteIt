package rodriguezfernandez.carlos.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookInput=findViewById(R.id.bookInput);
        mTitleText=findViewById(R.id.titleText);
        mAuthorText=findViewById(R.id.authorText);
    }

    public void searchBooks(View view) {
        String queryString=mBookInput.getText().toString();
        //Recuperar  un ConnectivityManager.
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=null;
        //Si tenemos conexion....
        if (connMgr != null) {
            networkInfo=connMgr.getActiveNetworkInfo();//Recuperar la informacion de la misma.
        }
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {//Si tenemos info de la red, hay conexion y hay consulta...
            //hacemos la consulta asincrona.
            new FetchBook(mTitleText,mAuthorText).execute(queryString);
            //borramos los resultados y añadimos el loading....
            mAuthorText.setText("");
            mTitleText.setText(R.string.loading);
        }else{//Si se da alguno de los errores anteriores...
            mAuthorText.setText("");//el autor lo lo borramos.
            if (queryString.length() == 0) {
                mTitleText.setText(R.string.no_search_term);
            }else{
                mTitleText.setText(R.string.no_network);

            }
        }
        //Cerramos el teclado al pulsar el boton.
        InputMethodManager inputManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager!=null){
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
