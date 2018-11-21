package com.example.user.demo.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.user.demo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by User on 01.02.2016.
 */
public class PilotFragment extends BaseFragment {
    private static final String TAG = "PilotFragment";


    boolean autoFlag=false;
    com.example.user.demo.MotionEvent event=null;

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;

    @Bind(R.id.auto_btn)
    Button autoBtn;

    @Bind(R.id.switchOn)
    Button btnSwitchOn;
    @Bind(R.id.switchOff)
    Button btnSwitchOff;
    @Bind(R.id.txtArduino)
    TextView txtArduino;

    //    추가 소스
    @Bind(R.id.ledonoff)
    ToggleButton ledonoffbtn;
//    @Bind(R.id.toggle_micbtn)
//    ToggleButton micbtn;

    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.web_progress)
    ProgressBar web_progress;

    //
    private static Handler h;
    private String macAddress;


    private static final int REQUEST_ENABLE_BT = 1;
    final int RECIEVE_MESSAGE = 1;        // status dlya Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();


    private ConnectedThread mConnectedThread;

    boolean tmp = true;



    // SPP UUID servis
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-addres for Bluetooth
    public static PilotFragment newInstance(String macAddress) {

        PilotFragment fragment = new PilotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mac_address", macAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null && this.getArguments().size() != 0) {
            this.macAddress = this.getArguments().getString("mac_address");


        }




    }

//    static String address = "98:D3:31:B3:02:C4";


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager=(SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        webView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                    if(tmp == true) {

                        webViewStart("http://192.168.2.12:8081/");
//                        webViewSatrt("https://youtu.be/bQ3XdhwHSA0");

                        tmp = !tmp;

                        Toast.makeText(getActivity(), "tmp : " + tmp, Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                        webViewStart("notExist");

                        tmp = !tmp;

                        Toast.makeText(getActivity(), "tmp : " + tmp, Toast.LENGTH_LONG).show();
                    }
                }

                return false;
            }
        });



        try {
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
                                txtArduino.setText("Android Bolajondan sms: " + sbprint);            // ��������� TextView
                                btnSwitchOn.setEnabled(true);
                                btnSwitchOff.setEnabled(true);
                            }
                            //Log.d(TAG, "...������:"+ sb.toString() +  "����:" + msg.arg1 + "...");
                            break;
                    }
                }

                ;
            };

            btAdapter = BluetoothAdapter.getDefaultAdapter();        // �������� ��������� Bluetooth �������
            checkBTState();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
                    Toast.LENGTH_SHORT).show();
        }

        autoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoFlag=!autoFlag;
                Toast.makeText(getContext(), "autoFlag - " + Boolean.toString(autoFlag), Toast.LENGTH_SHORT).show();

                if(autoFlag){

//
                    event=new com.example.user.demo.MotionEvent(getContext());
                    sensorEventListener=event;

                }else{
                    sensorManager.unregisterListener(sensorEventListener);
                }

            }
        });





    }

    void webViewStart(String inputUrl){



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);     //true일 경우 새창을 띄울때 사용
        // http://lsit81.tistory.com/entry/Android-WebView%EC%97%90%EC%84%9C-a-%ED%83%9C%EA%B7%B8%EC%9D%98-blank-target-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);



//        webView.loadUrl("http://192.168.2.12:8081");

        webView.setWebChromeClient(new WebChromeClient());



        webView.setWebViewClient(new WebViewClient() {




            //링크 클릭에 대한 반응
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                Log.e(TAG, "링크 클릭에 대한 반응");

                view.loadUrl(url);
                return true;
            }

            // 웹페이지 호출시 오류 발생에 대한 처리
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "오류 발생");

                Toast.makeText(getActivity(), "오류 : " + description, Toast.LENGTH_SHORT).show();
            }

            // 페이지 로딩 시작시 호출
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                Log.e(TAG, "페이지 로딩 시작시");
                web_progress.setVisibility(View.VISIBLE);
            }

            //페이지 로딩 종료시 호출
            @Override
            public void onPageFinished(WebView view, String url) {

                Log.e(TAG, "페이지 로딩 종료시");
                web_progress.setVisibility(View.GONE);
            }
        });


        webView.loadUrl(inputUrl);

    }


    @OnClick(R.id.switchOn)
    void switchOn() {
        try {//btnOff.setEnabled(false);
            mConnectedThread.write("m");    // ���������� ����� Bluetooth ����� 0
            //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.switchOff)
    void switchOff() {
        try {//btnOff.setEnabled(false);
            mConnectedThread.write("0");    // ���������� ����� Bluetooth ����� 0
            //Toast.makeText(getBaseContext(), "��������� LED", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //    추가 소스
    @OnClick(R.id.ledonoff)
    public void Ledonoff() {
        // TODO Auto-generated method stub
        if (ledonoffbtn.isChecked()) {
            try {
                mConnectedThread.write("3");
                Toast.makeText(getActivity(), "Chiroq yondi!",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            try {
                mConnectedThread.write("4");
                Toast.makeText(getActivity(), "Chiroq o`chdi!",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


//    @OnClick(R.id.toggle_micbtn)
//    public void Micsensor() {
//        // TODO Auto-generated method stub
//        if (micbtn.isChecked()) {
//            try {
//                mConnectedThread.write("ff");
//                Toast.makeText(getActivity(), "sound sensor ON!",
//                        Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            try {
//                mConnectedThread.write("0");
//                Toast.makeText(getActivity(), "sound sensor OFF!",
//                        Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
// ------------------------------------

    @Override
    protected int getLayout() {
        return R.layout.fragment_pilot;
    }

//    public void onEvent(String macAddress) {
//        if (macAddress != null) {
//            address = macAddress;
//
//        }
//    }


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
        Toast.makeText(getActivity(), title + " - " + message, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), "Iltimos bluetoothni yoqing !",
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
