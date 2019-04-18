package com.example.sharenlearn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteListViewHolder> implements Filterable {


    ArrayList<Notes> NotesList;
    ArrayList<Notes> newNotes ;
    Context context;

    public NotesListAdapter(Context context,ArrayList<Notes> NotesList)
    {
        this.NotesList = NotesList;
        this.newNotes = NotesList;
        this.context=context;
    }
    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // create a new view
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_list_item, viewGroup, false);

        NoteListViewHolder vh = new NoteListViewHolder(v);

        return vh;
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteListViewHolder noteListViewHolder, int i) {
        final Notes notes = newNotes.get(i);
        noteListViewHolder.tv_title.setText(notes.getTitle());
        noteListViewHolder.tv_category.setText(notes.getCategory());
        noteListViewHolder.tv_description.setText(notes.getDescription());
//        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(noteListViewHolder.notes_img);
//        Picasso.get().load("https://drive.google.com/file/d/14EPg3jMnIL6J3QDcKKPnTL2k-IyWfdNN/view?usp=sharing").into(noteListViewHolder.notes_img);

//        TextDrawable drawable = TextDrawable.builder()
//                .buildRect("A", Color.RED);
//        noteListViewHolder.notes_img.setImageDrawable(drawable);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
//        int color2 = generator.getColor("user@gmail.com")

// declare the builder object once.
        String title = notes.getTitle();
        TextDrawable drawable = TextDrawable.builder().buildRoundRect(String.valueOf(title.charAt(0)), color1 ,20);
//        noteListViewHolder.recyclerView.setBackgroundColor(color1);

// reuse the builder specs to create multiple drawables
        noteListViewHolder.notes_img.setImageDrawable(drawable);

        noteListViewHolder.bt_download.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = notes.getDownloadLink();
                        if (!url.startsWith("https://") && !url.startsWith("http://")){
                            url = "http://" + url;
                        }
                        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(openUrlIntent);
                        Toast.makeText(context,"Downloading : "+notes.getDownloadLink(),Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return newNotes.size();
    }

    public class NoteListViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,tv_description,tv_category,bt_download;
        ImageView notes_img;
//        RecyclerView recyclerView;

        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.notes_list_item_title);
            tv_description = itemView.findViewById(R.id.notes_list_item_Description);
            tv_category = itemView.findViewById(R.id.notes_list_item_category);
            bt_download = itemView.findViewById(R.id.notes_list_item_download);
            notes_img = itemView.findViewById(R.id.img_notes);
//            recyclerView = itemView.findViewById(R.id.rl_parent_notes_layout);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                ArrayList<Notes> nNotes= new ArrayList<>();
                if(query.isEmpty())
                        nNotes = NotesList;
                else
                {
                    for(Notes note : NotesList)
                    {
                        if( note.getTitle().toLowerCase().contains(query.toLowerCase()) )
//                                ||
//                            note.getCategory().toLowerCase().contains(query.toLowerCase()) ||
//                            note.getDescription().toLowerCase().contains(query.toLowerCase()) )
                        {
                            nNotes.add(note);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = nNotes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newNotes = (ArrayList<Notes>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
