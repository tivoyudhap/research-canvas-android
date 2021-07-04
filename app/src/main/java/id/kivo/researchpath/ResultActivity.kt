package id.kivo.researchpath

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        intent.getParcelableArrayListExtra<PathPointModel>("data")?.let {
//            resultImageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            val canvas = CanvasView(this)
            canvas.setDrawing(it)
            resultBase.addView(canvas)
        }
    }
}