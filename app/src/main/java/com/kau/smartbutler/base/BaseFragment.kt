package com.kau.smartbutler.base
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kau.smartbutler.utils.fragmentTransitions.BusProvider

abstract class BaseFragment : Fragment() {
    abstract val layoutRes: Int
    open val isUseDataBinding: Boolean = false

    // onCreateView 단계에서 데이터 바인딩 연계처리
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutRes != 0) {
            return if (!isUseDataBinding) {
                inflater.inflate(layoutRes, container, false)
            } else {
                onDataBinding(inflater, container)
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    lateinit var mFragmentNavigation: FragmentNavigation

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
    }

    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment)
    }


    override fun onDestroyView() {
        BusProvider.instance.unregister(this)
        super.onDestroyView()

    }


    open fun onDataBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        return null
    }

    // inflate 된 뷰를 가지고 지지고 볶고 처리 진행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        subscribeUI()

        BusProvider.instance.register(this)
    }

    // onCreate 작업 설정 (꼭 안써도 됨, 안 쓸 경우 onViewCreated를 오버라이드 받고, super 호출해주고 사용하면 됨)
    open fun setupView(view: View) {

    }

    // 라이브데이터 쓸 경우 해당 함수 사용 (꼭 안써도 됨, 안 쓸 경우 onViewCreated를 오버라이드 받고, super 호출해주고 사용하면 됨)
    open fun subscribeUI() {

    }
}