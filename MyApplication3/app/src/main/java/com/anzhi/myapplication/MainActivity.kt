package com.anzhi.myapplication

import android.animation.ObjectAnimator
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // var v=bindView(this)
        setContentView(R.layout.activity_main)
        var mBigView=findViewById<BigView>(R.id.bigView)
        var inputStream=assets.open("mybig.png")
          mBigView.setImage(inputStream);
     /*   var objectAnimator=ObjectAnimator.ofInt(v,"moveUp",10,1000)
        objectAnimator.setDuration(10000)
        objectAnimator.repeatCount=ObjectAnimator.INFINITE
        objectAnimator.start();*/
       /* setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
