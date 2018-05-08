package net.janczar.kotlinrx

import android.app.Application
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import net.janczar.kotlinrx.data.net.AuthInterceptor
import net.janczar.kotlinrx.data.repository.RestAuthRepository
import net.janczar.kotlinrx.data.repository.RestHelloRepository
import net.janczar.kotlinrx.data.repository.RestRegisterRepository
import net.janczar.kotlinrx.domain.repository.AuthRepository
import net.janczar.kotlinrx.domain.repository.HelloRepository
import net.janczar.kotlinrx.domain.repository.RegisterRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class KotlinRXApp: Application() {

    private lateinit var retrofit: Retrofit

    lateinit var authInterceptor: AuthInterceptor
        private set

    lateinit var  registerRepository: RegisterRepository
        private set

    lateinit var  helloRepository: HelloRepository
        private set

    lateinit var authRepository: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()

        authInterceptor = AuthInterceptor()
        retrofit = initRetrofit()

        registerRepository = RestRegisterRepository(retrofit)
        helloRepository = RestHelloRepository(retrofit)
        authRepository = RestAuthRepository(retrofit, authInterceptor)
    }

    private fun initRetrofit() =
            Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(MoshiConverterFactory.create(initMoshi()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(initOkHttp())
                    .build()

    private fun initMoshi() =
            Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

    private fun initOkHttp() =
            OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build()
}