package com.harsh.typicode.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harsh.typicode.R
import com.harsh.typicode.ui.albums.AlbumsFragment

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, AlbumsFragment())
                    .commit()
        }

    }
}
