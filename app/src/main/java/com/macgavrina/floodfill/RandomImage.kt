package com.macgavrina.floodfill

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

class RandomImage(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val defaultColor = Color.BLACK
    private var bitmap: Bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
    private var scale = 1f
    private var fillInProgress = false
    private var pointsQueue = ArrayDeque<PointWithColor>()
    private var checkedPointsList = mutableListOf<Boolean>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //canvas?.drawBitmap(bitmap, 0f, 0f, paint)

        val widthScale =  width.toFloat() / bitmap.width
        val heightScale = height.toFloat() / bitmap.height

        if (widthScale > heightScale) {
            scale = heightScale
        } else {
            scale = widthScale
        }

        val rect = Rect(0, 0, (scale*bitmap.width).toInt(), (scale*bitmap.height).toInt())
        canvas?.drawBitmap(bitmap, null, rect, null)
        //canvas?.drawBitmap(bitmap, null, Rect(0, 0, 256, 512), null)
    }

    private fun fillRecursive(pointWithColor: PointWithColor) {

        //if (pointWithColor.x >= bitmap.width || pointWithColor.x < 0 || pointWithColor.y >= bitmap.height || pointWithColor.y < 0) return

        if (bitmap.getPixel(pointWithColor.x, pointWithColor.y) != pointWithColor.colorId) return

        bitmap.setPixel(pointWithColor.x, pointWithColor.y, defaultColor)

        checkAndAddPointToQueue(PointWithColor(pointWithColor.x-1, pointWithColor.y, pointWithColor.colorId))
        checkAndAddPointToQueue(PointWithColor(pointWithColor.x+1, pointWithColor.y, pointWithColor.colorId))
        checkAndAddPointToQueue(PointWithColor(pointWithColor.x, pointWithColor.y-1, pointWithColor.colorId))
        checkAndAddPointToQueue(PointWithColor(pointWithColor.x, pointWithColor.y+1, pointWithColor.colorId))
    }

    private fun checkAndAddPointToQueue(point: PointWithColor) {
        if (point.x >= bitmap.width || point.x < 0 || point.y >= bitmap.height || point.y < 0) return

        if (checkedPointsList[bitmap.width * point.y + point.x]) return

        pointsQueue.add(point)
        checkedPointsList[bitmap.width * point.y + point.x] = true
    }


    fun onTouchElementByXY(x: Float, y: Float) {

        if (fillInProgress) return

        fillInProgress = true

        checkedPointsList = (List(bitmap.width * bitmap.height) {false}).toMutableList()

        if (x/scale >= bitmap.width || x < 0 || y/scale >= bitmap.height || y < 0) return
        val color = bitmap.getPixel((x/scale).toInt(),(y/scale).toInt())

        //ToDo Перенести вычисления из UI потока
        pointsQueue.add(PointWithColor((x/scale).toInt(), (y/scale).toInt(), color))



        while (pointsQueue.isNotEmpty()) {
            fillRecursive(pointsQueue.poll())
        }
        fillInProgress = false
        invalidate()
    }

    fun generateNewImage(width: Int, height: Int) {
//        //if(height>width)
//            val scaleH=height.toFloat()/height
//        //else
//            val scaleW=width.toFloat()/width
//
//        if (scaleH > scaleW) {
//            scale = scaleH
//        } else {
//            scale = scaleW
//        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        paint.color = defaultColor
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE


        val newCanvas = Canvas(bitmap)
        newCanvas.drawColor(Color.WHITE)

        for (i in 0 .. 10) {
            newCanvas.drawCircle((width * Math.random()).toFloat(), (height * Math.random()).toFloat(),
                (((width + height)/4) * Math.random()).toFloat(), paint)
        }

        invalidate()
    }
}