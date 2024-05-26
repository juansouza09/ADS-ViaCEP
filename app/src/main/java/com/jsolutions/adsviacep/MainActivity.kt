package com.jsolutions.adsviacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import com.jsolutions.adsviacep.api.AddressResponse
import com.jsolutions.adsviacep.api.RetrofitInstance
import com.jsolutions.adsviacep.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val cep = binding.cepInput.text.toString()
            if (cep.isNotEmpty()) {
                fetchAddress(cep)
            } else {
                Toast.makeText(this, "Por favor, insira um CEP válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAddress(cep: String) {
        RetrofitInstance.api.getAddress(cep).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {
                if (response.isSuccessful) {
                    val address = response.body()
                    address?.let {
                        binding.addressText.text = """
                            CEP: ${it.cep}
                            Logradouro: ${it.logradouro}
                            Bairro: ${it.bairro}
                            Localidade: ${it.localidade}
                            UF: ${it.uf}
                        """.trimIndent()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Falha ao obter o endereço", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
