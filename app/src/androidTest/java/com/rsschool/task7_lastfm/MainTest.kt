package com.rsschool.task7_lastfm

import com.rsschool.task7_lastfm.repository.Repository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: Repository

    @Test
    fun someTest() {
    }

//    @Module
//    @InstallIn(ApplicationComponent::class)
//    object AppModule
}