package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner spinner1;
    Spinner spinner2;
    EditText output;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkInternet()){
            setContentView(R.layout.activity_main);

            spinner1= (Spinner) findViewById(R.id.spinner1);
            spinner2= (Spinner) findViewById(R.id.spinner2);


            // set spinner
            BUS_item bus_item=new BUS_item();
            List<String> list = bus_item.get_items();

            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adp1);
            ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adp2);
            //


        }
        else {
            Intent intent =new Intent(this,err.class);
            finish();
            startActivity(intent);
        }
    }

    Toast mToast=null;
    private void call_err()
    {

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this,"Please check your internet!",Toast.LENGTH_SHORT);
        mToast.show();

    }

    public void change(View v){

            int x=spinner1.getSelectedItemPosition();
            int y=spinner2.getSelectedItemPosition();

            spinner1.setSelection(y);
            spinner2.setSelection(x);
    }

    public void convert(View v)
    {
        if (checkInternet()==false)
            call_err();
        else {
            output = (EditText) findViewById(R.id.output);
            BUS_convert bus_convert = new BUS_convert();
            String fr1 = spinner1.getSelectedItem().toString();
            String to2 = spinner2.getSelectedItem().toString();
            input = (EditText) findViewById(R.id.input);

            if (input.getText().toString().equals("")) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(this,"Enter input",Toast.LENGTH_SHORT);
                mToast.show();
                return;
            }
            if (fr1.equals(to2)) {
                output.setText(input.getText().toString());
                return;
            }

            output.setText(bus_convert.getValue(fr1.substring(fr1.length() - 3, fr1.length()).toLowerCase(),
                    to2.substring(to2.length() - 3, to2.length()).toLowerCase(),
                    input.getText().toString()));
        }
    }

    private boolean checkInternet()
    {
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null)
            return false;
        if (!networkInfo.isConnected())
            return false;
        if (!networkInfo.isAvailable())
            return false;

        return true;
    }

}
