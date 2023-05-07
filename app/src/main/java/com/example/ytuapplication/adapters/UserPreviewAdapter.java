package com.example.ytuapplication.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ytuapplication.R;
import com.example.ytuapplication.models.UserPreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserPreviewAdapter extends RecyclerView.Adapter<UserPreviewAdapter.UserPreviewViewHolder> {

    private List<UserPreview> userPreviews;
    private Context context;

    public UserPreviewAdapter(Context context, List<UserPreview> userPreviews) {
        this.context = context;
        this.userPreviews = userPreviews;
    }

    @NonNull
    @Override
    public UserPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_preview, parent, false);
        return new UserPreviewViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull UserPreviewViewHolder holder, int position) {
        UserPreview currentUser = userPreviews.get(position);

        holder.fullName.setText(currentUser.getFullName());
        holder.email.setText(currentUser.getEmail());

        Uri profilePhoto = currentUser.getProfilePhoto();
        if (profilePhoto != null) {
            Picasso.get().load(profilePhoto).into(holder.profilePhoto);
        } else {
            holder.profilePhoto.setImageResource(R.drawable.default_profile_photo); // Set a default image if the user has no profile photo
        }
    }

    @Override
    public int getItemCount() {
        return userPreviews.size();
    }

    public static class UserPreviewViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePhoto;
        TextView fullName;
        TextView email;

        public UserPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.iv_profile_photo);
            fullName = itemView.findViewById(R.id.tv_full_name);
            email = itemView.findViewById(R.id.tv_email);
        }
    }
}

