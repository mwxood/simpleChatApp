package com.example.simplechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.FirebaseDatabase
import android.R.attr.password
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

class MainActivity : AppCompatActivity() {
    var emailText: EditText? = null
    var passwordText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)

        if(mAuth.currentUser != null) {
            logIn()
        }
    }

    fun loginButton(view: View) {
        mAuth.signInWithEmailAndPassword(emailText?.text.toString(), passwordText?.text.toString())
            .addOnCompleteListener(this
            ) { task ->
                Log.i("info", emailText?.text.toString());
                if (task.isSuccessful) {
                    logIn()
                } else {
                    mAuth.createUserWithEmailAndPassword(emailText?.text.toString(), passwordText?.text.toString()).addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user!!.uid).child("email").setValue(emailText?.text.toString())

                            logIn()
                        } else {
                            Toast.makeText(this, "Login failed. Try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    fun logIn() {
        val intent = Intent(this, ChatsActivity::class.java)
        startActivity(intent)

    }
}
