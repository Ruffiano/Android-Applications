package com.example.user.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity
{
    private static final String TAG = "bluetooth2";

    Switch switch_sensor_fire;
//    Switch switch_sensor_gas;
    Switch switch_sensor_sound;
    Switch switch_auto_swing;
    SeekBar speed_controller;

    static boolean isTurnOnFireSensor = false;
//    static boolean isTurnOnGasSensor = true;
    static boolean isTurnOnSoundSensor = false;
    static boolean isTurnOnAutoSwing = false;
    static int swingSpeed = 1;



    private static Handler h;
    private String macAddress;


    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;        // status dlya Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;



    // SPP UUID servis
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        macAddress = intent.getExtras().getString("mac_address");

        Toast.makeText(this, "mac : " + macAddress,Toast.LENGTH_LONG).show();



        try{

            h = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case RECIEVE_MESSAGE:                                                    // ���� ������� ��������� � Handler
                            byte[] readBuf = (byte[]) msg.obj;
                            String strIncom = new String(readBuf, 0, msg.arg1);
                            sb.append(strIncom);                                                // ��������� ������
                            int endOfLineIndex = sb.indexOf("\r\n");                            // ���������� ������� ����� ������
                            if (endOfLineIndex > 0) {                                            // ���� ��������� ����� ������,
                                String sbprint = sb.substring(0, endOfLineIndex);                // �� ��������� ������
                                sb.delete(0, sb.length());                                        // � ������� sb
//                                txtArduino.setText("Android Bolajondan sms: " + sbprint);            // ��������� TextView
//                                btnSwitchOn.setEnabled(true);
//                                btnSwitchOff.setEnabled(true);
                            }
                            //Log.d(TAG, "...������:"+ sb.toString() +  "����:" + msg.arg1 + "...");
                            break;
                    }
                }

                ;
            };



            btAdapter = BluetoothAdapter.getDefaultAdapter();        // �������� ��������� Bluetooth �������
            checkBTState();
        }catch (Exception e) {
            Toast.makeText(this, "블루투스 연결 안되있음",
                    Toast.LENGTH_SHORT).show();
        }






        switch_sensor_fire = (Switch)findViewById(R.id.switch_sensor_fire1);
//        switch_sensor_gas = (Switch)findViewById(R.id.switch_sensor_gas1);
        switch_sensor_sound = (Switch)findViewById(R.id.switch_sensor_sound1);
        switch_auto_swing = (Switch)findViewById(R.id.switch_auto_swing);
        speed_controller = (SeekBar)findViewById(R.id.speed_controller);

        switch_sensor_fire.setChecked(isTurnOnFireSensor);
//        switch_sensor_gas.setChecked(isTurnOnGasSensor);
        switch_sensor_sound.setChecked(isTurnOnSoundSensor);
        switch_auto_swing.setChecked(isTurnOnAutoSwing);

        if( swingSpeed == 1 )
            speed_controller.setProgress(0);
        else if( swingSpeed == 2 )
            speed_controller.setProgress(50);
        else
            speed_controller.setProgress(100);

        switch_sensor_fire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Toast.makeText(getApplication(), "fire sensor : " + isChecked, Toast.LENGTH_SHORT).show();
                isTurnOnFireSensor = !isTurnOnFireSensor;

                if(isChecked == true){
                    try {//btnOff.setEnabled(false);
                        mConnectedThread.write("y");    // ���������� ����� Bluetooth ����� 0
                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {//btnOff.setEnabled(false);
                        mConnectedThread.write("w");    // ���������� ����� Bluetooth ����� 0
                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

//        switch_sensor_gas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                Toast.makeText(getApplication(), "gas sensor : " + isChecked, Toast.LENGTH_SHORT).show();
//                isTurnOnGasSensor = !isTurnOnGasSensor;
//
//                if(isChecked == true){
//                    try {//btnOff.setEnabled(false);
//                        mConnectedThread.write("k");    // ���������� ����� Bluetooth ����� 0
//                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    try {//btnOff.setEnabled(false);
//                        mConnectedThread.write("k");    // ���������� ����� Bluetooth ����� 0
//                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });

        switch_sensor_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplication(), "auto swing : " + isChecked, Toast.LENGTH_SHORT).show();
                isTurnOnSoundSensor = !isTurnOnSoundSensor;

                if(isChecked == true){
                    try {//btnOff.setEnabled(false);
                        mConnectedThread.write("f");    // ���������� ����� Bluetooth ����� 0
                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try {//btnOff.setEnabled(false);
                        mConnectedThread.write("q");    // ���������� ����� Bluetooth ����� 0
                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        switch_auto_swing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Toast.makeText(getApplication(), "notice : " + isChecked, Toast.LENGTH_SHORT).show();
                isTurnOnAutoSwing = !isTurnOnAutoSwing;

                if(isChecked == true){
                    try {//btnOff.setEnabled(false);
//                        mConnectedThread.write("f" + swingSpeed);    // ���������� ����� Bluetooth ����� 0
                        Toast.makeText(SettingsActivity.this, "f" + swingSpeed,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try {//btnOff.setEnabled(false);
                        mConnectedThread.write("q");    // ���������� ����� Bluetooth ����� 0
                        //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(SettingsActivity.this, "크래들과 연결 안되있음 !",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        speed_controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if( progress < 30 )
                {
                    seekBar.setProgress(0);
                    swingSpeed = 1;
                }
                else if( progress < 70)
                {
                    seekBar.setProgress(50);
                    swingSpeed = 2;
                }
                else
                {
                    seekBar.setProgress(100);
                    swingSpeed = 3;
                }
                Toast.makeText(getApplicationContext(), "swing speed : " + swingSpeed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - ������� ����������...");
        //String ad = btAdapter.getAddress();
        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        if (device != null) {

            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
                btSocket = null;
            }
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...�����������...");
        if (btSocket != null) {

            try {
                btSocket.connect();
                Log.d(TAG, "...���������� ����������� � ������ � �������� ������...");
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...�������� Socket...");


    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }


    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth �� ��������������");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth �������...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(this, title + " - " + message, Toast.LENGTH_LONG).show();
//        finish();
    }




    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // �������� ���-�� ���� � ���� �������� � �������� ������ "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();        // ���������� � ������� ��������� Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            try {
                Log.d(TAG, "...������ ��� ��������: " + message + "...");
                byte[] msgBuffer = message.getBytes();
                try {
                    mmOutStream.write(msgBuffer);
                } catch (IOException e) {
                    Log.d(TAG, "...������ �������� ������: " + e.getMessage() + "...");
                }
            } catch (Exception e) {
                Toast.makeText(SettingsActivity.this, "Iltimos bluetoothni yoqing !",
                        Toast.LENGTH_SHORT).show();
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }




}
