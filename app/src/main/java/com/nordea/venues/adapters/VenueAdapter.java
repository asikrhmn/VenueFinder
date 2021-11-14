package com.nordea.venues.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nordea.venues.R;
import com.nordea.venues.entities.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {

    private final ArrayList<Venue> dataList = new ArrayList<>();

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_venue, parent, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, final int position) {
        Venue venue = dataList.get(position);
        holder.txtName.setText(venue.getName());
        holder.txtAddress.setText(venue.getAddress());
        holder.txtDistance.setText(venue.getDistanceAsString());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<Venue> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtAddress, txtDistance;

        VenueViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtDistance = itemView.findViewById(R.id.txt_distance);
        }
    }
}