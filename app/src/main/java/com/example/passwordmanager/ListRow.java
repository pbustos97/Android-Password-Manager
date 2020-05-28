package com.example.passwordmanager;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class ListRow extends RecyclerView.ViewHolder {
    public ImageView mThumbnail;
    public Button mDeleteButton;

    public ListRow(View view) {
        super(view);
        mDeleteButton = (Button) view.findViewById(R.id.password_delete);


    }

}
