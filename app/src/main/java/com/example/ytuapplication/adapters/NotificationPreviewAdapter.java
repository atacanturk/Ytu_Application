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
import com.example.ytuapplication.models.NotificationPreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationPreviewAdapter extends RecyclerView.Adapter<NotificationPreviewAdapter.NotificationPreviewViewHolder> {

    private Context context;
    private List<NotificationPreview> notificationPreviews;

    public NotificationPreviewAdapter(Context context, List<NotificationPreview> notificationPreviews) {
        this.context = context;
        this.notificationPreviews = notificationPreviews;
    }

    @NonNull
    @Override
    public NotificationPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_preview, parent, false);
        return new NotificationPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationPreviewViewHolder holder, int position) {
        NotificationPreview notificationPreview = notificationPreviews.get(position);
        holder.ownerName.setText(notificationPreview.getOwnerName());
        holder.title.setText(notificationPreview.getTitle());

        Uri photoUri = notificationPreview.getNotificationPhoto();
        if (photoUri != null) {
            Picasso.get().load(photoUri).into(holder.notificationPhoto);
        } else {
            holder.notificationPhoto.setImageResource(R.drawable.default_notification_photo); // Set a default image if no photo is available.
        }
    }

    @Override
    public int getItemCount() {
        return notificationPreviews.size();
    }

    public static class NotificationPreviewViewHolder extends RecyclerView.ViewHolder {

        public TextView ownerName, title;
        public ImageView notificationPhoto;

        public NotificationPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ownerName = itemView.findViewById(R.id.notification_owner_name);
            title = itemView.findViewById(R.id.notification_title);
            notificationPhoto = itemView.findViewById(R.id.notification_photo);
        }
    }
}
