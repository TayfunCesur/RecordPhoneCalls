package com.xionces.StoreCallRecords;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Tayfun CESUR on 23.01.2016.
 */
public class CallHelper {

    String outputfilePath;
    private MediaRecorder myAudioRecorder;
    String FileName;
    String NameofNum;
    String NumberToSearch = "";
    public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/GorusmeKayitlari";

    private class CallStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    prepareFileName(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (myAudioRecorder != null)
                    {
                        MakeNotification(false);
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    MakeNotification(true);
                    record();
                    break;
            }
        }


    }

    public class OutgoingReceiver extends BroadcastReceiver {
        public OutgoingReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            NumberToSearch = number;
            prepareFileName(NumberToSearch);

        }

    }

    private Context ctx;
    private TelephonyManager tm;
    private CallStateListener callStateListener;

    private OutgoingReceiver outgoingReceiver;

    public CallHelper(Context ctx) {
        this.ctx = ctx;
        callStateListener = new CallStateListener();
        outgoingReceiver = new OutgoingReceiver();
    }


    public void start() {
        tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        ctx.registerReceiver(outgoingReceiver, intentFilter);
    }

    public void stop() {
        tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
        ctx.unregisterReceiver(outgoingReceiver);
    }

    private void record()
    {
        File dir = new File(PATH);
        if (!dir.exists())
        {
            boolean success = true;
            if (!dir.exists()) {
                success = dir.mkdir();
            }
            if (success) {
                outputfilePath = PATH + FileName;
            } else {
                // Do something else on failure
            }
        }
        else
        {
            outputfilePath = PATH + FileName;
        }

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        myAudioRecorder.setOutputFile(outputfilePath);
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void prepareFileName(String incoming)
    {
        if (incoming.length() >= 11)
        {
            getContactList(incoming);
        }
        else
        {
            NameofNum = incoming;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS");
        String date = sdf.format(new Date());
        FileName ="/"+ NameofNum + "|" + date + ".3gpp";
    }

    public String getContactList(String num){
        ArrayList<Person> contactList = new ArrayList<Person>();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;
        String[] PROJECTION = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
        };
        String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
        Cursor contacts = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, null);


        if (contacts.getCount() > 0)
        {
            while(contacts.moveToNext()) {
                Person aContact = new Person();
                int idFieldColumnIndex = 0;
                int nameFieldColumnIndex = 0;
                int numberFieldColumnIndex = 0;

                String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

                nameFieldColumnIndex = contacts.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                if (nameFieldColumnIndex > -1)
                {
                    aContact.setName(contacts.getString(nameFieldColumnIndex));
                }

                PROJECTION = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
                final Cursor phone = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                if(phone.moveToFirst()) {
                    while(!phone.isAfterLast())
                    {
                        numberFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        if (numberFieldColumnIndex > -1)
                        {
                            aContact.setPhoneNum(phone.getString(numberFieldColumnIndex));
                            phone.moveToNext();
                            TelephonyManager mTelephonyMgr;
                            mTelephonyMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                            if (!mTelephonyMgr.getLine1Number().contains(aContact.getPhoneNum()))
                            {
                                contactList.add(aContact);
                            }
                        }
                    }
                }
                phone.close();
            }

            contacts.close();
        }

        for (int i = 0;i<contactList.size();i++)
        {
            if (contactList.get(i).getPhoneNum().equals(num))
            {
                NameofNum = contactList.get(i).getName();
            }
        }
        return  NameofNum;
    }


    public void MakeNotification(Boolean status)
    {
        String title;
        if (status)
        {
            title = "Kaydediliyor...";
        }
        else
        {
            title = "Kayıt başarılı.";
        }
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.microphone, title, System.currentTimeMillis());
        Intent notificationIntent = new Intent(ctx, MyActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);

        notification.setLatestEventInfo(ctx, "Görüşme Kaydı", title, contentIntent);
        if (status)
        {
            mNotificationManager.notify(1, notification);
        }
        else
        {
            mNotificationManager.notify(1, notification);
        }
    }




}