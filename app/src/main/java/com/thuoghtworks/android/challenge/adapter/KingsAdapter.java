package com.thuoghtworks.android.challenge.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thuoghtworks.android.challenge.Constants;
import com.thuoghtworks.android.challenge.KingDetailsActivity;
import com.thuoghtworks.android.challenge.R;
import com.thuoghtworks.android.challenge.db.King;

import java.util.List;

/**
 * Created by Govind
 */
public class KingsAdapter extends RecyclerView.Adapter<KingsAdapter.ViewHolder> {

    private final List<King> mValues;
    private OnCardClickListener mOnCardClickListener;

    public interface OnCardClickListener {
        void onCardClick(King king);
    }

    public KingsAdapter(Context context, List<King> items) {
        mValues = items;
    }

    @Override
    public KingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_king_item, parent, false);
        return new KingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KingsAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.kingName.setText(holder.mItem.getName());
        holder.totalRating.setText("Highest Rating: " + mValues.get(position).getRating());
        String strength = "Battle Strength: ";
        if(holder.mItem.getTotal_won_count() <= 0) {
            strength = "Never won a battle";
        } else if(holder.mItem.getAttack_won_count() > holder.mItem.getDefend_won_count()) {
            strength += "Attack";
        } else if(holder.mItem.getAttack_won_count() < holder.mItem.getDefend_won_count()) {
            strength += "Defence";
        } else if(holder.mItem.getAttack_won_count() == holder.mItem.getDefend_won_count()){
            strength += "Both";
        }
        holder.battleStrength.setText( strength);
        final Context context = holder.kingName.getContext();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(context, KingDetailsActivity.class);
                next.putExtra(Constants.KEY_KING, holder.mItem);
                context.startActivity(next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView kingName;
        public final TextView totalRating;
        public final TextView battleStrength;
        public King mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.main_view);
            kingName = (TextView) view.findViewById(R.id.king_name);
            totalRating = (TextView) view.findViewById(R.id.rating);
            battleStrength = (TextView) view.findViewById(R.id.battle_strength);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + kingName.getText() + "'";
        }
    }

}
