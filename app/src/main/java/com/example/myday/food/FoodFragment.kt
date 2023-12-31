package com.example.myday.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.myday.R
import com.example.myday.databinding.FragmentFoodBinding
import com.example.myday.food.tabview.NutritionFragmentStateAdapter
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodFragment: Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchResult: MutableList<Row>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        setupTabLayoutWithViewPager()

        binding.foodMainlayout.setOnTouchListener { v, event ->
            this.view?.let { it1 -> hideKeyboard(it1) }
            false
        }

        // Spinner 설정(아침, 점심, 저녁 중 선택)
        val spinner: Spinner = binding.spinner
        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.`when`,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
        spinner.setSelection(0) // 초기값은 아침으로 설정
    }

    fun initNutritionRecyclerView() {
        val bundle = Bundle()
        bundle.putParcelableArrayList("resultList", ArrayList(searchResult))
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                bundle.putString("when", parent.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add<SearchResultFragment>(R.id.food_fragment_container, args = bundle)
            val mainFragment = requireActivity().findViewById<LinearLayout>(R.id.food_mainlayout)
            mainFragment.visibility = View.GONE
            val searchFragment = requireActivity().findViewById<LinearLayout>(R.id.food_fragment_container)
            searchFragment.visibility = View.VISIBLE
        }
    }

    private fun setupViewPager() {
        val adapter = NutritionFragmentStateAdapter(this)
        binding.nutritionVp.adapter = adapter
    }

    private fun setupTabLayoutWithViewPager() {
        val tabTitles = resources.getStringArray(R.array.`when`)
        val tabLayout = binding.nutritionTl
        val viewPager = binding.nutritionVp

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.foodAddBtn.setOnClickListener {
            this.view?.let { it1 -> hideKeyboard(it1) }
            val input = binding.foodEt.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://openapi.foodsafetykorea.go.kr/api/27dfc35a066042d49e98/I2790/json/1/30/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(GetKcalInfo::class.java)
            val call = service.getNutritionData(input)

            call.enqueue(object : Callback<FoodNutrition> {
                override fun onResponse(
                    call: Call<FoodNutrition>,
                    response: Response<FoodNutrition>
                ) {
                    val body = response.body()
                    if (body != null && response.isSuccessful) {
                        if (body.I2790.RESULT.CODE == "INFO-200") {
                            Toast.makeText(
                                this@FoodFragment.context,
                                "검색 결과가 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            response.body()?.I2790?.row?.let {
                                searchResult = it
                                initNutritionRecyclerView()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FoodNutrition>, t: Throwable) {
                    Log.v("retrofit", "call failed", t)
                }
            })

        }
        // Cloud Firestore 인스턴스 초기화
        val db = FirebaseFirestore.getInstance()

        // 사용자 정보 가져오기
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid // 사용자 UID 가져오기
            db.collection("User")
                .document(userId) // UID를 사용하여 문서 가져오기
                .get()
                .addOnSuccessListener { document: DocumentSnapshot ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: "사용자"
                        binding.foodGreeting.text = "${userName}님, 오늘은 무엇을 먹었나요?"
                    } else {
                        // 문서가 없을 경우 처리
                        binding.foodGreeting.text = "사용자 정보 없음"
                    }
                }
                .addOnFailureListener { e: Exception ->
                    // 오류 처리
                    binding.foodGreeting.text = "사용자 정보를 가져오지 못했습니다: $e"
                }
        } else {
            // 사용자가 로그인되어 있지 않을 경우 처리
            binding.foodGreeting.text = "로그인되지 않았습니다."
        }
    }
}