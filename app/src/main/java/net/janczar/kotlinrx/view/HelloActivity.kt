package net.janczar.kotlinrx.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hello.*
import net.janczar.kotlinrx.KotlinRXApp
import net.janczar.kotlinrx.R
import net.janczar.kotlinrx.domain.model.HelloMessage
import net.janczar.kotlinrx.domain.model.HttpCallFailureException
import net.janczar.kotlinrx.domain.model.NoNetworkException
import net.janczar.kotlinrx.domain.model.ServerUnreachableException
import net.janczar.kotlinrx.domain.repository.AuthRepository
import net.janczar.kotlinrx.domain.repository.HelloRepository


class HelloActivity : AppCompatActivity() {

    private lateinit var helloRepository: HelloRepository

    private lateinit var authRepository: AuthRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        val app = application as KotlinRXApp
        helloRepository = app.helloRepository
        authRepository = app.authRepository

        hello_button.clicks().subscribe {
            helloRepository
                    .sayHello()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { message -> displayMessage(message) },
                            { error -> displayError(error) }
                    )
        }

        token_button.clicks().subscribe {
            authRepository
                    .sayAuthorizedHello()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { message -> displayMessage(message) },
                            { error -> displayError(error) }
                    )
        }

        register_button.clicks().subscribe {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun displayMessage(helloMessage: HelloMessage) {
        Toast.makeText(this, helloMessage.message, Toast.LENGTH_SHORT).show()
    }

    private fun displayError(error: Throwable) {
        when (error) {
            is NoNetworkException -> displayNoNetworkError()
            is ServerUnreachableException -> displayServerUnreachableError()
            is HttpCallFailureException -> displayCallFailedError()
            else -> displayGenericError(error)
        }
    }

    private fun displayNoNetworkError() {
        Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show()
    }

    private fun displayServerUnreachableError() {
        Toast.makeText(this, "Server is unreachable!", Toast.LENGTH_SHORT).show()
    }

    private fun displayCallFailedError() {
        Toast.makeText(this, "Call failed!", Toast.LENGTH_SHORT).show()
    }

    private fun displayGenericError(error: Throwable) {
        Log.i("DEBUG", "Error in say hello", error)
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

}