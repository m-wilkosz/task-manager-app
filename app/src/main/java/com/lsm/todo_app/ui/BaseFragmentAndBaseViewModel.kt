package com.lsm.todo_app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.lsm.todo_app.R
import java.util.concurrent.atomic.AtomicBoolean

open class BaseViewModel : ViewModel() {
    val navigationCommands = BaseFragment.SingleLiveEvent<BaseFragment.NavigationCommand>()

    open fun prepare(args: Bundle?) {}

    fun navigateTo(directions: BaseFragment.NavigationCommand) {
        navigationCommands.postValue(directions)
    }
}

open class BaseFragment<T: BaseViewModel>(private val vmType: Class<T>) : Fragment() {
    open fun activityModel() = false

    protected lateinit var viewModel: T
    private val navController by lazy {
        requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }
    private fun setupViewModel() {
        if (activityModel())
            viewModel = ViewModelProvider(requireActivity()).get(vmType)
        else
            viewModel = ViewModelProvider(this).get(vmType)

        this.viewModel.prepare(arguments)

        this.viewModel.navigationCommands.observe(viewLifecycleOwner, Observer { command ->
            resolveNavigationCommand(command)
        })
    }

    private fun resolveNavigationCommand(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                navController.navigate(command.directions)
            }
        }
    }

    sealed class NavigationCommand {
        data class To(val directions: NavDirections) : NavigationCommand()
    }

    class SingleLiveEvent<T> : MutableLiveData<T> () {
        private val pending = AtomicBoolean(false)

        @MainThread
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            if (hasActiveObservers()) {
                Log.w(TAG, "Multiple observers registered but only one will be notified of changes")
            }

            super.observe(owner, Observer { t ->
                if (pending.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            })
        }

        @MainThread
        override fun setValue(t: T?) {
            pending.set(true)
            super.setValue(t)
        }

        @MainThread
        fun call() {
            value = null
        }

        companion object {
            private val TAG = "com.lsm.todo_app.ui.SingleLiveEvent"
        }
    }
}

fun <T> MutableLiveData<T>.notifyPostObserver() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}