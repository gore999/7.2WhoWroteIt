package rodriguezfernandez.carlos.whowroteit;

import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    // Base URL for Books API.
    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";
    static String getBookInfo(String queyString){
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String bookJSONString=null;
        //Creamos la URI
        Uri builtUri=Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,queyString)
                .appendQueryParameter(MAX_RESULTS,"10")
                .appendQueryParameter(PRINT_TYPE,"books").build();
        //Convertir URI en URL
        try {
            //Necesitamos un try catch porque el constructor de URL puede lanzar una "malformedException).
            URL requestURL=new URL(builtUri.toString());
            //Crear la conexion
            urlConnection=(HttpURLConnection) requestURL.openConnection(); // el urlConnection iniciado se crea a partir del requestURL
            urlConnection.setRequestMethod("GET");// Establecer el metodo de query
            urlConnection.connect();
            //Recibir la respuesta
            InputStream inputStream=urlConnection.getInputStream();
            //Meter en un BufferReader para facilitar la lectura.
            reader=new BufferedReader(new InputStreamReader(inputStream));
            //Con el stringbuilder podemos ir añadiendo cachos a la salida facilmente.
            StringBuilder builder=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
            //Si no hay respuesta devolvemos null.
            if (builder.length() == 0) {
                return null;
            }
            bookJSONString=builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //cerrar conexion si está abierta y cerrar el buffer si no es null.
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){//Cerrar el buffer puede dar una excepcion.
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG,bookJSONString);// Chivato de la consulta realizada a trabes del Logcat.
        return bookJSONString;
    }
}
