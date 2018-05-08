package net.janczar.kotlinrx.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import net.janczar.kotlinrx.KotlinRXApp
import net.janczar.kotlinrx.R
import net.janczar.kotlinrx.domain.model.*
import net.janczar.kotlinrx.domain.repository.RegisterRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerRepository: RegisterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val app = application as KotlinRXApp
        registerRepository = app.registerRepository

        register_button.clicks().subscribe {

            registerRepository.register(
                            RegisterForm(
                                    register_name_edittext.text.toString(),
                                    register_email_edittext.text.toString(),
                                    register_password_edittext.text.toString()
                            )
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                result -> when (result) {
                                    is RegisterSuccess -> {
                                        Toast.makeText(this, "Register success, user id is ${result.userId}", Toast.LENGTH_SHORT).show()
                                        clearErrors()
                                    }
                                    is RegisterFailure -> showErrors(result)
                                }
                            },{
                                error -> Toast.makeText(this, "Transfer failed: ${error.message}", Toast.LENGTH_SHORT).show()
                            })


        }
    }

    private fun clearErrors() {
        register_name_input.error = null
        register_email_input.error = null
        register_password_input.error = null
    }

    private fun showErrors(errors: RegisterFailure) {
        register_name_input.error = errors.nameError
        register_email_input.error = errors.emailError
        register_password_input.error = errors.passwordError
    }
}
