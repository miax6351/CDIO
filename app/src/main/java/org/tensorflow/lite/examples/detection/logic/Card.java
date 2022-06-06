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

    public void setLockedPosition(boolean lockedPosition) {
        this.lockedPosition = lockedPosition;
    }

    public boolean getLockedPosition(){
        return lockedPosition;
    }
}
