package rodriguezfernandez.carlos.whowroteit;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    public FetchBook(TextView mTitleText, TextView mAuthorText) {
        //Crear las referencias debiles a los elementos de la UI.
        this.mTitleText = new WeakReference<>(mTitleText);
        this.mAuthorText = new WeakReference<>(mAuthorText);

    }

    @Override
    protected String doInBackground(String... strings) {
        //Hacer la consulta a través de la clase NetworkUtils
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Convertir la respuesta en un objeto JSON
            JSONObject jsonObject = new JSONObject(s);
            // Extraer los "items" del libro en forma de JSONArray.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Iterar los resultados obtenidos.
            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                // Obtener los datos del libro.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Intentar capturar el titulo y el autor.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Iterar
                i++;
            }

            // If tenemos ambos resultados, los ponemos en el UI
            if (title != null && authors != null) {
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            } else {
                // Si no se logra, se muestran los mensajes de error.
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

        } catch (Exception e) {
            // Si la cadena recibida no es JSON, mostramos errores en el UI.
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
        }
    }
}

