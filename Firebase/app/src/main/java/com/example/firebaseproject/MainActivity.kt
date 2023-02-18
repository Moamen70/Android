package com.example.firebaseproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnReg.setOnClickListener {
            registerUser()
        }

        binding.btnLog.setOnClickListener {
            logInUser()
        }

        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }

    }

    override fun onStart() {
        super.onStart()
        checkedLoggedInState()
    }

    private fun registerUser() {
        val email = binding.etRegEmail.text.toString()
        val pass = binding.etRegPass.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()){
           CoroutineScope(Dispatchers.IO).launch {
               try {
                    auth.createUserWithEmailAndPassword(email,pass).await()
                   withContext(Dispatchers.Main){
                       checkedLoggedInState()
                   }
               }
               catch (e:Exception){
                   withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
                   }
               }
           }
        }

    }

    private fun logInUser() {
        val email = binding.etLogEmail.text.toString()
        val pass = binding.etLogPass.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,pass).await()
                    withContext(Dispatchers.Main){
                        checkedLoggedInState()
                    }
                }
                catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateProfile() {


        auth.currentUser?.let { user ->
            val username = binding.etUserName.text.toString()
            val photoUri = Uri.parse("android.resource://$packageName/${R.drawable.download}")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoUri)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    checkedLoggedInState()
                    user.updateProfile(profileUpdates).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"Successfully Updated",Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun checkedLoggedInState() {
        val user = auth.currentUser
        if (auth.currentUser == null){
            binding.tvResult.text ="You are not logged in"
        }
        else {
            binding.tvResult.text ="Logged in"
            binding.etUserName.setText(user?.displayName)
            binding.img.setImageURI(user?.photoUrl)
        }
    }
}