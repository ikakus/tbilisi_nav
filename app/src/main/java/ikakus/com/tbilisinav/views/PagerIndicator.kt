package ikakus.com.tbilisinav.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import ikakus.com.tbilisinav.R


class PagerIndicator : LinearLayout {

    private var mIndicatorsCount: Int = 0
    private var mActiveDrawable: Int = 0
    private var mInactiveDrawable: Int = 0
    private var mCurrentActive: Int = 0
    private var mIndicators: Array<View>? = null
    private var mContext: Context? = null
    private var mSizePoint: Int = 0

    private var mIndicatorHorizontalMargin: Int = 0

    fun setIndicatorHorizontalMargin(indicatorHorizontalMargin: Int) {
        mIndicatorHorizontalMargin = indicatorHorizontalMargin
        addViews(mContext)
    }

    constructor(context: Context) : super(context) {
        initViews(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initViews(context, attrs)

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initViews(context, attrs)
    }

    fun setInactiveDrawable(inactiveDrawable: Int) {
        mInactiveDrawable = inactiveDrawable
        addViews(mContext)
    }

    fun setActiveDrawable(activeDrawable: Int) {
        mActiveDrawable = activeDrawable
        addViews(mContext)
    }

    fun setIndicatorsCount(indicatorsCount: Int) {
        mIndicatorsCount = indicatorsCount
        addViews(mContext)

    }

    private fun initViews(context: Context, attributeSet: AttributeSet?) {
        mContext = context
        if (attributeSet == null) {
            return
        }
        val styledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PagerIndicator, 0, 0)
        mIndicatorsCount = styledAttributes.getInteger(R.styleable.PagerIndicator_indicators, 0)
        mActiveDrawable = styledAttributes.getResourceId(R.styleable.PagerIndicator_activeDrawable, R.drawable.bullet_active)
        mInactiveDrawable = styledAttributes.getResourceId(R.styleable.PagerIndicator_inactiveDrawable, R.drawable.bullet_inactive)
        val defPointSize = resources.getDimensionPixelSize(R.dimen.sizePointIndicator)
        val defHorizontalMargin = resources.getDimensionPixelSize(R.dimen.marginPointIndicator)

        mSizePoint = styledAttributes.getDimensionPixelSize(R.styleable.PagerIndicator_indicatorPointSize, defPointSize)
        mIndicatorHorizontalMargin = styledAttributes.getDimensionPixelSize(R.styleable.PagerIndicator_indicatorHorizontalMargin, defHorizontalMargin)

        styledAttributes.recycle()

        addViews(context)
    }

    private fun addViews(context: Context?) {
        val params = LinearLayout.LayoutParams(mSizePoint, mSizePoint)
        params.setMargins(mIndicatorHorizontalMargin, 0, mIndicatorHorizontalMargin, 0)

        mIndicators = arrayOfNulls<View>(mIndicatorsCount) as Array<View>?

        mCurrentActive = 0
        removeAllViews()
        for (i in 0 until mIndicatorsCount) {
            val view = View(context)
            mIndicators!![i] = view
            addView(view, params)
        }
        update()
    }

    fun canNext(): Boolean {
        return mCurrentActive + 1 <= mIndicators!!.size - 1
    }

    fun canBack(): Boolean {
        return mCurrentActive - 1 >= 0
    }

    operator fun next() {
        mCurrentActive++
        update()
    }

    fun back() {
        mCurrentActive--
        update()
    }

    fun setPosition(position: Int) {
        mCurrentActive = position
        update()
    }

    private fun update() {
        for (i in mIndicators!!.indices) {
            mIndicators!![i].setBackgroundResource(if (i == mCurrentActive) mActiveDrawable else mInactiveDrawable)
        }
    }
}
