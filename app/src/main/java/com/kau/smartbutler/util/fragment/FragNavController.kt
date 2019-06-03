package com.kau.smartbutler.util.fragment

import android.os.Bundle

import org.json.JSONArray

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.ArrayList
import java.util.Stack

import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * The class is used to manage navigation through multiple stacks of fragments, as well as coordinate
 * fragments that may appear on screen
 *
 *
 * https://github.com/ncapdevi/FragNav
 * Nic Capdevila
 * Nic.Capdevila@gmail.com
 *
 *
 * Originally Created March 2016
 */
class FragNavController
//region Construction and setup

private constructor(builder: Builder, savedInstanceState: Bundle?) {

    @IdRes
    private val mContainerId: Int
    private val mFragmentStacks: MutableList<Stack<Fragment>>
    private val mFragmentManager: FragmentManager
    private val mDefaultTransactionOptions: FragNavTransactionOptions?
    /**
     * Get the index of the current stack that is being displayed
     *
     * @return Current stack index
     */
    @TabIndex
    @get:CheckResult
    @get:TabIndex
    var currentStackIndex: Int = 0
        private set
    private var mTagCount: Int = 0
    private var mCurrentFrag: Fragment? = null
    private var mCurrentDialogFrag: DialogFragment? = null
    private val mRootFragmentListener: RootFragmentListener?
    private val mTransactionListener: TransactionListener?
    private var mExecutingTransaction: Boolean = false

    /**
     * @return Current DialogFragment being displayed. Null if none
     */
    //Else try to find one in the FragmentManager
    val currentDialogFrag: DialogFragment?
        @CheckResult
        get() {
            if (mCurrentDialogFrag != null) {
                return mCurrentDialogFrag
            } else {
                val fragmentManager: FragmentManager
                if (mCurrentFrag != null) {
                    fragmentManager = mCurrentFrag!!.childFragmentManager
                } else {
                    fragmentManager = mFragmentManager
                }
                if (fragmentManager.fragments != null) {
                    for (fragment in fragmentManager.fragments) {
                        if (fragment is DialogFragment) {
                            mCurrentDialogFrag = fragment
                            break
                        }
                    }
                }
            }
            return mCurrentDialogFrag
        }

    /**
     * Helper function to attempt to get current fragment
     *
     * @return Fragment the current frag to be returned
     */
    //Attempt to used stored current fragment
    //if not, try to pull it from the stack
    val currentFrag: Fragment?
        @CheckResult
        get() {
            if (mCurrentFrag != null) {
                return mCurrentFrag
            } else if (currentStackIndex == NO_TAB) {
                return null
            } else {
                val fragmentStack = mFragmentStacks[currentStackIndex]
                if (!fragmentStack.isEmpty()) {
                    mCurrentFrag = mFragmentManager.findFragmentByTag(mFragmentStacks[currentStackIndex].peek().tag)
                }
            }
            return mCurrentFrag
        }

    //endregion

    //region Public helper functions

    /**
     * Get the number of fragment stacks
     *
     * @return the number of fragment stacks
     */
    val size: Int
        @CheckResult
        get() = mFragmentStacks.size


    /**
     * Get a copy of the current stack that is being displayed
     *
     * @return Current stack
     */
    val currentStack: Stack<Fragment>?
        @CheckResult
        get() = getStack(currentStackIndex)

    /**
     * @return If true, you are at the bottom of the stack
     * (Consider using replaceFragment if you need to change the root fragment for some reason)
     * else you can popFragment as needed as your are not at the root
     */

    val isRootFragment: Boolean
        @CheckResult
        get() {
            val stack = currentStack

            return stack == null || stack.size == 1
        }

    init {
        mFragmentManager = builder.mFragmentManager
        mContainerId = builder.mContainerId
        mFragmentStacks = ArrayList(builder.mNumberOfTabs)
        mRootFragmentListener = builder.mRootFragmentListener
        mTransactionListener = builder.mTransactionListener
        mDefaultTransactionOptions = builder.mDefaultTransactionOptions
        currentStackIndex = builder.mSelectedTabIndex

        //Attempt to restore from bundle, if not, initialize
        if (!restoreFromBundle(savedInstanceState, builder.mRootFragments)) {

            for (i in 0 until builder.mNumberOfTabs) {
                val stack = Stack<Fragment>()
                if (builder.mRootFragments != null) {
                    stack.add(builder.mRootFragments!![i])
                }
                mFragmentStacks.add(stack)
            }

            initialize(builder.mSelectedTabIndex)
        }
    }

    //endregion

    //region Transactions

    /**
     * Function used to switch to the specified fragment stack
     *
     * @param index              The given index to switch to
     * @param transactionOptions Transaction options to be displayed
     * @throws IndexOutOfBoundsException Thrown if trying to switch to an index outside given range
     */
    @Throws(IndexOutOfBoundsException::class)
    @JvmOverloads
    fun switchTab(@TabIndex index: Int, transactionOptions: FragNavTransactionOptions? = null) {
        //Check to make sure the tab is within range
        if (index >= mFragmentStacks.size) {
            throw IndexOutOfBoundsException("Can't switch to a tab that hasn't been initialized, " +
                    "Index : " + index + ", current stack size : " + mFragmentStacks.size +
                    ". Make sure to create all of the tabs you need in the Constructor or provide a way for them to be created via RootFragmentListener.")
        }
        if (currentStackIndex != index) {
            currentStackIndex = index

            val ft = createTransactionWithOptions(transactionOptions)

            detachCurrentFragment(ft)

            var fragment: Fragment? = null
            if (index == NO_TAB) {
                ft.commit()
            } else {
                //Attempt to reattach previous fragment
                fragment = reattachPreviousFragment(ft)
                if (fragment != null) {
                    ft.commit()
                } else {
                    fragment = getRootFragment(currentStackIndex)
                    ft.add(mContainerId, fragment, generateTag(fragment))
                    ft.commit()
                }
            }

            executePendingTransactions()

            mCurrentFrag = fragment
            mTransactionListener?.onTabTransaction(mCurrentFrag, currentStackIndex)
        }
    }

    /**
     * Push a fragment onto the current stack
     *
     * @param fragment           The fragment that is to be pushed
     * @param transactionOptions Transaction options to be displayed
     */
    @JvmOverloads
    fun pushFragment(fragment: Fragment?, transactionOptions: FragNavTransactionOptions? = null) {
        if (fragment != null && currentStackIndex != NO_TAB) {
            val ft = createTransactionWithOptions(transactionOptions)

            detachCurrentFragment(ft)
            ft.add(mContainerId, fragment, generateTag(fragment))
            ft.commit()

            executePendingTransactions()

            mFragmentStacks[currentStackIndex].push(fragment)

            mCurrentFrag = fragment
            mTransactionListener?.onFragmentTransaction(mCurrentFrag, TransactionType.PUSH)

        }
    }

    /**
     * Pop the current fragment from the current tab
     *
     * @param transactionOptions Transaction options to be displayed
     */
    @Throws(UnsupportedOperationException::class)
    @JvmOverloads
    fun popFragment(transactionOptions: FragNavTransactionOptions? = null) {
        popFragments(1, transactionOptions)
    }

    /**
     * Pop the current stack until a given tag is found. If the tag is not found, the stack will popFragment until it is at
     * the root fragment
     *
     * @param transactionOptions Transaction options to be displayed
     */
    @Throws(UnsupportedOperationException::class)
    @JvmOverloads
    fun popFragments(popDepth: Int, transactionOptions: FragNavTransactionOptions? = null) {
        if (isRootFragment) {
            throw UnsupportedOperationException(
                    "You can not popFragment the rootFragment. If you need to change this fragment, use replaceFragment(fragment)")
        } else if (popDepth < 1) {
            throw UnsupportedOperationException("popFragments parameter needs to be greater than 0")
        } else if (currentStackIndex == NO_TAB) {
            throw UnsupportedOperationException("You can not pop fragments when no tab is selected")
        }

        //If our popDepth is big enough that it would just clear the stack, then call that.
        if (popDepth >= mFragmentStacks[currentStackIndex].size - 1) {
            clearStack(transactionOptions)
            return
        }

        var fragment: Fragment?
        val ft = createTransactionWithOptions(transactionOptions)

        //Pop the number of the fragments on the stack and remove them from the FragmentManager
        for (i in 0 until popDepth) {
            fragment = mFragmentManager.findFragmentByTag(mFragmentStacks[currentStackIndex].pop().tag)
            if (fragment != null) {
                ft.remove(fragment)
            }
        }

        //Attempt to reattach previous fragment
        fragment = reattachPreviousFragment(ft)

        var bShouldPush = false
        //If we can't reattach, either pull from the stack, or create a new root fragment
        if (fragment != null) {
            ft.commit()
        } else {
            if (!mFragmentStacks[currentStackIndex].isEmpty()) {
                fragment = mFragmentStacks[currentStackIndex].peek()
                ft.add(mContainerId, fragment!!, fragment.tag)
                ft.commit()
            } else {
                fragment = getRootFragment(currentStackIndex)
                ft.add(mContainerId, fragment, generateTag(fragment))
                ft.commit()

                bShouldPush = true
            }
        }

        executePendingTransactions()

        //Need to have this down here so that that tag has been
        // committed to the fragment before we add to the stack
        if (bShouldPush) {
            mFragmentStacks[currentStackIndex].push(fragment)
        }

        mCurrentFrag = fragment
        mTransactionListener?.onFragmentTransaction(mCurrentFrag, TransactionType.POP)
    }

    /**
     * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment
     *
     * @param transactionOptions Transaction options to be displayed
     */
    @JvmOverloads
    fun clearStack(transactionOptions: FragNavTransactionOptions? = null) {
        if (currentStackIndex == NO_TAB) {
            return
        }

        //Grab Current stack
        val fragmentStack = mFragmentStacks[currentStackIndex]

        // Only need to start popping and reattach if the stack is greater than 1
        if (fragmentStack.size > 1) {
            var fragment: Fragment?
            val ft = createTransactionWithOptions(transactionOptions)

            //Pop all of the fragments on the stack and remove them from the FragmentManager
            while (fragmentStack.size > 1) {
                fragment = mFragmentManager.findFragmentByTag(fragmentStack.pop().tag)
                if (fragment != null) {
                    ft.remove(fragment)
                }
            }

            //Attempt to reattach previous fragment
            fragment = reattachPreviousFragment(ft)

            var bShouldPush = false
            //If we can't reattach, either pull from the stack, or create a new root fragment
            if (fragment != null) {
                ft.commit()
            } else {
                if (!fragmentStack.isEmpty()) {
                    fragment = fragmentStack.peek()
                    ft.add(mContainerId, fragment!!, fragment.tag)
                    ft.commit()
                } else {
                    fragment = getRootFragment(currentStackIndex)
                    ft.add(mContainerId, fragment, generateTag(fragment))
                    ft.commit()

                    bShouldPush = true
                }
            }

            executePendingTransactions()

            if (bShouldPush) {
                mFragmentStacks[currentStackIndex].push(fragment)
            }

            //Update the stored version we have in the list
            mFragmentStacks[currentStackIndex] = fragmentStack

            mCurrentFrag = fragment
            mTransactionListener?.onFragmentTransaction(mCurrentFrag, TransactionType.POP)
        }
    }

    /**
     * Replace the current fragment
     *
     * @param fragment           the fragment to be shown instead
     * @param transactionOptions Transaction options to be displayed
     */
    @JvmOverloads
    fun replaceFragment(fragment: Fragment, transactionOptions: FragNavTransactionOptions? = null) {
        val poppingFrag = currentFrag

        if (poppingFrag != null) {
            val ft = createTransactionWithOptions(transactionOptions)

            //overly cautious fragment popFragment
            val fragmentStack = mFragmentStacks[currentStackIndex]
            if (!fragmentStack.isEmpty()) {
                fragmentStack.pop()
            }

            val tag = generateTag(fragment)
            ft.replace(mContainerId, fragment, tag)

            //Commit our transactions
            ft.commit()

            executePendingTransactions()

            fragmentStack.push(fragment)
            mCurrentFrag = fragment

            mTransactionListener?.onFragmentTransaction(mCurrentFrag, TransactionType.REPLACE)
        }
    }

    /**
     * Clear any DialogFragments that may be shown
     */
    fun clearDialogFragment() {
        if (mCurrentDialogFrag != null) {
            mCurrentDialogFrag!!.dismiss()
            mCurrentDialogFrag = null
        } else {
            val fragmentManager: FragmentManager
            if (mCurrentFrag != null) {
                fragmentManager = mCurrentFrag!!.childFragmentManager
            } else {
                fragmentManager = mFragmentManager
            }

            if (fragmentManager.fragments != null) {
                for (fragment in fragmentManager.fragments) {
                    if (fragment is DialogFragment) {
                        fragment.dismiss()
                    }
                }
            }
        }// If we don't have the current dialog, try to find and dismiss it
    }

    /**
     * Display a DialogFragment on the screen
     *
     * @param dialogFragment The Fragment to be Displayed
     */
    fun showDialogFragment(dialogFragment: DialogFragment?) {
        if (dialogFragment != null) {
            val fragmentManager: FragmentManager
            if (mCurrentFrag != null) {
                fragmentManager = mCurrentFrag!!.childFragmentManager
            } else {
                fragmentManager = mFragmentManager
            }

            //Clear any current dialog fragments
            if (fragmentManager.fragments != null) {
                for (fragment in fragmentManager.fragments) {
                    if (fragment is DialogFragment) {
                        fragment.dismiss()
                        mCurrentDialogFrag = null
                    }
                }
            }

            mCurrentDialogFrag = dialogFragment
            try {
                dialogFragment.show(fragmentManager, dialogFragment.javaClass.name)
            } catch (e: IllegalStateException) {
                // Activity was likely destroyed before we had a chance to show, nothing can be done here.
            }

        }
    }

    //endregion

    //region Private helper functions

    /**
     * Helper function to make sure that we are starting with a clean slate and to perform our first fragment interaction.
     *
     * @param index the tab index to initialize to
     */
    private fun initialize(@TabIndex index: Int) {
        currentStackIndex = index
        if (currentStackIndex > mFragmentStacks.size) {
            throw IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks")
        }

        currentStackIndex = index
        clearFragmentManager()
        clearDialogFragment()

        if (index == NO_TAB) {
            return
        }

        val ft = createTransactionWithOptions(null)

        val fragment = getRootFragment(index)
        ft.add(mContainerId, fragment, generateTag(fragment))
        ft.commit()

        executePendingTransactions()

        mCurrentFrag = fragment
        mTransactionListener?.onTabTransaction(mCurrentFrag, currentStackIndex)
    }

    /**
     * Helper function to get the root fragment for a given index. This is done by either passing them in the constructor, or dynamically via NavListener.
     *
     * @param index The tab index to get this fragment from
     * @return The root fragment at this index
     * @throws IllegalStateException This will be thrown if we can't find a rootFragment for this index. Either because you didn't provide it in the
     * constructor, or because your RootFragmentListener.getRootFragment(index) isn't returning a fragment for this index.
     */
    @CheckResult
    @Throws(IllegalStateException::class)
    private fun getRootFragment(index: Int): Fragment {
        var fragment: Fragment? = null
        if (!mFragmentStacks[index].isEmpty()) {
            fragment = mFragmentStacks[index].peek()
        } else if (mRootFragmentListener != null) {
            fragment = mRootFragmentListener.getRootFragment(index)

            if (currentStackIndex != NO_TAB) {
                mFragmentStacks[currentStackIndex].push(fragment)
            }

        }
        if (fragment == null) {
            throw IllegalStateException("Either you haven't past in a fragment at this index in your constructor, or you haven't " + "provided a way to create it while via your RootFragmentListener.getRootFragment(index)")
        }

        return fragment
    }

    /**
     * Will attempt to reattach a previous fragment in the FragmentManager, or return null if not able to.
     *
     * @param ft current fragment transaction
     * @return Fragment if we were able to find and reattach it
     */
    private fun reattachPreviousFragment(ft: FragmentTransaction): Fragment? {
        val fragmentStack = mFragmentStacks[currentStackIndex]
        var fragment: Fragment? = null
        if (!fragmentStack.isEmpty()) {
            fragment = mFragmentManager.findFragmentByTag(fragmentStack.peek().tag)
            if (fragment != null) {
                ft.attach(fragment)
            }
        }
        return fragment
    }

    /**
     * Attempts to detach any current fragment if it exists, and if none is found, returns.
     *
     * @param ft the current transaction being performed
     */
    private fun detachCurrentFragment(ft: FragmentTransaction) {
        val oldFrag = currentFrag
        if (oldFrag != null) {
            ft.detach(oldFrag)
        }
    }

    /**
     * Create a unique fragment tag so that we can grab the fragment later from the FragmentManger
     *
     * @param fragment The fragment that we're creating a unique tag for
     * @return a unique tag using the fragment's class name
     */
    @CheckResult
    private fun generateTag(fragment: Fragment): String {
        return fragment.javaClass.name + ++mTagCount
    }

    /**
     * This check is here to prevent recursive entries into executePendingTransactions
     */
    private fun executePendingTransactions() {
        if (!mExecutingTransaction) {
            mExecutingTransaction = true
            mFragmentManager.executePendingTransactions()
            mExecutingTransaction = false
        }
    }

    /**
     * Private helper function to clear out the fragment manager on initialization. All fragment management should be done via FragNav.
     */
    private fun clearFragmentManager() {
        if (mFragmentManager.fragments != null) {
            val ft = createTransactionWithOptions(null)
            for (fragment in mFragmentManager.fragments) {
                if (fragment != null) {
                    ft.remove(fragment)
                }
            }
            ft.commit()
            executePendingTransactions()
        }
    }

    /**
     * Setup a fragment transaction with the given option
     *
     * @param transactionOptions The options that will be set for this transaction
     */
    @CheckResult
    private fun createTransactionWithOptions(transactionOptions: FragNavTransactionOptions?): FragmentTransaction {
        var transactionOptions = transactionOptions
        val ft = mFragmentManager.beginTransaction()
        if (transactionOptions == null) {
            transactionOptions = mDefaultTransactionOptions
        }
        if (transactionOptions != null) {

            ft.setCustomAnimations(transactionOptions.enterAnimation, transactionOptions.exitAnimation, transactionOptions.popEnterAnimation, transactionOptions.popExitAnimation)
            ft.setTransitionStyle(transactionOptions.transitionStyle)

            ft.setTransition(transactionOptions.transition)


            if (transactionOptions.sharedElements != null) {
                for (sharedElement in transactionOptions.sharedElements) {
                    ft.addSharedElement(sharedElement.first!!, sharedElement.second!!)
                }
            }


            if (transactionOptions.breadCrumbTitle != null) {
                ft.setBreadCrumbTitle(transactionOptions.breadCrumbTitle)
            }

            if (transactionOptions.breadCrumbShortTitle != null) {
                ft.setBreadCrumbShortTitle(transactionOptions.breadCrumbShortTitle)

            }
        }
        return ft
    }


    /**
     * Get a copy of the stack at a given index
     *
     * @return requested stack
     */
    @CheckResult
    fun getStack(@TabIndex index: Int): Stack<Fragment>? {
        if (index == NO_TAB) return null
        if (index >= mFragmentStacks.size) {
            throw IndexOutOfBoundsException("Can't get an index that's larger than we've setup")
        }
        return mFragmentStacks[index].clone() as Stack<Fragment>
    }

    //endregion

    //region SavedInstanceState

    /**
     * Call this in your Activity's onSaveInstanceState(Bundle outState) method to save the instance's state.
     *
     * @param outState The Bundle to save state information to
     */
    fun onSaveInstanceState(outState: Bundle) {

        // Write tag count
        outState.putInt(EXTRA_TAG_COUNT, mTagCount)

        // Write select tab
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, currentStackIndex)

        // Write current fragment
        if (mCurrentFrag != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, mCurrentFrag!!.tag)
        }

        // Write stacks
        try {
            val stackArrays = JSONArray()

            for (stack in mFragmentStacks) {
                val stackArray = JSONArray()

                for (fragment in stack) {
                    stackArray.put(fragment.tag)
                }

                stackArrays.put(stackArray)
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            // Nothing we can do
        }

    }

    /**
     * Restores this instance to the state specified by the contents of savedInstanceState
     *
     * @param savedInstanceState The bundle to restore from
     * @param rootFragments      List of root fragments from which to initialize empty stacks. If null, pull fragments from RootFragmentListener.
     * @return true if successful, false if not
     */
    private fun restoreFromBundle(savedInstanceState: Bundle?, rootFragments: List<Fragment>?): Boolean {
        if (savedInstanceState == null) {
            return false
        }

        // Restore tag count
        mTagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0)

        // Restore current fragment
        mCurrentFrag = mFragmentManager.findFragmentByTag(savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT))

        // Restore fragment stacks
        try {
            val stackArrays = JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK))

            for (x in 0 until stackArrays.length()) {
                val stackArray = stackArrays.getJSONArray(x)
                val stack = Stack<Fragment>()

                if (stackArray.length() == 1) {
                    val tag = stackArray.getString(0)
                    val fragment: Fragment?

                    if (tag == null || "null".equals(tag, ignoreCase = true)) {
                        if (rootFragments != null) {
                            fragment = rootFragments[x]
                        } else {
                            fragment = getRootFragment(x)
                        }

                    } else {
                        fragment = mFragmentManager.findFragmentByTag(tag)
                    }

                    if (fragment != null) {
                        stack.add(fragment)
                    }
                } else {
                    for (y in 0 until stackArray.length()) {
                        val tag = stackArray.getString(y)

                        if (tag != null && !"null".equals(tag, ignoreCase = true)) {
                            val fragment = mFragmentManager.findFragmentByTag(tag)

                            if (fragment != null) {
                                stack.add(fragment)
                            }
                        }
                    }
                }

                mFragmentStacks.add(stack)
            }
            // Restore selected tab if we have one
            when (savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX)) {
                TAB1 -> switchTab(TAB1)
                TAB2 -> switchTab(TAB2)
                TAB3 -> switchTab(TAB3)
            }

            //Successfully restored state
            return true
        } catch (t: Throwable) {
            return false
        }

    }
    //endregion

    enum class TransactionType {
        PUSH,
        POP,
        REPLACE
    }

    //Declare the TabIndex annotation
    @IntDef(NO_TAB, TAB1, TAB2, TAB3)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TabIndex

    // Declare Transit Styles
    @IntDef(FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class Transit

    interface RootFragmentListener {
        /**
         * Dynamically create the Fragment that will go on the bottom of the stack
         *
         * @param index the index that the root of the stack Fragment needs to go
         * @return the new Fragment
         */
        fun getRootFragment(index: Int): Fragment
    }

    interface TransactionListener {

        fun onTabTransaction(fragment: Fragment?, index: Int)

        fun onFragmentTransaction(fragment: Fragment?, transactionType: TransactionType)
    }

    class Builder(private val mSavedInstanceState: Bundle?, internal val mFragmentManager: FragmentManager, internal val mContainerId: Int) {
        internal var mRootFragmentListener: RootFragmentListener? = null
        @TabIndex
        internal var mSelectedTabIndex = TAB1
        internal var mTransactionListener: TransactionListener? = null
        internal var mDefaultTransactionOptions: FragNavTransactionOptions? = null
        internal var mNumberOfTabs = 0
        internal var mRootFragments: MutableList<Fragment>? = null

        /**
         * @param selectedTabIndex The initial tab index to be used must be in range of rootFragments size
         */
        fun selectedTabIndex(@TabIndex selectedTabIndex: Int): Builder {
            mSelectedTabIndex = selectedTabIndex
            if (mRootFragments != null && mSelectedTabIndex > mNumberOfTabs) {
                throw IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks")
            }
            return this
        }

        /**
         * @param rootFragment A single root fragment. This library can still be helpful when managing a single stack of fragments
         */
        fun rootFragment(rootFragment: Fragment): Builder {
            mRootFragments = ArrayList(1)
            mRootFragments!!.add(rootFragment)
            mNumberOfTabs = 1
            return rootFragments(mRootFragments!!)
        }

        /**
         * @param rootFragments a list of root fragments. root Fragments are the root fragments that exist on any tab structure. If only one fragment is sent in, fragnav will still manage
         * transactions
         */
        fun rootFragments(rootFragments: MutableList<Fragment>): Builder {
            mRootFragments = rootFragments
            mNumberOfTabs = rootFragments.size
            if (mNumberOfTabs > MAX_NUM_TABS) {
                throw IllegalArgumentException("Number of root fragments cannot be greater than $MAX_NUM_TABS")
            }
            return this
        }

        /**
         * @param transactionOptions The default transaction options to be used unless otherwise defined.
         */
        fun defaultTransactionOptions(transactionOptions: FragNavTransactionOptions): Builder {
            mDefaultTransactionOptions = transactionOptions
            return this
        }


        /**
         * @param rootFragmentListener a listener that allows for dynamically creating root fragments
         * @param numberOfTabs         the number of tabs that will be switched between
         */
        fun rootFragmentListener(rootFragmentListener: RootFragmentListener, numberOfTabs: Int): Builder {
            mRootFragmentListener = rootFragmentListener
            mNumberOfTabs = numberOfTabs
            if (mNumberOfTabs > MAX_NUM_TABS) {
                throw IllegalArgumentException("Number of tabs cannot be greater than $MAX_NUM_TABS")
            }
            return this
        }

        /**
         * @param val A listener to be implemented (typically within the main activity) to fragment transactions (including tab switches)
         */
        fun transactionListener(`val`: TransactionListener): Builder {
            mTransactionListener = `val`
            return this
        }


        fun build(): FragNavController {
            if (mRootFragmentListener == null && mRootFragments == null) {
                throw IndexOutOfBoundsException("Either a root fragment(s) needs to be set, or a fragment listener")
            }
            return FragNavController(this, mSavedInstanceState)
        }


    }

    companion object {
        //Declare the constants  There is a maximum of 5 tabs, this is per Material Design's Bottom Navigation's design spec.
        const val NO_TAB = -1
        const val TAB1 = 0
        const val TAB2 = 1
        const val TAB3 = 2

        private val MAX_NUM_TABS = 3

        // Extras used to store savedInstanceState
        private val EXTRA_TAG_COUNT = FragNavController::class.java.name + ":EXTRA_TAG_COUNT"
        private val EXTRA_SELECTED_TAB_INDEX = FragNavController::class.java.name + ":EXTRA_SELECTED_TAB_INDEX"
        private val EXTRA_CURRENT_FRAGMENT = FragNavController::class.java.name + ":EXTRA_CURRENT_FRAGMENT"
        private val EXTRA_FRAGMENT_STACK = FragNavController::class.java.name + ":EXTRA_FRAGMENT_STACK"

        fun newBuilder(savedInstanceState: Bundle?, fragmentManager: FragmentManager, containerId: Int): Builder {
            return Builder(savedInstanceState, fragmentManager, containerId)
        }
    }
}
/**
 * Function used to switch to the specified fragment stack
 *
 * @param index The given index to switch to
 * @throws IndexOutOfBoundsException Thrown if trying to switch to an index outside given range
 */
/**
 * Push a fragment onto the current stack
 *
 * @param fragment The fragment that is to be pushed
 */
/**
 * Pop the current fragment from the current tab
 */
/**
 * Pop the current fragment from the current tab
 */
/**
 * Clears the current tab's stack to get to just the bottom Fragment. This will reveal the root fragment.
 */
/**
 * Replace the current fragment
 *
 * @param fragment the fragment to be shown instead
 */
