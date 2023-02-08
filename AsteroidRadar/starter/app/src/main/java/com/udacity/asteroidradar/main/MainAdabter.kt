package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemBinding
import com.udacity.asteroidradar.main.MainAdabter.ViewHolder.Companion.from


class MainAdabter(private val clickListener: (asteroid: Asteroid) -> Unit):
    androidx.recyclerview.widget.ListAdapter<Asteroid, MainAdabter.ViewHolder>(AsteroidDiffCallback()) {

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent) as ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Asteroid, clickListener: (asteroid: Asteroid) -> Unit) {
            binding.asteroid = item
            binding.constraintLayoutItem.setOnClickListener { clickListener(item) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}