package org.tensorflow.lite.examples.detection.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.logic.Card;

import java.util.LinkedList;
import java.util.Objects;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private LinkedList<Card> dataset;
    private Context context;

    public CardListAdapter(LinkedList dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    public Dialog CreateDialog(int index, int arrayID, boolean suit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final int[] choice = new int[1];

        builder
                .setSingleChoiceItems(arrayID, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice[0] = i;
                    }
                }).setPositiveButton("ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (suit) {
                            dataset.get(index).fixSuit(context.getResources().getStringArray(arrayID)[choice[0]]);
                            notifyItemChanged(index);
                        } else {
                            dataset.get(index).fixRank(context.getResources().getStringArray(arrayID)[choice[0]]);
                            notifyItemChanged(index);
                        }


                    }
                }).setNegativeButton("nej", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button cardSuit;
        private final Button cardRank;

        public ViewHolder(View view) {
            super(view);

            cardSuit = view.findViewById(R.id.textView_card_suit);
            cardRank = view.findViewById(R.id.textView_card_rank);
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


        if (Objects.equals(card.getSuit(), "H")) {
            viewHolder.cardRank.setBackground();
        } else if (Objects.equals(card.getSuit(), "C")) {
            viewHolder.cardRank.setBackground();
        } else if (Objects.equals(card.getSuit(), "S")) {
            viewHolder.cardRank.setBackground();
        } else if (Objects.equals(card.getSuit(), "D")) {
            viewHolder.cardRank.setBackground();
        }

        //viewHolder.cardRank.setText(card.getRank());
        viewHolder.cardSuit.setText(card.getSuit());


        viewHolder.itemView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataset.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.itemView.findViewById(R.id.textView_card_suit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialog(viewHolder.getAdapterPosition(), R.array.suits, true).show();
            }
        });
        viewHolder.itemView.findViewById(R.id.textView_card_rank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDialog(viewHolder.getAdapterPosition(), R.array.ranks, false).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}