package com.example.loginiot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        auth = FirebaseAuth.getInstance()


        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()


            if (email.isNotEmpty() && password.isNotEmpty()) {


                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "¡Bienvenido! Sesión iniciada.", Toast.LENGTH_SHORT).show()
                            // TODO: Aquí puedes cambiar de pantalla (Intent a otra Activity)
                        } else {

                            registrarUsuarioEnFirebase(email, password)
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuarioEnFirebase(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Cuenta creada y registrada con éxito!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}