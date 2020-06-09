package com.mzom.aural.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mzom.aural.R;
import com.mzom.aural.models.FolderWithPlayables;
import com.mzom.aural.models.Playable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class FoldersRecyclerAdapter extends ListAdapter<FolderWithPlayables, FoldersRecyclerAdapter.FolderHolder> {


    private static final String TAG = "AUR-FoldersRecyclerAdapter";


    private final FoldersRecyclerAdapterCallback callback;

    public interface FoldersRecyclerAdapterCallback{

        void onFolderClicked(final FolderWithPlayables folder);

    }


    public FoldersRecyclerAdapter(final FoldersRecyclerAdapterCallback callback) {

        super(FolderWithPlayables.DIFF_CALLBACK);

        this.callback = callback;

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getFolder().getId();
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {

        holder.bindTo(getItem(position));

    }

    class FolderHolder extends RecyclerView.ViewHolder {

        private final TextView folderTitle;
        private final TextView folderSize;

        FolderHolder(@NonNull View itemView) {
            super(itemView);

            this.folderTitle = itemView.findViewById(R.id.folder_item_title);
            this.folderSize = itemView.findViewById(R.id.folder_item_size);

        }

        private void bindTo(final FolderWithPlayables folder){

            folderTitle.setText(folder.getTitle());

            final List<Playable> playables = folder.getPlayables();

            if(playables != null){
                folderSize.setText(String.valueOf(playables.size()));
            }

            itemView.setOnClickListener(view -> callback.onFolderClicked(folder));

        }

    }


}
