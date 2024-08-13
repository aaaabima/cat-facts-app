package id.aaaabima.chucknorrisjokes.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.aaaabima.chucknorrisjokes.databinding.ItemJokeLayoutBinding
import id.aaaabima.chucknorrisjokes.ui.model.JokeModel

class JokesViewHolder(
    private val binding: ItemJokeLayoutBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(jokeModel: JokeModel) {
        binding.tvJoke.text = jokeModel.value
        Glide.with(binding.root.context)
            .load(jokeModel.iconUrl)
            .centerCrop()
            .into(binding.ivPhoto)
    }
}