package com.example.user.demo.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.demo.R;
import com.example.user.demo.adapter.BlueAdapter;
import com.example.user.demo.models.BluetoothModel;
import com.example.user.demo.utils.DividerItemDecoration;
import com.example.user.demo.utils.Settings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by User on 01.03.2016.
 */
public class BluetoothListActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    Settings settings;
    List<BluetoothModel> list;

    BlueAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        init();
        eventBus = EventBus.getDefault();


    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null

    }


    private void init() {

        list = new ArrayList<BluetoothModel>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  // BluetoothAdapter는 폰의 블루투스 모듈을 사용하기 위한 오브젝트이다
                                                                                    //스태틱 메소드인 getBluetoothAdapter()를 호출해서 오브젝트의 디폴트 인스턴스를 가져온다.
                                                                                    // http://arsviator.blogspot.kr/2014/06/bluetooth-programming-in-android-12.html
        mBluetoothAdapter.startDiscovery();                                         // 이 메소드는 비동기 메소드이다.
                                                                                    // 즉 이 메소드를 호출하면 안드로이드에게 새 검색을 시작하라는 요청을 넘겨주고 바로 리턴한다.
                                                                                    // 새 디바이스가 발견되거나 discovery가 끝난걸 앱에게 알려주기 위해서는 BroadcastReceiver를 사용해야만 한다.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver, filter);

        if (mBluetoothAdapter == null) {
            //장치가 블루투스를 지원하지 않는 경우
        } else {
            //장치가 블루투스를 지원하는 경우
            if (mBluetoothAdapter.isEnabled()) {    //블루투스를 지원하고 활성 상태인 경우

                progressBar.setVisibility(View.VISIBLE);

            } else {                                // 블루투스를 지원하지만 비활성 상태인 경우
                                                    // 불루투스를 활성 상태로 바꾸기 위해 상용자 동의 요청
                Intent enableBtIntent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                progressBar.setVisibility(View.GONE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothModel bluetoothModel = new BluetoothModel(device.getAddress(), device.getName() != null ? device.getName() : "");
                list.add(bluetoothModel);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));

                bluetoothAdapter = new BlueAdapter(getApplicationContext(), list, itemClickListener);

                recyclerView.setAdapter(bluetoothAdapter);
            }
        }
    };

    BlueAdapter.ItemClickListener itemClickListener = new BlueAdapter.ItemClickListener() {
        @Override
        public void onItemClick(BluetoothModel model) {
            Intent intent = new Intent(BluetoothListActivity.this, MainActivity.class);
            intent.putExtra("address", model.getAddress());
            startActivity(intent);
            Toast.makeText(BluetoothListActivity.this, model.getAddress(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}
