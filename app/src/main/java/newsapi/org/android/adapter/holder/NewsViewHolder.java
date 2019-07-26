package newsapi.org.android.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import newsapi.org.android.databinding.NewsItemViewBinding;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public NewsItemViewBinding binding;
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        binding= DataBindingUtil.bind(itemView);
    }
}
