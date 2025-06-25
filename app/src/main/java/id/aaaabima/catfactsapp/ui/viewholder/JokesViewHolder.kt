package id.aaaabima.catfactsapp.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import id.aaaabima.catfactsapp.databinding.ItemJokeLayoutBinding
import id.aaaabima.catfactsapp.ui.model.JokeModel

class JokesViewHolder(
    private val binding: ItemJokeLayoutBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(jokeModel: JokeModel) {
        binding.tvJoke.text = jokeModel.fact
    }
}