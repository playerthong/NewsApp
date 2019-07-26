package newsapi.org.android.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import newsapi.org.android.databinding.LoadingItemViewBinding;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public LoadingItemViewBinding binding;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        binding= DataBindingUtil.bind(itemView);
    }
}
