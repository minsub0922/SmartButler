package com.kau.smartbutler.controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.SeekBar
import com.kau.smartbutler.model.ProgressItem

class CustomProgressBar : SeekBar{
    constructor(context: Context) : super(context) {

    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {

    }
    var progressItemsList: ArrayList<ProgressItem> = ArrayList<ProgressItem>()
    fun initData(initProgressItemsList: ArrayList<ProgressItem>) {
        progressItemsList = initProgressItemsList
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (progressItemsList.size > 0) {
            val progressBarWidth = getWidth()
            val progressBarHeight = getHeight()
            val thumboffset = getThumbOffset()
            var lastProgressX = 0
            var progressItemWidth = 0
            var progressItemRight = 0
            for (i in 0 .. progressItemsList.size - 1) {
                val progressItem = progressItemsList.get(i);
                val progressPaint = Paint();
                progressPaint.color = getResources().getColor(progressItem.color)

                progressItemWidth = (progressItem.progressItemPercentage * progressBarWidth / 100).toInt();

                progressItemRight = lastProgressX + progressItemWidth;

                // for last item give right to progress item to the width
                if (i == progressItemsList.size - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                val progressRect = Rect();
                progressRect.set(lastProgressX, thumboffset / 2,
                        progressItemRight, progressBarHeight - thumboffset / 2);
                canvas.drawRect(progressRect, progressPaint);
                lastProgressX = progressItemRight;
            }
            super.onDraw(canvas);
        }
    }
}