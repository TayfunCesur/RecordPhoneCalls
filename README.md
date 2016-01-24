<img src="http://i.hizliresim.com/zrM9Mj.png" align="left" />
# RecordPhoneCalls
> It's a simple application to record and save your incoming or outgoing phone calls.It also contains Read Contacts of user, basic ListView example, using Shared Preferences and Folder works in sd card.
<br><br><br>

###Screenshots
<p align="center">
<img src="http://i.hizliresim.com/7MWgJm.png"/>
</p>

###How it works

This app has some steps to make. I'll write this below with like I made.

## Step 1 : Shared Preferences

I used shared preferences to save user's choice. This choices may be Listen / Not Listen so Phone Listener service will work with respect to this choice/users choice.The method getState, checks users choice via shared preferences below.

    private void getState()
    {
        if (sharedpreferences.contains(State))
        {
            state.setText(sharedpreferences.getString(State, ""));
            durum = state.getText().toString();
            if (durum.equals("Listening..."))
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
            state.setText("Listening...");
            imgbtn.setImageResource(R.drawable.microphone);
        }
    }
User clicks listen/not listen button (First Screenshot), and via this choice CallListener service will work or stop.

                    if (!durum.equals("Listening..."))
                    {
                        editor.putString(State, "Listening...");
                        state.setText("Listening...");
                        imgbtn.setImageResource(R.drawable.stop);
                        ****getActivity().startService(intent)****;
                    }
                    else
                    {
                        editor.putString(State, "Not Listening...");
                        state.setText("Not Listening...");
                        imgbtn.setImageResource(R.drawable.microphone);
                        ****getActivity().stopService(intent)*****;
                    }

## Step 2 : Creating Call Detect Service

In my application, after service started , service call some class CallHelper which has PhoneStateListener class to listen  phone state. I considered three statement which is given below.

    private class CallStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    /*HERE! PHONE IS RINGING!*/
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    /*PHONE IDLE*/
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    /*PHONE CALL ANSWERED*/
                    break;
            }
        }
    }



