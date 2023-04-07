package com.group.comicreader.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group.comicreader.ComicReaderActivity;
import com.group.comicreader.R;
import com.group.comicreader.models.Chapter;

import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {
    private List<Chapter> chaptersList;

    public ChapterListAdapter(List<Chapter> chapterList) {
        chaptersList = chapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chapter chapter = chaptersList.get(position);

        holder.mChapterNumber.setText(String.valueOf(chapter.getChapterNumber()));
        holder.mChapterTitle.setText(chapter.getTitle());
        holder.mChapterReleaseDate.setText(chapter.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mChapterTitle;
        public TextView mChapterNumber;
        public TextView mChapterReleaseDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mChapterTitle = itemView.findViewById(R.id.text_chapter_title);
            mChapterNumber = itemView.findViewById(R.id.text_chapter_number);
            mChapterReleaseDate = itemView.findViewById(R.id.text_chapter_release_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ComicReaderActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}
