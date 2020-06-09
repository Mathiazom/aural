package com.mzom.aural.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzom.aural.R;
import com.mzom.aural.models.Playable;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PlayablesRecyclerAdapter extends ListAdapter<Playable, PlayablesRecyclerAdapter.PlayableHolder> {


    private final PlayablesRecyclerAdapterCallback callback;

    public interface PlayablesRecyclerAdapterCallback{

        void onPlayableClicked(final Playable playable);

    }

    public PlayablesRecyclerAdapter(final PlayablesRecyclerAdapterCallback callback) {

        super(Playable.DIFF_CALLBACK);

        this.callback = callback;

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public PlayableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayableHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.playable_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayableHolder holder, int position) {

        holder.bindTo(getItem(position));

    }



    class PlayableHolder extends RecyclerView.ViewHolder {

        private final TextView playableTitle;
        private final Guideline progressGuideline;

        PlayableHolder(@NonNull View itemView) {
            super(itemView);

            this.playableTitle = itemView.findViewById(R.id.playable_item_title);
            this.progressGuideline = itemView.findViewById(R.id.playable_item_progress_guideline);

        }

        private void bindTo(final Playable playable){

            playableTitle.setText(playable.getTitle());
            progressGuideline.setGuidelinePercent(playable.getProgress() / (float) playable.getDuration());

            itemView.setOnClickListener(view -> callback.onPlayableClicked(playable));

        }

    }


}
