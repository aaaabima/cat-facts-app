package id.aaaabima.chucknorrisjokes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import id.aaaabima.chucknorrisjokes.databinding.ItemJokeLayoutBinding
import id.aaaabima.chucknorrisjokes.ui.model.JokeModel
import id.aaaabima.chucknorrisjokes.ui.viewholder.JokesViewHolder

class JokesAdapter: ListAdapter<JokeModel, JokesViewHolder>(JokeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        return JokesViewHolder(
            ItemJokeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class JokeDiffCallback: DiffUtil.ItemCallback<JokeModel>() {
        override fun areItemsTheSame(oldItem: JokeModel, newItem: JokeModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JokeModel, newItem: JokeModel): Boolean {
            return oldItem == newItem
        }
    }
}