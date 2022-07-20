package com.example.unlock

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import java.lang.StringBuilder
import kotlin.math.sqrt

class PicUnlockView : ViewGroup {
    var unlockListener: UnlockListener? = null
    var callback: ((String) -> Unit)? = null
    private val dotSize = dp2px(60)
    private var space = 0
    private var lineSize = 0
    private val dotViews = arrayListOf<ImageView>()
    private var lastSelectedDotView: ImageView? = null
    private val allSelectedDotViews = arrayListOf<ImageView>()
    private val allSelectedLineViews = arrayListOf<ImageView>()
    private val passwordBuilder = StringBuilder()
    private val allLineTags = listOf(
        12, 23, 45, 56, 78, 89,
        14, 25, 36, 47, 58, 69,
        24, 35, 57, 68,
        15, 26, 48, 59
    )

    constructor(context: Context) : super(context) {
        initUi()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initUi()
    }

    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {
        initUi()
    }

    private fun initUi() {
        //创建9个点
        initNineDot()
        //添加6条横线
        initLandscapeLine()
        //添加6条竖线
        initVerticalLine()
        //添加斜线
        initSlashLine()
        initLLine()
    }

    private fun initSlashLine() {
        //15 26
        //48 59
        var tag = 0
        for (i in 0..3) {
            ImageView(context).apply {
                setImageResource(R.drawable.line_right)
                if (i == 0) {
                    tag = 15
                } else {
                    tag += (i % 2) * 11 + (i + 1) % 2 * 22
                }
                this.tag = tag
                Log.d(TAG, "initSlashLine: tag = $tag")
                addView(this)
            }
        }
    }


    private fun layoutSlashLine() {
        for (row in 0..1) {
            for (column in 0..1) {
                val left = dotSize / 2 + sqrt(2.0) / 4 * dotSize + column * (dotSize + space)
                val top = dotSize / 2 + sqrt(2.0) * dotSize / 4 + row * (dotSize + space)
                val right = left + space + (dotSize / 2 - sqrt(2.0) * dotSize.toDouble() / 4) * 2
                val bottom = top + space + (dotSize / 2 - sqrt(2.0) * dotSize.toDouble() / 4) * 2

                //找到这根线在父容器中的索引
                val index = 21 + row * 2 + column
                val lineView = getChildAt(index)
                lineView.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                lineView.visibility = INVISIBLE
            }
        }
    }

    private fun initLLine() {
        //24 35
        //57 68
        var tag = 0
        for (i in 0..3) {
            ImageView(context).apply {
                setImageResource(R.drawable.line_left)
                if (i == 0) {
                    tag = 24
                } else {
                    tag += (i % 2) * 11 + (i + 1) % 2 * 22
                }
                this.tag = tag
                addView(this)
            }
        }
    }

    private fun layoutLLine() {
        for (row in 0..1) {
            for (column in 0..1) {
                val left = dotSize / 2 + sqrt(2.0) / 4 * dotSize + column * (dotSize + space)
                val top = dotSize / 2 + sqrt(2.0) * dotSize / 4 + row * (dotSize + space)
                val right = left + space + (dotSize / 2 - sqrt(2.0) * dotSize.toDouble() / 4) * 2
                val bottom = top + space + (dotSize / 2 - sqrt(2.0) * dotSize.toDouble() / 4) * 2

                //找到这根线在父容器中的索引
                val index = 25 + row * 2 + column
                val lineView = getChildAt(index)
                lineView.layout(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                lineView.visibility = INVISIBLE
            }
        }
    }

    private fun initVerticalLine() {
        //14 25 36
        //47 58 69
        var tag = 0
        for (i in 0..5) {
            ImageView(context).apply {
                setImageResource(R.drawable.line_vertical)
                if (i == 0) {
                    tag = 14
                } else {
                    tag += 11
                }

                this.tag = tag
                addView(this)
            }
        }
    }


    private fun initLandscapeLine() {
        // 12 23
        // 45 56
        // 78 89
        var tag = 0
        for (i in 0..5) {
            ImageView(context).apply {
                setImageResource(R.drawable.line_horizontal)
                if (i == 0) {
                    tag = 12
                } else {
                    tag += i % 2 * 11 + (i + 1) % 2 * 22
                }
                this.tag = tag
                Log.d(TAG, "tag: $tag")
                Log.d(TAG, "initLandscapeLine: ")
                addView(this)
            }
        }
    }

    private fun layoutVerticalLine() {
        for (row in 0..1) {
            for (column in 0..2) {
                val left = dotSize / 2 + column * (dotSize + space)
                val top = dotSize + row * (dotSize + space)
                val right = left + dp2px(2)
                val bottom = top + space

                //找到这根线在父容器中的索引
                val index = 15 + row * 3 + column
                val lineView = getChildAt(index)
                lineView.layout(left, top, right, bottom)
                lineView.visibility = INVISIBLE
            }
        }
    }

    private fun layoutHorizontalLine() {
        for (row in 0..2) {
            for (column in 0..1) {
                val left = dotSize + column * (dotSize + space)
                val top = dotSize / 2 + row * lineSize
                val right = left + space
                val bottom = top + dp2px(2)

                //找到这根线在父容器中的索引
                val index = 9 + row * 2 + column
                val lineView = getChildAt(index)
                lineView.layout(left, top, right, bottom)
                lineView.visibility = INVISIBLE
            }
        }
    }

    private fun initNineDot() {
        for (i in 1..9) {
            ImageView(context).apply {
                setImageResource(R.drawable.dot_normal)
                tag = "$i"
                addView(this)
                dotViews.add(this)
            }
        }
    }

    private fun layoutNineDot() {
        for (row in 0..2) {
            for (column in 0..2) {
                val left = column * (dotSize + space)
                val top = row * (dotSize + space)
                val right = left + dotSize
                val bottom = top + dotSize
                val index = row * 3 + column
                val dotView = getChildAt(index)
                dotView.layout(left, top, right, bottom)
            }
        }
    }

    //已测量父容器的宽高
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //计算两个点之间的间距
        space = (width - 3 * dotSize) / 2
        lineSize = dotSize + space
    }

