package org.tensorflow.lite.examples.detection.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private LinkedList<Card> dataset;

    public CardListAdapter (LinkedList dataset){
        this.dataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cardSuit;
        private final TextView cardRank;

        public ViewHolder(View view) {
            super(view);

            cardSuit = (TextView) view.findViewById(R.id.textView_card_suit);
            cardRank = (TextView) view.findViewById(R.id.textView_card_rank);
        }

        public TextView getCardSuit() {
            return cardSuit;
        }

        public TextView getCardRank() {
            return cardRank;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_card_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Card card = dataset.get(position);
        viewHolder.cardRank.setText(card.getRank());
        viewHolder.cardSuit.setText(card.getSuit());


        viewHolder.itemView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataset.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText cardSuit = (EditText) viewHolder.itemView.findViewById(R.id.textView_card_suit);
                EditText cardRank = (EditText) viewHolder.itemView.findViewById(R.id.textView_card_rank);


                dataset.get(viewHolder.getAdapterPosition()).
                        fixCard(cardRank.getText().toString() + cardSuit.getText().toString());

                 notifyItemChanged(viewHolder.getAdapterPosition());

            }
        });

        //viewHolder.getCardSuit().setText(dataset.get(position).getTitle());
        //viewHolder.getCardRank().setText(dataset.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}