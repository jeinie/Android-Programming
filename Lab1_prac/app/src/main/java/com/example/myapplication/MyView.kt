package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

// 랜덤으로 번호 받아서
// drawRect, drawCircle 등 하면 될 듯
class MyView : View {
    var randomFigure = 0 // 랜덤 번호
    var color = Color.BLUE
    private var paint = Paint()

    var tx = 100F   //삼각형
    var ty = 100F
    var tw = 100F


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5F
        val rect = Rect(20, 20, 110, 110)

        when(randomFigure) {
            0 -> canvas.drawRect(rect, paint)
            1 -> canvas.drawCircle(650f, 650f, 50f, paint)
            2 -> {
                var hw = tw / 2
                var path = Path()
                path.moveTo(tx, ty - hw)
                path.lineTo(tx - hw, ty + hw)
                path.lineTo(tx + hw, ty + hw)
                path.lineTo(tx, ty - hw)
                path.close()
                canvas.drawPath(path, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            /*println("{${event.x}, ${event.y}")
            rect.left = event.x.toInt()
            rect.top = event.y.toInt()
            rect.right = rect.left + 100
            rect.bottom = rect.top + 100*/

            val random = Random()
            randomFigure = random.nextInt(3)
            Log.d("랜덤 숫자는 ", randomFigure.toString())

            invalidate()
            performClick() // 클릭 이벤트 발생

            return true // 꼭 해줘야 함!
        }

        return super.onTouchEvent(event)
    }
}