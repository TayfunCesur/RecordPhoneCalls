package com.xionces.StoreCallRecords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Tayfun CESUR on 24.01.2016.
 */
public class State extends Fragment {


    ImageButton imgbtn;
    TextView state;
    SharedPreferences sharedpreferences;
    public static final String Prefs = "MyPrefs";
    public static final String State = "StateKey";
    String durum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab3.xml
        View view = inflater.inflate(R.layout.state, container, false);

        imgbtn = (ImageButton) view.findViewById(R.id.imageButton);
        state = (TextView) view.findViewById(R.id.textView);
        sharedpreferences = getActivity().getSharedPreferences(Prefs, Context.MODE_PRIVATE);
        getState();

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallDetectService.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                getState();

                if (durum.equals("false"))
                {
                    editor.putString(State, "Dinleniyor...");
                    state.setText("Dinleniyor...");
                    imgbtn.setImageResource(R.drawable.stop);
                    getActivity().startService(intent);
                }
                else
                {
                    if (!durum.equals("Dinleniyor..."))
                    {
                        editor.putString(State, "Dinleniyor...");
                        state.setText("Dinleniyor...");
                        imgbtn.setImageResource(R.drawable.stop);
                        getActivity().startService(intent);
                    }
                    else
                    {
                        editor.putString(State, "Dinlenmiyor...");
                        state.setText("Dinlenmiyor...");
                        imgbtn.setImageResource(R.drawable.microphone);
                        getActivity().stopService(intent);
                    }
                }
                editor.commit();

            }
        });


        return view;
    }



    private void getState()
    {
        if (sharedpreferences.contains(State))
        {
            state.setText(sharedpreferences.getString(State, ""));
            durum = state.getText().toString();
            if (durum.equals("Dinleniyor..."))
            {
                imgbtn.setImageResource(R.drawable.stop);
            }
            else
            {
                imgbtn.setImageResource(R.drawable.microphone);
            }
        }
        else
        {
            durum = "false";
            state.setText("Dinlenmiyor...");
            imgbtn.setImageResource(R.drawable.microphone);
        }
    }

}
