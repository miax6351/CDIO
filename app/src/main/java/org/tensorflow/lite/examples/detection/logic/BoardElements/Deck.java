package org.tensorflow.lite.examples.detection.logic.BoardElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is more for testing purpose. It act as a deck consisting of all the cards.
 */
public class Deck {
     private List<Card> cardList;
     private int numberOfCardsDeck = 24;

    public Deck(){
         this.cardList = new ArrayList<>();
    }


    public List<Card> cardsKnownStartGame(List<Card> firstCards){
         List<Card> deck = new ArrayList<>();
         for (Card card : firstCards){
              deck.add(card);
         }
         return deck;
    }

 // for testing
    public void buildDeckWithAllCards(){
         List<Card> listCards = new ArrayList<>();
         Card sa = new Card("As");
         Card s2 = new Card("2s");
         Card s3 = new Card("3s");
         Card s4 = new Card("4s");
         Card s5 = new Card("5s");
         Card s6 = new Card("6s");
         Card s7 = new Card("7s");
         Card s8 = new Card("8s");
         Card s9 = new Card("9s");
         Card s10 = new Card("10s");
         Card sj = new Card("Js");
         Card sq = new Card("Qs");
         Card sk = new Card("Ks");

         Card ha = new Card("Ah");
         Card h2 = new Card("2h");
         Card h3 = new Card("3h");
         Card h4 = new Card("4h");
         Card h5 = new Card("5h");
         Card h6 = new Card("6h");
         Card h7 = new Card("7h");
         Card h8 = new Card("8h");
         Card h9 = new Card("9h");
         Card h10 = new Card("10h");
         Card hj = new Card("Jh");
         Card hq = new Card("Qh");
         Card hk = new Card("Kh");

         Card da = new Card("Ad");
         Card d2 = new Card("2d");
         Card d3 = new Card("3d");
         Card d6 = new Card("6d");
         Card d4 = new Card("4d");
         Card d5 = new Card("5d");
         Card d7 = new Card("7d");
         Card d8 = new Card("8d");
         Card d9 = new Card("9d");
         Card d10 = new Card("10d");
         Card dj = new Card("Jd");
         Card dq = new Card("Qd");
         Card dk = new Card("Kd");

         Card ca = new Card("Ac");
         Card c2 = new Card("2c");
         Card c3 = new Card("3c");
         Card c4 = new Card("4c");
         Card c5 = new Card("5c");
         Card c6 = new Card("6c");
         Card c7 = new Card("7c");
         Card c8 = new Card("8c");
         Card c9 = new Card("9c");
         Card c10 = new Card("10c");
         Card cj = new Card("Jc");
         Card cq = new Card("Qc");
         Card ck = new Card("Kc");
         listCards = Arrays.asList(sa, s2, s3, s4, s5 ,s6 ,s6 ,s7, s8, s9, s10, sj, sq, sk, ha, h2, h3, h4, h5, h6, h7, h8, h9, h10, hj, hq, hk, da, d2, d3, d4, d5, d6, d7, d8, d9, d10, dj, dq, dk, ca, c2, c3, c4, c5, c6, c7, c8, c9, c10, cj, cq, ck);

         this.cardList = listCards;
    }

    public void shuffleDeck(){
        Collections.shuffle(this.cardList);
    }

    public Card getCard(int i){
         return cardList.get(i);
    }
}