    //定义自己的规则
    //这个方法里面不要大量创建和运算
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        layoutNineDot()
        layoutHorizontalLine()
        layoutVerticalLine()
        layoutSlashLine()
        layoutLLine()
    }

    private fun dp2px(dp: Int) = (dp * context.resources.displayMetrics.density).toInt()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dealWithTouchPoint(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                dealWithTouchPoint(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                dealWithResult2()
            }
        }
        return true
    }

    private fun dealWithResult() {
        if (unlockListener != null) {
            //通过接口的方法传递数据出去
            unlockListener!!.passwordPicDidFinished(passwordBuilder.toString())
        }
    }

    private fun dealWithResult2() {
        callback?.let {
            it(passwordBuilder.toString())
        }
    }

    private fun dealWithTouchPoint(x: Float, y: Float) {
        dotViews.forEach {
            val rect = RectF(
                it.x,
                it.y,
                it.x + it.width,
                it.y + it.height
            )
            if (rect.contains(x, y)) {
                //判断是不是第一个点
                if (lastSelectedDotView == null) {
                    //直接点亮
                    changeSelectedDotViewStatus(it, ViewStatus.SELECTED)
                } else {
                    val lastTag = (lastSelectedDotView!!.tag as String).toInt()
                    val currentTag = (it.tag as String).toInt()
                    //获取两点间线的tag
                    val lineTag = if (lastTag < currentTag)
                        lastTag * 10 + currentTag
                    else
                        currentTag * 10 + lastTag
                    //判断tags数组中是否有这个值
                    if (allLineTags.contains(lineTag)) {
                        //存在这条线，使用tag将它拿到
                        val lineView = findViewWithTag<ImageView>(lineTag)
                        if (!allSelectedDotViews.contains(it)) {
                            //点亮点
                            changeSelectedDotViewStatus(it, ViewStatus.SELECTED)
                            if (!allSelectedLineViews.contains(lineView)) {
                                //点亮线
                                changeSelectedLineStatus(lineView, ViewStatus.SELECTED)
                            }
                        }
                    }
                }
            }
        }
    }

    //切换点的状态
    private fun changeSelectedDotViewStatus(imageView: ImageView, status: ViewStatus) {
        if (status == ViewStatus.NORMAL) {
            imageView.setImageResource(R.drawable.dot_normal)
        } else {
            imageView.setImageResource(R.drawable.dot_selected)
            lastSelectedDotView = imageView
            allSelectedDotViews.add(imageView)
            passwordBuilder.append(imageView.tag as String)
        }
    }

    //切换线的状态
    private fun changeSelectedLineStatus(line: ImageView, status: ViewStatus) {
        if (status == ViewStatus.NORMAL) {
            line.visibility = INVISIBLE
        } else {
            line.visibility = VISIBLE
            allSelectedLineViews.add(line)
        }
    }

    enum class ViewStatus {
        NORMAL, SELECTED
    }

    interface UnlockListener {
        fun passwordPicDidFinished(password: String)
    }
}