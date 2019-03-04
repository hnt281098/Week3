@file:Suppress("DEPRECATION")

package com.example.week3

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_main.*
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.week3.data.*
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.google.gson.GsonBuilder
import org.json.JSONObject


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var scrollListener : EndlessRecyclerViewScrollListener
    private lateinit var arrData : ArrayList<Datum>
    private lateinit var adapter : AdapterRecyclerView
    private lateinit var dpHelper : DPHelper
    private lateinit var result : GraphObject

    private var lastGraphResponse : GraphResponse? = null
    private var firstGraphResponse : GraphResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main)

        dpHelper = DPHelper(applicationContext)
        dpHelper.initDB()
        arrData = ArrayList()

        checkSign()

        adapter = AdapterRecyclerView(applicationContext, arrData)
        val layout = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layout
        recycler_view.adapter = adapter

        loginButton = this.findViewById(R.id.login_button)
        callbackManager =  CallbackManager.Factory.create()

        loginButton.setOnClickListener {
            loginWithFB()
        }

        scrollListener = object : EndlessRecyclerViewScrollListener(LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("Page" , page.toString())
                loadNextData()
            }

        }

        recycler_view.addOnScrollListener(scrollListener)

        swipeRefresh.setOnRefreshListener {
            loadDataRefresh()
        }
    }

    private fun checkSign(){
        if (AccessToken.getCurrentAccessToken() == null) { // Logout
            login_button.visibility = View.VISIBLE
            supportActionBar?.hide()
            ic_fb.visibility = View.VISIBLE
            login_button.isEnabled = true
            swipeRefresh.isEnabled = false
        }
        else{ // Login
            if(!isNetworkAvailable()){
                Toast.makeText(applicationContext, "not login", Toast.LENGTH_LONG).show()
//                arrData.addAll(dpHelper.getAllData())
                swipeRefresh.isEnabled = false
            }
            else {
                Toast.makeText(applicationContext, "login", Toast.LENGTH_LONG).show()
//                dpHelper.clearData()
                getGraph().executeAsync()
            }

            login_button.visibility = View.GONE
            supportActionBar?.show()
            ic_fb.visibility = View.GONE
            login_button.isEnabled = false
        }
    }

    private fun getGraph(): GraphRequest {
        return GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "me/feed?fields=created_time,likes.summary(true),attachments{},object_id,id,message,from{name,picture.height(300).width(300),id}&date_format=U",
            null,
            HttpMethod.GET, GraphRequest.Callback { response ->
                swipeRefresh.isRefreshing = true
                if(response.jsonObject != null){
                    login_button.visibility = View.GONE
                    login_button.isEnabled = false
                    ic_fb.visibility = View.GONE
                    supportActionBar?.show()
                    pareData(response.jsonObject)
                    lastGraphResponse = response
                    firstGraphResponse = response
                    if(arrData.size > 0)
                        dpHelper.insertData(arrData)
                } else
                    Toast.makeText(applicationContext , "null" , Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun pareData(jsonObject: JSONObject?) {
        Log.d("responsee" ,jsonObject.toString())
        val gsonBuilder = GsonBuilder()

        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a")
        val gson = gsonBuilder.create()
        if(gson?.fromJson(jsonObject.toString(), GraphObject::class.java) != null){
            result = gson.fromJson(jsonObject.toString(), GraphObject::class.java)
            if(result.data.size > 0){
                arrData.addAll(result.data)
                adapter.notifyItemRangeInserted(arrData.size , result.data.size - 1)
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
                Toast.makeText(applicationContext , "success" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    private fun loadDataRefresh() {
        swipeRefresh.isRefreshing = true
        if(isNetworkAvailable()) {
            arrData.clear()
        }
        val graphRequest = firstGraphResponse?.request
        if(graphRequest != null){
            graphRequest.callback = GraphRequest.Callback { response ->
                if(response.jsonObject != null){
                    pareData(response.jsonObject)
                    scrollListener.resetState()
                    Log.d("abc" , "refresh")
                    swipeRefresh.isRefreshing = false
                }
            }
        }
        else
            swipeRefresh.isRefreshing = false

        graphRequest?.executeAsync()
    }

    private fun loadNextData() {
        val nextResultsRequests = lastGraphResponse?.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT)
        if (nextResultsRequests != null) {
            nextResultsRequests.callback = GraphRequest.Callback { response ->

                pareData(response.jsonObject)
                lastGraphResponse = response
            }
            nextResultsRequests.executeAsync()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menuLogout){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("are you sure you want to logout ?")
            alertDialog.setNegativeButton("no") { _, _ -> }
            alertDialog.setPositiveButton("yes") { _, _ ->
                Toast.makeText(applicationContext, "logout", Toast.LENGTH_LONG).show()
                LoginManager.getInstance().logOut()
                ic_fb.visibility = View.VISIBLE
                login_button.visibility = View.VISIBLE
                login_button.isEnabled = true
                supportActionBar?.hide()
                arrData.clear()
                adapter.notifyDataSetChanged()
                swipeRefresh.isEnabled = false
                //                    dpHelper.clearData()
            }
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loginWithFB(){
        AppEventsLogger.activateApp(applicationContext)
        loginButton.setReadPermissions("email")
        loginButton.setReadPermissions("user_posts")

        loginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                getGraph().executeAsync()
                swipeRefresh.isEnabled = true
                checkSign()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext , "cancel connect" , Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, "error connect", Toast.LENGTH_LONG).show()
                Log.d("Error" , error!!.message.toString())
            }

        } )
    }
}
