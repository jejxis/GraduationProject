package oasis.team.econg.graduationproject.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//https://popcorn16.tistory.com/192
class ConnectedThread(socket: BluetoothSocket): Thread() {
    private val TAG = "BLUETOOTH"
    private var mmSocket: BluetoothSocket = socket
    private var mmInStream: InputStream = socket.inputStream
    private var mmOutStream: OutputStream = socket.outputStream
    private var valueRead: String? = null

    fun getValueRead(): String? {
        return valueRead
    }

    override fun run() {
        val buffer = ByteArray(1024)
        var bytes = 0 // bytes returned from read()
        var numberOfReadings = 0 //to control the number of readings from the Arduino

        // Keep listening to the InputStream until an exception occurs.
        //We just want to get 1 temperature readings from the Arduino
        while (numberOfReadings < 1) {
            try {
                buffer[bytes] = mmInStream.read().toByte()
                var readMessage: String
                // If I detect a "\n" means I already read a full measurement
                if (buffer[bytes].toInt().toChar() == '\n') {
                    readMessage = String(buffer, 0, bytes)
                    Log.e(TAG, readMessage)
                    //Value to be read by the Observer streamed by the Obervable
                    valueRead = readMessage
                    bytes = 0
                    numberOfReadings++
                } else {
                    bytes++
                }
            } catch (e: IOException) {
                Log.d(TAG, "Input stream was disconnected", e)
                break
            }
        }
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }
    }
}