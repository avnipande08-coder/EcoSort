
        package com.example.ecosort;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecosort.Waste;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

        public class WasteAdapter
        extends RecyclerView.Adapter<WasteAdapter.WasteView> {

    ArrayList<Waste> wastes;

    ArrayList<Waste> filteredWastes;


    public WasteAdapter(ArrayList<Waste> wasteArrayList) {

        this.wastes = wasteArrayList;

        filteredWastes =
                new ArrayList<>(wasteArrayList);
    }


    public class WasteView
            extends RecyclerView.ViewHolder {

        TextView txtViewForWasteId,
                txtViewForWasteName;

        ImageView imgViewForWasteImage;


        public WasteView(@NonNull View itemView) {
            super(itemView);

            imgViewForWasteImage =
                    itemView.findViewById(
                            R.id.imgViewForWasteImage
                    );

            txtViewForWasteId =
                    itemView.findViewById(
                            R.id.txtViewForWasteId
                    );

            txtViewForWasteName =
                    itemView.findViewById(
                            R.id.txtViewForWasteName
                    );


            itemView.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent =
                                    new Intent(
                                            itemView.getContext(),
                                            WasteDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "waste",
                                    filteredWastes.get(
                                            getAdapterPosition()
                                    )
                            );

                            itemView.getContext()
                                    .startActivity(intent);
                        }
                    }
            );
        }
    }


    @NonNull
    @Override
    public WasteView onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(
                        parent.getContext()
                );

        View wasteView =
                layoutInflater.inflate(
                        R.layout.waste_view_holder,
                        parent,
                        false
                );

        return new WasteView(wasteView);
    }


    @Override
    public void onBindViewHolder(
            @NonNull WasteView holder,
            int position) {

        Waste waste =
                filteredWastes.get(position);

        holder.txtViewForWasteId.setText(
                waste.getId() + ""
        );

        holder.txtViewForWasteName.setText(
                waste.getName()
        );

        Glide.with(
                        holder.itemView.getContext()
                )
                .load(waste.getImage())
                .placeholder(
                        R.drawable.ic_launcher_background
                )
                .centerCrop()
                .into(
                        holder.imgViewForWasteImage
                );
    }


    @Override
    public int getItemCount() {

        return filteredWastes.size();
    }


    // SEARCH FUNCTION

    public void filter(String text) {

        filteredWastes.clear();

        if (text.isEmpty()) {

            filteredWastes.addAll(wastes);

        } else {

            String searchText =
                    text.toLowerCase();

            for (Waste waste : wastes) {

                if (waste.getName()
                        .toLowerCase()
                        .contains(searchText)) {

                    filteredWastes.add(waste);
                }
            }
        }

        notifyDataSetChanged();
    }
    public void filterByBinColor(String color) {

        filteredWastes.clear();

        if (color.equals("All")) {

            filteredWastes.addAll(wastes);

        } else {

            for (Waste waste : wastes) {

                if (waste.getBinColor() != null &&
                        waste.getBinColor().equalsIgnoreCase(color)) {

                    filteredWastes.add(waste);
                }
            }
        }

        notifyDataSetChanged();
    }


    // UPDATE DATA AFTER API RESPONSE

    public void updateList(ArrayList<Waste> newList) {

        this.wastes = new ArrayList<>(newList);

        this.filteredWastes = new ArrayList<>(newList);

        notifyDataSetChanged();
    }

            public void sortAZ() {

                Collections.sort(
                        filteredWastes,
                        new Comparator<Waste>() {

                            @Override
                            public int compare(
                                    Waste waste1,
                                    Waste waste2) {

                                return waste1.getName()
                                        .compareToIgnoreCase(
                                                waste2.getName()
                                        );
                            }
                        }
                );

                notifyDataSetChanged();
            }

            public void sortZA() {

                Collections.sort(
                        filteredWastes,
                        new Comparator<Waste>() {

                            @Override
                            public int compare(
                                    Waste waste1,
                                    Waste waste2) {

                                return waste2.getName()
                                        .compareToIgnoreCase(
                                                waste1.getName()
                                        );
                            }
                        }
                );

                notifyDataSetChanged();
            }
}

