package oasis.team.econg.graduationproject.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread(device: BluetoothDevice, MY_UUID: UUID, handler: Handler): Thread() {
    @SuppressLint("MissingPermission")
    private var mmSocket: BluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
    private val TAG = "FrugalLogs"
    var handler: Handler = handler
    private val ERROR_READ = 0

    @SuppressLint("MissingPermission")
    override fun run() {
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket!!.connect()
        } catch (connectException: IOException) {
            // Unable to connect; close the socket and return.
            handler!!.obtainMessage(ERROR_READ, "Unable to connect to the BT device").sendToTarget()
            Log.e(TAG, "connectException: $connectException")
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
                Log.e(TAG, "Could not close the client socket", closeException)
            }
            return
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }

    fun getMmSocket(): BluetoothSocket? {
        return mmSocket
    }
}