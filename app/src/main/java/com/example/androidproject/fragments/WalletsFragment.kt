package com.example.androidproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.activities.WalletActivity
import com.example.androidproject.adapters.WalletAdapter
import com.example.androidproject.room.Wallet
import com.example.androidproject.room.WalletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WalletsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var walletAdapter : WalletAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var walletList: ArrayList<Wallet>
    private val walletDatabase by lazy { WalletDatabase.getDatabase(requireContext()).walletDao() }

    private suspend fun getWalletsFromDB() {
        withContext(Dispatchers.IO) {
            walletList = ArrayList(walletDatabase.getAllWallets())
        }
    }
    private fun initializeRecyclerView(view: View) {
        walletAdapter = WalletAdapter(walletList)
        recyclerView = view.findViewById(R.id.recycler_wallet)
        val recyclerLayout = LinearLayoutManager(context)
        recyclerView.layoutManager = recyclerLayout
        recyclerView.adapter = walletAdapter
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            getWalletsFromDB()
            initializeRecyclerView(view)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wallet, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_wallet -> {
                val intent = Intent(requireActivity(), WalletActivity::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallets, container, false)
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        lifecycleScope.launch(Dispatchers.Main) {
            getWalletsFromDB()
            walletAdapter.updateData(walletList) // Set the updated list to the adapter
//            walletAdapter.notifyDataSetChanged()
            Log.d("WalletsFragment", "Data refreshed: $walletList")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}