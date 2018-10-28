package com.android.moneytap.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.moneytap.About;
import com.android.moneytap.R;
import com.android.moneytap.common.CustomFilter;
import com.android.moneytap.common.ItemClickListener;
import com.android.moneytap.pojo.DataModel;
import com.squareup.picasso.Picasso;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Umadevi Kuppanagari
 */
public class MoneyTapAdapter extends RecyclerView.Adapter<MoneyTapAdapter.MoneyTapViewHolder> implements Filterable {

    private static final String TAG = MoneyTapAdapter.class
            .getSimpleName();
    public ArrayList<DataModel> dataSet;
    ArrayList<DataModel> filterList;

    private Context mContext;
    CustomFilter filter;

    public MoneyTapAdapter(ArrayList<DataModel> mDataSet, Context context) {
        this.dataSet = mDataSet;
        mContext = context;
        Log.i(TAG, "dataSet" + dataSet);
        this.filterList = mDataSet;


    }

    @Override
    public MoneyTapViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customitem, parent, false);
        //HOLDER
        MoneyTapViewHolder myViewHolder = new MoneyTapViewHolder(view);
        return myViewHolder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(MoneyTapViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        //BIND DATA

        if (dataSet
                .get(position)
                .getName() != null) {

            Log.i(TAG, "Iff Block getName::" + dataSet.get(position).getName());

            holder.scientistName.setText(dataSet.get(position).getName());

        } else {

            Log.i(TAG, "Else Block getName" + dataSet.get(position).getName());

            holder.scientistName.setText("N/A");

        }

        if (dataSet
                .get(position)
                .getDescription() != null) {

            Log.i(TAG, "Iff Block getDescription::" + dataSet.get(position).getDescription());

            holder.descriptionView.setText(dataSet.get(position).getDescription());

        } else {

            Log.i(TAG, "Else Block getDescription" + dataSet.get(position).getDescription());

            holder.descriptionView.setText("N/A");

        }
        if (dataSet
                .get(position)
                .getImage() != null && !dataSet.get(position).getImage().toString().isEmpty()) {

            Log.i(TAG, "Iff Block getImage::" + dataSet.get(position).getImage());

            Picasso.get().load(dataSet.get(position).getImage()).into(holder.imageViewIcon);

        } else {

            Log.i(TAG, "Else Block getImage" + dataSet.get(position).getImage());

           holder.imageViewIcon.setImageResource(R.mipmap.profile);

            //Picasso.get().load(R.mipmap.ic_launcher).resize(120, 60).into(holder.imageViewIcon);


        }
        //IMPLEMENT CLICK LISTENET
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Snackbar.make(v, dataSet.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, About.class);
                intent.putExtra("name", dataSet
                        .get(pos)
                        .getName());
                intent.putExtra("desc", dataSet
                        .get(pos)
                        .getDescription());
                intent.putExtra("imgUrl", dataSet
                        .get(pos)
                        .getImage());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }
    public class MoneyTapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //OUR VIEWS
         ImageView imageViewIcon;
         TextView scientistName,descriptionView;

        ItemClickListener itemClickListener;


        public MoneyTapViewHolder(View itemView) {
            super(itemView);

            this.imageViewIcon= (ImageView) itemView.findViewById(R.id.scientist_image);
            this.scientistName= (TextView) itemView.findViewById(R.id.scientistName_txt);
            this.descriptionView= (TextView) itemView.findViewById(R.id.description_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());

        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
    }

}