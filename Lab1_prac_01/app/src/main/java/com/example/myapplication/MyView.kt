package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyView : View {
    var rect = Rect(10, 10, 110, 110)
    var color = Color.BLUE
    private var currentFigure = ""
    //var currentFigure = ""  // 현재 도형이 무엇인지 저장
    private var paint = Paint()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setFigure(figure:String) {
        currentFigure = figure
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        paint.style = Paint.Style.FILL
        Log.d("onDraw()", "onDraw() 왔니?")

        //currentFigure = intent.getStringExtra("figure").toString()
        Log.d("currentFigure : ", "${currentFigure}")
        if (currentFigure == "rect") {
            canvas.drawRect(rect, paint)
        } else if (currentFigure == "circle") {
            canvas.drawCircle(650f, 650f, 50f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d("onTouchEvent()", "touchevent 왔나?")
            invalidate()
            performClick()

            return true
        }

        return super.onTouchEvent(event)
    }
}