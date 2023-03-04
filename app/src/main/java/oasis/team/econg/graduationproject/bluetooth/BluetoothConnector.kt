package oasis.team.econg.graduationproject.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import oasis.team.econg.graduationproject.MainActivity
import java.util.*


class BluetoothConnector(context: Context, textView: TextView) {
    val TAG = "BLUETOOTH"
    var main = context as MainActivity
    val REQUEST_ALL_PERMISSION = 1
    var results: IntArray = IntArray(2)
    private val REQUEST_ENABLE_BT = 1
    private val ERROR_READ = 0
    val dataView = textView
    var arduinoBTModule : BluetoothDevice? = null
    var arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    //val dataView = textView
    val permissions = arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN)
    var bluetoothManager: BluetoothManager? = getSystemService(main, BluetoothManager::class.java)
    var bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    var handler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                ERROR_READ ->
                    dataView.text = msg.obj.toString()
            }
        }
    }

    val connectToBTObservable: Observable<String> = Observable.create { emitter ->
        Log.d(TAG, "Calling connectThread class")
        //Call the constructor of the ConnectThread class
        //Passing the Arguments: an Object that represents the BT device,
        // the UUID and then the handler to update the UI
        val connectThread = ConnectThread(arduinoBTModule!!, arduinoUUID, handler)
        connectThread.run()
        //Check if Socket connected
        if (connectThread.getMmSocket()!!.isConnected) {
            Log.d(TAG, "Calling ConnectedThread class")
            //The pass the Open socket as arguments to call the constructor of ConnectedThread
            val connectedThread =
                ConnectedThread(connectThread.getMmSocket()!!)
            connectedThread.run()
            if (connectedThread.getValueRead() != null) {
                // If we have read a value from the Arduino
                // we call the onNext() function
                //This value will be observed by the observer
                emitter.onNext(connectedThread.getValueRead()!!)
            }
            //We just want to stream 1 value, so we close the BT stream
            connectedThread.cancel()
        }
        // SystemClock.sleep(5000); // simulate delay
        //Then we close the socket connection
        connectThread.cancel()
        //We could Override the onComplete function
        emitter.onComplete()
    }

    fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (main.let { ActivityCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun isBluetoothEnabled():Boolean{
        return if (!bluetoothAdapter!!.isEnabled) {
            Toast.makeText(main, "Bluetooth를 활성화 해 주세요.", Toast.LENGTH_SHORT).show()
            false
        }else{
            true
        }
    }

    fun requestPermissions(){
        ActivityCompat.requestPermissions(main,
            permissions, REQUEST_ALL_PERMISSION)
    }

    @SuppressLint("MissingPermission")
    fun searchDevice(){
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.d(TAG, "Device doesn't support Bluetooth")
        } else {
            Log.d(TAG, "Device support Bluetooth")
            //Check BT enabled. If disabled, we ask the user to enable BT
            if (!isBluetoothEnabled()) {
                Log.d(TAG, "Bluetooth is disabled")
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (!hasPermissions()) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d(TAG, "We don't BT Permissions")
                    ActivityCompat.requestPermissions(main, permissions, REQUEST_ENABLE_BT)
                    main.onRequestPermissionsResult(REQUEST_ENABLE_BT, permissions, results)
                    Log.d(TAG, "Bluetooth is enabled now")
                } else {
                    Log.d(TAG, "We have BT Permissions")
                    main.onRequestPermissionsResult(REQUEST_ENABLE_BT, permissions, results)
                    Log.d(TAG, "Bluetooth is enabled now")
                }

            } else {
                Log.d(TAG, "Bluetooth is enabled");
            }
            var btDevicesString=""
            val pairedDevices: MutableSet<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

            if (pairedDevices!!.size > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (device: BluetoothDevice in pairedDevices) {
                    var deviceName = device.name;
                    var deviceHardwareAddress = device.address; // MAC address
                    Log.d(TAG, "deviceName:$deviceName");
                    Log.d(TAG, "deviceHardwareAddress:$deviceHardwareAddress");
                    //We append all devices to a String that we will display in the UI
                    //btDevicesString=btDevicesString+deviceName+" || "+deviceHardwareAddress+"\n";
                    //If we find the HC 05 device (the Arduino BT module)
                    //We assign the device value to the Global variable BluetoothDevice
                    //We enable the button "Connect to HC 05 device"
                    if (deviceName == " MySensor") {
                        Log.d(TAG, "MySensor found");
                        arduinoUUID = device.uuids[0].uuid
                        arduinoBTModule = device
                        connectToDevice()
                        break
                        //HC -05 Found, enabling the button to read results
                        //connectToDevice.setEnabled(true);
                    }
                    //dataView.text = btDevicesString;
                }
            }
        }
        Log.d(TAG, "Button Pressed");
    }

    @SuppressLint("CheckResult")
    fun connectToDevice(){
        if (arduinoBTModule != null) {
            //We subscribe to the observable until the onComplete() is called
            //We also define control the thread management with
            // subscribeOn:  the thread in which you want to execute the action
            // observeOn: the thread in which you want to get the response
            connectToBTObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe { valueRead: String? ->
                    //valueRead returned by the onNext() from the Observable
                    dataView.text = valueRead
                }
        }
    }

    fun isBluetoothSupport(): Boolean{
        return if(bluetoothAdapter == null){
            Toast.makeText(main, "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show()
            false
        }else{
            true
        }
    }
}