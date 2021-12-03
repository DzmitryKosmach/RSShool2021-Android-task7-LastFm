package com.rsschool.task7_lastfm

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.rsschool.task7_lastfm.model.api.ArtistsApiImpl
import com.rsschool.task7_lastfm.repository.Repository
import com.rsschool.task7_lastfm.ui.MainActivity
import com.rsschool.task7_lastfm.ui.fragments.ArtistsFragment
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

//@UninstallModules(AppModule::class)
@HiltAndroidTest
class MainTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: Repository

    @Test
    fun someTest() {
        assertThat("test string", containsString("test"))
    }

    @Test
    fun mainActivityTest(){
        val scenario = launchActivity<MainActivity>()
    }

    @Test
    fun artistsFragmentTest(){
        val scenario = launchFragmentInContainer<ArtistsFragment>()
    }

//    @Module
//    @InstallIn(ApplicationComponent::class)
//        object AppModule {
//        @Singleton
//        @Provides
//        fun provideRepository(artistsApiImpl: ArtistsApiImpl): Repository =
//            Repository(artistsApiImpl)
//    }

}