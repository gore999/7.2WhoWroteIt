package rodriguezfernandez.carlos.whowroteit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        new FetchBook(mTitleText,mAuthorText).execute(queryString);
    }
}
