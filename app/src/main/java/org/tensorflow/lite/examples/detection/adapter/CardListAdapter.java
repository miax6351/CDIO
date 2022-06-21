package org.tensorflow.lite.examples.detection.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;


import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.Game;
import org.tensorflow.lite.examples.detection.viewmodels.GameViewModel;

import java.util.LinkedList;
import java.util.Objects;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private GameViewModel gameViewModel;
    private LinkedList<Card> dataset;
    private Context context;

    public CardListAdapter(GameViewModel gameViewModel, Context context) {
        this.context = context;
        this.gameViewModel = gameViewModel;
        this.dataset = gameViewModel.getRecognizedCards();
    }




    public Dialog CreateDialog(int index, int arrayID, boolean suit) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final int[] choice = new int[1];

        builder.setSingleChoiceItems(arrayID, 5, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice[0] = i;
                if (suit) {
                    dataset.get(index).fixSuit(context.getResources().getStringArray(R.array.suitsText)[choice[0]]);
                } else {
                    dataset.get(index).fixRank(context.getResources().getStringArray(arrayID)[choice[0]]);
                }
                notifyItemChanged(index);
                dialogInterface.dismiss();


                String lastEditedCardTitle = dataset.get(index).getTitle();
                LinkedList<Card> cardsOnEditedCard = new LinkedList<Card>();
                for (int j = 0; j < 7; j++) {
                    for (int k = 0; k < Game.cardColumns[j].size(); k++) {

                        if (((Card) Game.cardColumns[j].get(k)).getTitle().equals(lastEditedCardTitle)) {
                            for (int h = k + 1; h < Game.cardColumns[j].size(); h++) {
                                cardsOnEditedCard.add((Card) Game.cardColumns[j].get(h));
                            }
                            if(!Game.cardColumns[j].isEmpty())
                                Game.cardColumns[j].removeAll(cardsOnEditedCard);
                        }

                    }
                }
                boolean hasAdded = false;
                int emptyIndex = 0;
                for (int j = 0; j < 7; j++) {
                    if(Game.cardColumns[j].isEmpty() && Game.hiddenCardsInColumns[j] > 0) {
                        hasAdded = true;
                        Game.cardColumns[j] = cardsOnEditedCard;
                        break;
                    }
                    else if(Game.cardColumns[j].isEmpty()) {
                        emptyIndex = j;
                    }
                }
                if(!hasAdded)
                    Game.cardColumns[emptyIndex] = cardsOnEditedCard;
            }
        });

       /* builder
                .setSingleChoiceItems(arrayID, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice[0] = i;
                    }
                }).setPositiveButton("ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (suit) {
                            dataset.get(index).fixSuit(context.getResources().getStringArray(R.array.suitsText)[choice[0]]);
                        } else {
                            dataset.get(index).fixRank(context.getResources().getStringArray(arrayID)[choice[0]]);
                        }
                        notifyItemChanged(index);


                    }
                }).setNegativeButton("nej", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });*/
        return builder.create();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton cardSuit;
        private final Button cardRank;
        public ViewHolder(View view) {
            super(view);

            cardSuit = view.findViewById(R.id.textView_card_suit);
            cardRank = view.findViewById(R.id.textView_card_rank);
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

        if (Objects.equals(card.getSuit(), "h")) {
            viewHolder.cardSuit.setImageResource(R.drawable.heart);
        } else if (Objects.equals(card.getSuit(), "c")) {
            viewHolder.cardSuit.setImageResource(R.drawable.club);

        } else if (Objects.equals(card.getSuit(), "s")) {
            viewHolder.cardSuit.setImageResource(R.drawable.spade);

        } else if (Objects.equals(card.getSuit(), "d")) {
            viewHolder.cardSuit.setImageResource(R.drawable.diamond);

        }

        viewHolder.cardRank.setText(card.getRank());
        //viewHolder.cardSuit.setText(card.getSuit());


        viewHolder.itemView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.recognizedCards.remove(viewHolder.getAdapterPosition());
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