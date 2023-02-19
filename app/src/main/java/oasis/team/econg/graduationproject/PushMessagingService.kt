package oasis.team.econg.graduationproject

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class PushMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Log", "Refreshed token: $token");
    }
}