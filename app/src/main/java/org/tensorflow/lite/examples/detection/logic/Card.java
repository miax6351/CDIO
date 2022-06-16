package org.tensorflow.lite.examples.detection.logic;

public class Card {
    private String title;
    private boolean lockedPosition = false;

    public Card(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getRank() {
        if (title.length() > 2) {
            return title.substring(0, 2);
        } else {
            return title.substring(0, 1);
        }
    }

    public String getSuit() {
        return title.substring(title.length() - 1);
    }

    public String getCardMatch(int i, char c) {
        String returnText = "";
        if (i == 1) return "A" + c + "";
        if (i == 11) return "J" + c + "";
        else if (i == 12) return "Q" + c + "";
        else if (i == 13) return "K" + c + "";
        return (String) (returnText + i) + c + "";
    }
    public static char getCardColor(Card card) {

        if (card.getTitle().charAt(1) != '0') {
            return card.getTitle().charAt(1);
        }
        return card.getTitle().charAt(2);
    }


    public int getCardNumber() {

        char[] toArray = this.getTitle().toCharArray();
        if ((char) (toArray[0]) == 'A')
            return 1;
        if ((char) (toArray[0]) == 'J')
            return 11;
        else if ((char) (toArray[0]) == 'Q')
            return 12;
        else if ((char) (toArray[0]) == 'K')
            return 13;
        else if (((char) (toArray[0]) == '1') && ((char) (toArray[1]) == '0'))
            return 10;
        return Integer.parseInt(toArray[0] + "");
    }

    public boolean isCardCanBeUsed(Card resultCard) {
        int number, number1;
        char color;
        String cardMatch1;
        String cardMatch2;
        String temp = "";
        number = resultCard.getCardNumber();
        color = getCardColor(resultCard);
        if (resultCard.isKing()){
            return false;
        }
        number1 = number + 1;
        if (color == 'h' || color == 'd') {
            cardMatch1 = getCardMatch(number1, 'c').trim();
            cardMatch2 = getCardMatch(number1, 's').trim();
        }  else {
            cardMatch1 = getCardMatch(number1, 'h').trim();
            cardMatch2 = getCardMatch(number1, 'd').trim();
        }
        if (this.getTitle().equalsIgnoreCase(cardMatch1) || this.getTitle().equalsIgnoreCase(cardMatch2))
            return true;
        return false;
    }

    public char getCardColor() {

        if (this.getTitle().charAt(1) != '0') {
            return this.getTitle().charAt(1);
        }
        return this.getTitle().charAt(2);
    }



    public Boolean isKing(){
        if (this.getTitle().equals("Kh") || this.getTitle().equals("Kd") || this.getTitle().equals("Kc") || this.getTitle().equals("Ks")) {
           return true;
        }
        return false;
    }

    public void fixCard(String title) {
        this.title = title;
    }
    public void fixSuit(String suit){
        title = getRank() + suit;
    }

    public void fixRank(String rank){
        title = rank + getSuit();
    }
    public void setLockedPosition(boolean lockedPosition) {
        this.lockedPosition = lockedPosition;
    }

    public boolean getLockedPosition(){
        return lockedPosition;
    }
}
