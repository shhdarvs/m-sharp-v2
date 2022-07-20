package com.example.msharp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder>{
    private ArrayList<SavedItem> myList;
    private OnItemClickListener myListener;

    public interface OnItemClickListener
    {
        void onItemClicked(int position);
        void onDeleteClicked(int position);
        void onEditClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        myListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView myTextView;
        public ImageView mDeleteImage;
        public ImageView mEditImage;
        public MyViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView);
            mDeleteImage = itemView.findViewById(R.id.image_delete);
            mEditImage = itemView.findViewById(R.id.image_edit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClicked(position);
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClicked(position);
                        }
                    }
                }
            });

            mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onEditClicked(position);
                        }
                    }
                }
            });
        }


    }

    public MainActivityAdapter(ArrayList<SavedItem> List) {
        myList = List;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item, parent, false);
        MyViewHolder mvh = new MyViewHolder(v, myListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SavedItem currentItem = myList.get(position);

        holder.myTextView.setText(currentItem.getText());
    }


    @Override
    public int getItemCount() {
        return myList.size();
    }

}
