package com.karczewski.procenty;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView totalTextView;
    EditText percentageTxt;
    EditText numberTxt;
    ShareActionProvider mShareActionProvider;


    // metoda trim() służy sprawdzeniu i usunięciu końcówki '.0'
    public String trim(float x) {
        String s = Float.toString(x);
        if (s.length() > 1 && s.charAt(s.length() - 1) == '0' && s.charAt(s.length() - 2) == '.') {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        totalTextView = (TextView) findViewById(R.id.totalTextView);
        percentageTxt = (EditText) findViewById(R.id.percentageTxt);
        numberTxt = (EditText) findViewById(R.id.numberTxt);


        Button calcBtn = (Button) findViewById(R.id.calcBtn);
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input1 = percentageTxt.getText().toString(); // Zmienna input1 służy walidacji wpisanych danych
                if (TextUtils.isEmpty(input1)) { // Sprawdza czy została wpisana jajakolwiek wartość w pole z procentami
                    Toast.makeText(getApplicationContext(), "Proszę wpisać wartość", Toast.LENGTH_SHORT).show();
                    return;
                }

                String input2 = numberTxt.getText().toString(); // Zmienna input2 służy walidacji wpisanych danych
                if (TextUtils.isEmpty(input2)) { // Sprawdza czy została wpisana jajakolwiek wartość w pole z liczbą
                    Toast.makeText(getApplicationContext(), "Proszę wpisać wartość", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Poniżej operacja obliczania oczekiwanej wartości (parsowanie z łańcucha na typ zmiennoprzecinkowy)
                float percentage = Float.parseFloat(percentageTxt.getText().toString());
                float dec = percentage / 100;
                float num = Float.parseFloat(numberTxt.getText().toString());
                float total = dec * num;
                // wyświetlenie wyniku po sprawdzeniu i usunięciu końcówki '.0'
                totalTextView.setText(trim(total));
                setShareIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }
        setShareIntent();
        return true;
    }

    private void setShareIntent() {
        String msgShare = "";
        if (mShareActionProvider != null) {
            msgShare = percentageTxt.getText() + "% z liczby " + numberTxt.getText() + " wynosi " + totalTextView.getText();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, msgShare);
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // akcje wykonywane z opcji menu
        // informacyjne okno dialogowe
        int id = item.getItemId();
        if (id == R.id.about) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.custom_dialog);

            TextView tvSampleText = (TextView) dialog.findViewById(R.id.custom_dialog_tv_text);
            tvSampleText.setText("\nProgramista: Maciej Karczewski\n\nWszelkie prawa zastrzeżone © 2017\n");

            Button btnDone = (Button) dialog.findViewById(R.id.custom_dialog_btn_done);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        // funkcja resetująca w menu
        if (id == R.id.reset) {
            totalTextView.setText("0");
            percentageTxt.setText(null);
            numberTxt.setText(null);
        }

        // funkcja wyjścia, opcja z menu
        if (id == R.id.exit) {
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
