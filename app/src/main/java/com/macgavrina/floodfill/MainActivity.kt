package com.macgavrina.floodfill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomImageView.setOnTouchListener { view, motionEvent ->
            Log.d("MyApp", "onTouch, x = ${motionEvent.x}, y = ${motionEvent.y}")
            randomImageView.onTouchElementByXY(motionEvent.x, motionEvent.y)
            false
        }

        generateNewImageButton.setOnClickListener {
            Log.d("MyApp", "onClick generate new image button, width = ${randomImageWidth.text}, " +
                    "height = ${randomImageHeight.text}")

            val newImageWidth = randomImageWidth.text.toString().toIntOrNull()
            val newImageHeight = randomImageHeight.text.toString().toIntOrNull()

            if (newImageHeight != null && newImageWidth != null && newImageHeight != 0 && newImageWidth != 0) {
                randomImageView.generateNewImage(newImageWidth, newImageHeight)
            } else {
                Toast.makeText(this, "Invalid size", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
