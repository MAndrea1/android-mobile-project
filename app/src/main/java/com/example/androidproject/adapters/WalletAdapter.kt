package com.example.androidproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.model.Wallet


class WalletAdapter(private val walletList : ArrayList<Wallet>) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletAdapter.WalletViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet, parent, false)
        return WalletViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WalletAdapter.WalletViewHolder, position: Int) {
        val currentItem = walletList[position]
        holder.walletName.text = currentItem.walletName
        holder.walletCurrency.text = currentItem.walletCurrency
        holder.walletAmount.text = currentItem.walletAmount.toString()
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    class WalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val walletName : TextView = itemView.findViewById(R.id.lblWalletName)
        val walletCurrency : TextView = itemView.findViewById(R.id.lblWalletCurrency)
        val walletAmount : TextView = itemView.findViewById(R.id.lblWalletAmount)
    }
}