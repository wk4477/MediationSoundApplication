package com.example.meditationsoundmixture;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.meditationsoundmixture.R;

final class ItemViewHolder extends RecyclerView.ViewHolder {

    final View rootView;
    final ImageView textViewSessionSlot;
    final TextView text;

    ItemViewHolder(@NonNull final View view) {
        super(view);
        rootView = view;
        textViewSessionSlot = view.findViewById(R.id.textViewSessionSlot);
        text=view.findViewById(R.id.textslot);
    }
}
