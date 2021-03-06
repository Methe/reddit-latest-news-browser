package br.com.matheus.reddit.base.statemachine

import android.os.Bundle

/**
 * Simple finite state machine for view stateMap.
 * This class have the necessary methods to do ViewState transitions
 * Extends this class and implements performChangeState method to make ViewState transitions
 */
const val STATE_MACHINE_CURRENT_KEY = "STATE_MACHINE_CURRENT_KEY"
abstract class StateMachine<T> {

    internal val stateMap = HashMap<Int, T>()

    private var onChangeState: ((key: Int) -> Unit)? = null
    var currentStateKey: Int = -1
        private set

    /**
     * The implementation to change the state

     * @param state
     */
    protected abstract fun performChangeState(state: T)

    /**
     * The implementation to Create a State

     * @return new State instance
     */
    protected abstract fun createState(): T

    fun onChangeState(onChangeState: ((key: Int) -> Unit)) {
        this.onChangeState = onChangeState
    }

    fun changeState(stateKey: Int, forceChange: Boolean = false, onChangeState: ((key: Int) -> Unit)? = this.onChangeState) {

        if (stateKey == currentStateKey && !forceChange) return

        performChangeState(stateMap[stateKey]!!)
        // On change state
        onChangeState?.invoke(stateKey)

        currentStateKey = stateKey
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        currentStateKey = savedInstanceState.getInt(STATE_MACHINE_CURRENT_KEY)
    }

    fun saveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putInt(STATE_MACHINE_CURRENT_KEY, currentStateKey)
        return bundle
    }

    fun add(key: Int, state: T.() -> Unit) = stateMap.put(key, createState().apply(state))

    inline fun setup(initialState: Int = -1, restoreState: Bundle? = null, func: StateMachine<T>.() -> Unit) {
        func()
        var state = if (currentStateKey != -1) currentStateKey else initialState
        state = restoreState?.getInt(STATE_MACHINE_CURRENT_KEY, state) ?: state
        if (state != -1) changeState(state, forceChange = true)
    }

}