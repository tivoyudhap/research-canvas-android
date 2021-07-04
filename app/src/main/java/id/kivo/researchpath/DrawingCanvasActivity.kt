package id.kivo.researchpath

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.AsyncHttpResponse
import com.koushikdutta.async.http.WebSocket
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerRequestImpl
import com.koushikdutta.async.http.server.AsyncHttpServerResponseImpl
import kotlinx.android.synthetic.main.activity_drawing_canvas.*
import java.lang.Exception

class DrawingCanvasActivity : AppCompatActivity() {

    private lateinit var socket: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing_canvas)

        val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        val canvas = CanvasView(this)
        canvas.setListener {
//            socket.send(canvas.path)
        }
        canvas.layoutParams = layoutParams
        base.addView(canvas)

        drawingButton.setOnClickListener {
            Log.v("path", Gson().toJson(canvas.list).toString())
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("data", canvas.list)
            startActivity(intent)
        }

        connectSocket()
    }

    private fun connectSocket() {
        AsyncHttpClient.getDefaultInstance().websocket("ws://192.168.18.170:5000/ws", "ws", object : AsyncHttpClient.WebSocketConnectCallback {
            override fun onCompleted(ex: Exception?, webSocket: WebSocket?) {
                if (ex != null) {
                    Log.e("error", ex.localizedMessage ?: "")
                    return
                }

                webSocket?.let {
                    socket = it
                }
            }
        })
    }
}