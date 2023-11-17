package com.example.androidproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.room.Wallet


class WalletAdapter(private var walletList : ArrayList<Wallet>) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletAdapter.WalletViewHolder {
//        Log.d("walletFrag", "in onCreateViewHolder$walletList")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)
        return WalletViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WalletAdapter.WalletViewHolder, position: Int) {
//        Log.d("walletFrag", "in onBindViewHolder$walletList")
        val currentItem = walletList[position]
        holder.walletName.text = currentItem.walletName
        holder.walletCurrency.text = currentItem.walletCurrency
        holder.walletAmount.text = currentItem.walletAmount.toString()
    }

    override fun getItemCount(): Int {
        Log.d("walletsFrag", "getItemCount: ${walletList.size}")
        return walletList.size
    }

    class WalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val walletName : TextView = itemView.findViewById(R.id.lblWalletName)
        val walletCurrency : TextView = itemView.findViewById(R.id.lblWalletCurrency)
        val walletAmount : TextView = itemView.findViewById(R.id.lblWalletAmount)
    }

    fun updateData(newWalletList: ArrayList<Wallet>) {
        walletList = newWalletList
        notifyDataSetChanged()
    }
}