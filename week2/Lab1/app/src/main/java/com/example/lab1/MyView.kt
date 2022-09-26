package com.example.lab1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyView : View {
    var rect = Rect(10, 10, 110, 110)
    var color = Color.BLUE
    private var paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        canvas.drawRect(rect, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            println("{${event.x}, ${event.y}")
            rect.left = event.x.toInt()
            rect.top = event.y.toInt()
            rect.right = rect.left + 100
            rect.bottom = rect.top + 100
            invalidate()
            performClick() // 클릭 이벤트 발생

            return true // 꼭 해줘야 함!
        }
        return super.onTouchEvent(event)
    }
}