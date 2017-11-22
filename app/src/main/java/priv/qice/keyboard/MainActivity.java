package priv.qice.keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import priv.qice.keyboardlibrary.KeyboardUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etText = (EditText) findViewById(R.id.et_test);


        new KeyboardUtil(this).setEdit(etText).showKeyboard();
    }
}
