package com.kau.smartbutler.utils.fragmentTransitions

import java.util.ArrayList

/**
 * Created by f22labs on 11/05/17.
 */

class FragmentHistory {


    private val stackArr: ArrayList<Int>


    val isEmpty: Boolean
        get() = stackArr.size == 0


    val stackSize: Int
        get() = stackArr.size

    /**
     * constructor to create stack with size
     *
     * @param
     */
    init {
        stackArr = ArrayList()


    }

    /**
     * This method adds new entry to the top
     * of the stack
     *
     * @param entry
     * @throws Exception
     */
    fun push(entry: Int) {

        if (isAlreadyExists(entry)) {
            return
        }
        stackArr.add(entry)

    }

    private fun isAlreadyExists(entry: Int): Boolean {
        return stackArr.contains(entry)
    }

    /**
     * This method removes an entry from the
     * top of the stack.
     *
     * @return
     * @throws Exception
     */
    fun pop(): Int {

        var entry = -1
        if (!isEmpty) {

            entry = stackArr[stackArr.size - 1]

            stackArr.removeAt(stackArr.size - 1)
        }
        return entry
    }


    /**
     * This method removes an entry from the
     * top of the stack.
     *
     * @return
     * @throws Exception
     */
    fun popPrevious(): Int {

        var entry = -1

        if (!isEmpty) {
            entry = stackArr[stackArr.size - 2]
            stackArr.removeAt(stackArr.size - 2)
        }
        return entry
    }


    /**
     * This method returns top of the stack
     * without removing it.
     *
     * @return
     */
    fun peek(): Int {
        return if (!isEmpty) {
            stackArr[stackArr.size - 1]
        } else -1

    }

    fun emptyStack() {

        stackArr.clear()
    }
}
