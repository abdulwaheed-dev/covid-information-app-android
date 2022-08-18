package com.example.covidapp.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.covidapp.R
import com.example.covidapp.data.model.Model
import com.example.covidapp.databinding.ActivityMainBinding
import com.example.covidapp.ui.adapter.PrecautionsAdapter
import com.example.covidapp.ui.adapter.SymptomsAdapter
import com.example.covidapp.utils.Constant.KNOW_MORE
import com.example.covidapp.utils.goNextScreen
import com.example.covidapp.utils.loadWeb
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val symptomsList = ArrayList<Model>()
    private val precautionsList = ArrayList<Model>()

    private val symptomsAdapter: SymptomsAdapter by lazy { SymptomsAdapter() }
    private val precautionsAdapter: PrecautionsAdapter by lazy { PrecautionsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = symptomsAdapter
        }

        binding.recyclerViewPrecautions.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = precautionsAdapter
        }

        symptomsList.add(
            Model(
                R.drawable.cough,
                "Dry Cough",
                "A dry cough is one that does not produce phlegm or mucus."
            )
        )
        symptomsList.add(
            Model(
                R.drawable.fever,
                "Fever",
                "A fever is a temporary increase in your body temperature."
            )
        )
        symptomsList.add(
            Model(
                R.drawable.headache,
                "Head Ache",
                "A painful sensation in any part of the head, ranging from sharp to dull, that may occur with other symptoms."
            )
        )
        precautionsList.add(
            Model(
                R.drawable.vaccine,
                "Get Vaccinated",
                "Get vaccinated and protect yourself and others from corona"
            )
        )
        precautionsList.add(
            Model(
                R.drawable.handwash,
                "Hand Wash",
                " Wash your hands well and often. Use hand sanitizer when you’re not near soap and water."
            )
        )
        precautionsList.add(
            Model(
                R.drawable.mask,
                "Wear Mask",
                "Masks are a key measure to suppress transmission and save lives."
            )
        )

        symptomsAdapter.submitList(symptomsList)
        precautionsAdapter.submitList(precautionsList)
        initClicks()
        getData()
    }

    private fun initClicks() = binding.run {
        txtViewSymptoms.setOnClickListener {
            goNextScreen(SymptomsActivity::class.java)
        }

        btnKnowMore.setOnClickListener {
            loadWeb(KNOW_MORE)
        }

        txtViewPrecautions.setOnClickListener {
            goNextScreen(PrecautionActivity::class.java)
        }
    }

    private fun getData() = binding.run {
        val txtTotalR : TextView = findViewById(R.id.txtTotalR)
        val txtPositive : TextView = findViewById(R.id.txtPositive)
        val txtNegative : TextView = findViewById(R.id.txtNegative)
        val txtHospital : TextView = findViewById(R.id.txtHospital)
        val txtDead : TextView = findViewById(R.id.txtDead)


        var url = "https://api.covidtracking.com/v1/us/current.json"

        val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            // on below line we are adding a try catch block.
            try {
                val obj : JSONObject = response.getJSONObject(0)

                val total: String = obj.getString("totalTestResults")
                val positive: String = obj.getString("positive")
                val negative: String = obj.getString("negative")
                val hospital: String = obj.getString("hospitalizedCumulative")
                val death: String = obj.getString("death")

                txtTotalR.text = total
                txtPositive.text = positive
                txtNegative.text = negative
                txtDead.text = death
                txtHospital.text = hospital

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, { error ->
            Log.e("TAG", "RESPONSE IS $error")
            Toast.makeText(this@MainActivity, "Fail to get response", Toast.LENGTH_SHORT).show()
        })
        queue.add(request)

    }
}
