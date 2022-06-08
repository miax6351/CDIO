package org.tensorflow.lite.examples.detection.logic;

public class Card {
    private String title;

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

    public int getCardNumber(String title) {
        char[] toArray = title.toCharArray();
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

    public void fixCard(String title) {
        this.title = title;
    }


}
