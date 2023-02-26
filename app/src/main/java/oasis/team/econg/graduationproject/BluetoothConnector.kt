package oasis.team.econg.graduationproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class BluetoothConnector(context: Context) {
    var main = context as MainActivity
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mDevices: Set<BluetoothDevice>? = null
    private var bSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null
    private var mInputStream: InputStream? = null
    private var mRemoteDevice: BluetoothDevice? = null
    var onBluetooth = false
    var sendByte = ByteArray(4)
    var asyncDialog: ProgressDialog? = null
    private val REQUEST_ENABLE_BT = 1
    lateinit var BTSend : Thread

    fun startBluetooth(){
        checkActivation()

        BTSend = Thread(Runnable(){
            run(){
                try{
                    mOutputStream!!.write(sendByte)
                }catch (e: Exception){

                }
            }
        })

    }

    fun sendbtData(btLightPercent: Int) {
        throw IOException()
        val bytes = ByteArray(4)
        bytes[0] = 0xa5.toByte()
        bytes[1] = 0x5a.toByte()
        bytes[2] = 1 //command

        bytes[3] = btLightPercent as Byte
        sendByte = bytes
        BTSend.run()
    }

    fun checkActivation(){
        if(onBluetooth){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if(mBluetoothAdapter == null){
                Toast.makeText(main, "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            }else{
                if(!mBluetoothAdapter!!.isEnabled){
                    //var enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ActivityCompat.checkSelfPermission(
                            main,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                            main,
                            Manifest.permission.BLUETOOTH_ADMIN
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(main,
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADMIN), 1)
                    }
                    else {
                        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter!!.bondedDevices
                        if(pairedDevices.isNotEmpty()){
                            selectDevice()
                        }
                        else{
                            Toast.makeText(main, "먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
        else{
            try{
                BTSend.interrupt()
                mInputStream!!.close()
                mOutputStream!!.close()
                bSocket!!.close()
            }catch (e: Exception){
                throw e
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun selectDevice(){
        mDevices = mBluetoothAdapter!!.bondedDevices
        val mPairedDeviceCount = mDevices!!.size
        if(mPairedDeviceCount == 0){
            Toast.makeText(main,"장치를 페어링 해주세요!",Toast.LENGTH_SHORT).show()
        }

        val builder = AlertDialog.Builder(main)
        builder.setTitle("블루투스 장치 선택")

        val listItems = ArrayList<String>()
        for(device : BluetoothDevice in mDevices!!){
            listItems.add(device.name)
        }
        listItems.add("취소")

        val items: Array<CharSequence> = listItems as Array<CharSequence>

        builder.setItems(items, object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, item: Int) {
                if(item == mPairedDeviceCount){
                    main.finish()
                }
                else{
                    connectToSelectedDevice(items[item].toString())
                }
            }
        })

        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()
    }

    @SuppressLint("MissingPermission")
    fun connectToSelectedDevice(selectedDeviceName: String){
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName)

        asyncDialog = ProgressDialog(main)
        asyncDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        asyncDialog!!.setMessage("블루투스 연결 중..")
        asyncDialog!!.show()
        asyncDialog!!.setCancelable(false)

        val BTConnect = Thread( Runnable(){
                try{
                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
                    bSocket = mRemoteDevice!!.createRfcommSocketToServiceRecord(uuid)
                    bSocket!!.connect()
                    mOutputStream = bSocket!!.outputStream
                    mInputStream = bSocket!!.inputStream

                    main.runOnUiThread(Runnable() {
                        run(){
                            Toast.makeText(main,selectedDeviceName + " 연결 완료",Toast.LENGTH_LONG).show()
                            asyncDialog!!.dismiss()
                        }
                    })
                    onBluetooth = true
                }catch (e:Exception){
                    main.runOnUiThread(Runnable() {
                        run(){
                            asyncDialog!!.dismiss()
                            Toast.makeText(main,"블루투스 연결 오류",Toast.LENGTH_SHORT).show();
                        }
                    })
                }
            }
        )
        BTConnect.start()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceFromBondedList(name: String): BluetoothDevice {
        var selectedDevice: BluetoothDevice? = null

        mDevices!!.forEach {
            if(name == it.name){
                selectedDevice = it
                return@forEach
            }
        }
        return selectedDevice!!
    }
}