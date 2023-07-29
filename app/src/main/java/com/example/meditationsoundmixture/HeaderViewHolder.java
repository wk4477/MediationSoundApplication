package com.example.meditationsoundmixture;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.meditationsoundmixture.R;

final class HeaderViewHolder extends RecyclerView.ViewHolder {

    final TextView textViewSessionHeader;
    HeaderViewHolder(@NonNull final View view) {
        super(view);
        textViewSessionHeader = view.findViewById(R.id.textViewSessionHeader);
    }
}
