package com.example.android.roomwordssample

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class QuoteListAdapter internal constructor(
        private val context: Context,
        private var quoteList: List<Quote>
) : RecyclerView.Adapter<QuoteListAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Quote>() // Cached copy of words

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.character_text)!!
        val txtQuote = itemView.findViewById<TextView>(R.id.quote_text)!!
        val charImage = itemView.findViewById<ImageView>(R.id.image)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
//        return ViewHolder(itemView)
        val v = when (viewType) {
            TYPE_LEFT -> inflater.inflate(R.layout.item_left_layout, parent, false)
            else -> inflater.inflate(R.layout.item_right_layout, parent, false)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val current = words[position]
//        holder.quoteItemView.text = current.quote
        holder.txtName.text = quoteList[position].character
        holder.txtQuote.text = quoteList[position].quote

        Picasso.with(context).load(quoteList[position].image)
                .placeholder(R.drawable.simpsons_placeholder)
                .fit()
                .into(holder.charImage)
    }

//    internal fun setWords(words: List<Quote>) {
//        this.words = words
//        notifyDataSetChanged()
//    }

    override fun getItemCount() = words.size

    override fun getItemViewType(position: Int): Int {
        return when (quoteList[position].direction) {
            ViewDirection.VIEW_LEFT.direction -> TYPE_LEFT
            else -> TYPE_RIGHT
        }
    }

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }
}

