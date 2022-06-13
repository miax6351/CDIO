/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Stack;

import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.logic.Card;
import org.tensorflow.lite.examples.detection.logic.SOLITARE_STATES;
import org.tensorflow.lite.examples.detection.tflite.Classifier;
import org.tensorflow.lite.examples.detection.tflite.DetectorFactory;
import org.tensorflow.lite.examples.detection.tflite.YoloV5Classifier;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    //confidence level where the card recognized is accepted. To avoid wrong recognition
    private static final float RECOGNIZED_CARD_CONFIDENCE = 0.9f;
    private static final boolean MAINTAIN_ASPECT = true;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(200, 640);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private YoloV5Classifier detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    private Snackbar snackbar;

    private boolean continueGame = true;

    /*
    Rows
     */
    public static boolean TESTMODE = false;
    private static LinkedList[] cardColumns = null;
    public static LinkedList recognizedCards = new LinkedList<Card>();
    public static Stack deckCards = new Stack<Card>();
    public static ArrayList<String> cardMoves = new ArrayList<String>();

    //Test//
    public static List<Card> fromTest = new LinkedList<Card>();
    public static int toEmptyTest = -1;
    public static Card toTest;
    public static Boolean pickupDeckCardTest = false;
    public static Boolean moveCardTest = false;
    public static Card fromDeckTest;
    public static  Boolean drawTest = false;
    public static Boolean moveToFoundationTest = false;
    public static Boolean moveCardColoumnTest = true;
    public static int roundCounterTest = 0;
    //Test//

    private static int PHASE_CHANGE_COUNTER = 0;

    private static int cardColumnCounter = 0;
    public static SOLITARE_STATES gameState = SOLITARE_STATES.INITIAL;
    private static Card movingCard;
    private static int waitTimeCount = 0;
    private static int emptyColoumn = -1;


    /*
    Foundation piles
     */
    private static LinkedList<Card> spades = new LinkedList<>();
    private static LinkedList<Card> clubs = new LinkedList<>();
    private static LinkedList<Card> hearts = new LinkedList<>();
    private static LinkedList<Card> diamonds = new LinkedList<>();
    private static LinkedList<Card> finishedCard = new LinkedList<>();

    //Deck
    //den samlede mængde af kort i deck og talon skal være 3 eller over ellers kan kabalen ikke løses.
    private static int numberOfCardsDeck = 24;
    private static int numberOfCardsTalon = 0;

    public void initializeCardColumns() {
        if (cardColumns == null) {
            cardColumns = new LinkedList[7];
            for (int i = 0; i < 7; i++) {
                cardColumns[i] = new LinkedList<Card>();
            }
        }
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        final int modelIndex = modelView.getCheckedItemPosition();
        final String modelString = modelStrings.get(modelIndex);

        try {
            detector = DetectorFactory.getDetector(getAssets(), modelString);
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        int cropSize = detector.getInputSize();

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }

    protected void updateActiveModel() {
        // Get UI information before delegating to background
        final int modelIndex = modelView.getCheckedItemPosition();
        final int deviceIndex = deviceView.getCheckedItemPosition();
        String threads = threadsTextView.getText().toString().trim();
        final int numThreads = Integer.parseInt(threads);


        handler.post(() -> {
            if (modelIndex == currentModel && deviceIndex == currentDevice
                    && numThreads == currentNumThreads) {
                return;
            }
            currentModel = modelIndex;
            currentDevice = deviceIndex;
            currentNumThreads = numThreads;

            // Disable classifier while updating
            if (detector != null) {
                detector.close();
                detector = null;
            }

            // Lookup names of parameters.
            String modelString = modelStrings.get(modelIndex);
            String device = deviceStrings.get(deviceIndex);

            LOGGER.i("Changing model to " + modelString + " device " + device);

            // Try to load model.

            try {
                detector = DetectorFactory.getDetector(getAssets(), modelString);
                // Customize the interpreter to the type of device we want to use.
                if (detector == null) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.e(e, "Exception in updateActiveModel()");
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }


            if (device.equals("CPU")) {
                detector.useCPU();
            } else if (device.equals("GPU")) {
                detector.useGpu();
            } else if (device.equals("NNAPI")) {
                detector.useNNAPI();
            }
            detector.setNumThreads(numThreads);

            int cropSize = detector.getInputSize();
            croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

            frameToCropTransform =
                    ImageUtils.getTransformationMatrix(
                            previewWidth, previewHeight,
                            cropSize, cropSize,
                            sensorOrientation, MAINTAIN_ASPECT);

            cropToFrameTransform = new Matrix();
            frameToCropTransform.invert(cropToFrameTransform);
        });
    }

    @Override
    protected void processImage() {
        //initializing card columns
        initializeCardColumns();
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;
        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }


        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.i("Running detection on image " + currTimestamp);
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        Log.e("CHECK", "run: " + results.size());

                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(2.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                break;
                        }

                        final List<Classifier.Recognition> mappedRecognitions =
                                new LinkedList<Classifier.Recognition>();

                        for (final Classifier.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);

                                result.setLocation(location);
                                mappedRecognitions.add(result);

                                /*
                                GAME LOGIC
                                 */
                                if (result.getConfidence() >= RECOGNIZED_CARD_CONFIDENCE) {
                                        Card resultCard = new Card(result.getTitle().trim());
                                       // if (!recognizedCardsContains(resultCard)){
                                    if (gameState == SOLITARE_STATES.DISPLAY_HIDDEN_CARD)
                                        PHASE_CHANGE_COUNTER++;

                                            playGame(resultCard);
                                            printBoard();
                                            printMoves();
                                       // }
                                }



                            }
                        }

                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showFrameInfo(previewWidth + "x" + previewHeight);
                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showInference(lastProcessingTimeMs + "ms");
                                    }
                                });
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> detector.setUseNNAPI(isChecked));
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }

    public static int getCardNumber(Card card) {

        char[] toArray = card.getTitle().toCharArray();
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

    private static String getCardMatch(int i, char c) {
        String returnText = "";
        if (i == 1) return "A" + c + "";
        if (i == 11) return "J" + c + "";
        else if (i == 12) return "Q" + c + "";
        else if (i == 13) return "K" + c + "";
        return (String) (returnText + i) + c + "";
    }

    public static boolean isCardCanBeUsed(Card card, Card resultCard) {
        int number, number1;
        char color;
        String cardMatch1;
        String cardMatch2;
        String temp = "";
        number = getCardNumber(resultCard);
        color = getCardColor(resultCard);
        if (isKingMovable(resultCard)){
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
        if (card.getTitle().equalsIgnoreCase(cardMatch1) || card.getTitle().equalsIgnoreCase(cardMatch2))
            return true;
        return false;
    }

    public static Boolean isKingMovable(Card card){
        if (card.getTitle().equals("Kh") || card.getTitle().equals("Kd") || card.getTitle().equals("Kc") || card.getTitle().equals("Ks")) {
            for (int i = 0; i < 7; i++){
                //Så denne funktion bliver kaldt konstant hvilket betyder at den fylder alle de tomme rækker ud
                //med en konge så hvis række 1 og 2 er tomme bliver den fyldt med to gange kh.
                //hovedfunktionen tjekker alle rækker i gennem så den bliver basically kaldt 7 gange i træk.
                if (cardColumns[i].isEmpty()){
                    emptyColoumn = i;
                    return true;
                }
            }
        }
        return false;
    }

    public static char getCardColor(Card card) {

        if (card.getTitle().charAt(1) != '0') {
            return card.getTitle().charAt(1);
        }
        return card.getTitle().charAt(2);
    }

    private SOLITARE_STATES handleCheckShownCards() {
     /*   int number, number1;
        char color;
        String cardMatch1 = "";
        String cardMatch2 = "";
        Card temp;
*/
        // first check if a card can be removed and put into finished card queue
        for (int i = 0; i < 7; i++) {
            if((!cardColumns[i].isEmpty()) && cardsToFoundationPile((Card) cardColumns[i].getLast())) {
                // this opened card should be moved out to finished card queue
                if(cardColumns[i].isEmpty()) {
                    // this is the last card in the list
                    return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                }
                // card moved to finish queue, check if other card can be moved
                return SOLITARE_STATES.ANALYZE_CARD_MOVE;
            }

            //Card can be moved to another column
            else{
            for (int j = 0; j < 7; j++){
                if (cardColumns[i].isEmpty()){
                    continue;
                }
                if (cardColumns[j].isEmpty() || (i == j))
                    continue;
                if(isCardCanBeUsed((Card) cardColumns[j].getLast(),(Card) cardColumns[i].getFirst()) && i!=j){
                    movingCard = (Card) cardColumns[i].getFirst();
                    //for (int k = 0; k < 5; k++) {
                    //waitNSeconds(1);
                    waitPlayerOption("Move " + movingCard.getTitle() + " to " + ((Card) cardColumns[j].getLast()).getTitle());
                    System.out.println("***************** CARD " + movingCard.getTitle() + " CAN BE MOVED TO " + ((Card) cardColumns[j].getLast()).getTitle() + " ************");
                    //MyResult myResult = new MyResult(movingCard, ((Card) cardColumns[j].getLast()));
                    cardMoves.add(movingCard.getTitle() + "-" + (j+1));
                    moveCardColoumnTest = true;
                    fromTest.clear();
                    fromTest.addAll(cardColumns[i]);
                    toTest = ((Card) cardColumns[j].getLast());
                    //}
                    cardColumns[j].addAll(cardColumns[i]);
                    cardColumns[i].clear();
                    return SOLITARE_STATES.DISPLAY_HIDDEN_CARD;
                }
            }
            }
        }
        // then check if some card can be move to another list
            /*number = getCardNumber((Card) cardColumns[i].getFirst());
            color = getCardColor( ((Card) cardColumns[i].getFirst()));
            number1 = number + 1;*/

            // check if king is shown and can be moved to empty column
           /* if (cardColumns[i].isEmpty()){
                for (int j = 0; j < 7; j++) {
                    String kings = ((Card) cardColumns[j].getFirst()).getTitle();
                    if ((kings.equals("Kh") && kings.equals("Kd") && kings.equals("Kc") && kings.equals("Ks")) &&  !((Card) cardColumns[j].getFirst()).getLockedPosition()) {
                        System.out.println("------ move card " + cardColumns[j].getFirst() + "to card column " + i + "------");
                        cardColumns[j] = cardColumns[i];
                        ((Card) cardColumns[i].getFirst()).setLockedPosition(true);
                        cardColumns[i].clear();
                    }
                }
                continue;
            }*/

         /*   if (color == 'h' || color == 'd') {
                cardMatch1 = getCardMatch(number1, 'c').trim();
                cardMatch2 = getCardMatch(number1, 's').trim();
            } else {
                cardMatch1 = getCardMatch(number1, 'h').trim();
                cardMatch2 = getCardMatch(number1, 'd').trim();
            }*/
            //System.out.println("************ CARD MATCH 1 " + cardMatch1
            //  + "******** CARD MATCH 2: "+ cardMatch2 );
         /*   for (int j = 0; j < 7; j++) {

                temp = ((Card) cardColumns[j].getLast());
                if ((temp.getTitle().equals(cardMatch1.toLowerCase(Locale.ROOT))) || (temp.getTitle().equals(cardMatch2.toLowerCase()))) {

                }
            }*/
        // no card can be moved, then pickup new card
        return SOLITARE_STATES.PICKUP_DECK_CARD;
    }

    private void waitNSeconds(int i) {
        try {
            System.out.println("******* WAIT " + i + " SEC **********");
            Thread.sleep(i * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }

    public static boolean recognizedCardsContains(Card card) {
        ListIterator listIterator = recognizedCards.listIterator();
        while (listIterator.hasNext()) {
            Card columnCard =  (Card)listIterator.next();
            if (columnCard.getTitle().equals(card.getTitle())) {
                return true;
            }
        }
        return false;
    }

    private boolean canMoveCardFromDeck(){
        if(numberOfCardsDeck + numberOfCardsTalon == 3 && numberOfCardsTalon != 0){
            return false;
        }
        else{
            return true;
        }
    }
        private void moveCardFromDeck(){
            if(numberOfCardsTalon == 0){
                numberOfCardsDeck -= 3;
                numberOfCardsTalon = 3;
            }
            numberOfCardsTalon --;
        }



    private boolean cardsToFoundationPile(Card card) {
        boolean removeCard = false;
        char lastColor = getCardColor(card);
        switch (lastColor) {
            case 's':
                if (spades.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    spades.add(new Card("As"));
                    removeCard = true;
                } else if (!spades.isEmpty() && (getCardNumber(card) == getCardNumber(spades.getLast()) + 1)) {
                    spades.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'c':
                if (clubs.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    clubs.add(new Card("Ac"));
                    removeCard = true;

                } else if (!clubs.isEmpty() && (getCardNumber(card) == getCardNumber(clubs.getLast()) + 1)) {
                    clubs.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'h':
                if (hearts.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    hearts.add(new Card("Ah"));
                    removeCard = true;
                } else if (!hearts.isEmpty() && (getCardNumber(card) == getCardNumber(hearts.getLast()) + 1)) {
                    hearts.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
            case 'd':
                if (diamonds.isEmpty() && card.getTitle().toLowerCase(Locale.ROOT).charAt(0) == 'a') {
                    diamonds.add(new Card("Ad"));
                    removeCard = true;
                } else if (!diamonds.isEmpty() && (getCardNumber(card) == getCardNumber(diamonds.getLast()) + 1)) {
                    diamonds.add(new Card(card.getTitle()));
                    removeCard = true;
                }
                break;
        }
        if(removeCard) {
            for (int i = 0; i < 7; i++) {
                if(!cardColumns[i].isEmpty() && ((Card)cardColumns[i].getLast()).getTitle().equalsIgnoreCase(card.getTitle())) {
                    //TEST//
                    DetectorActivity.fromTest.clear();
                    //TEST//
                    cardColumns[i].remove(card);
                    break;
                }
            }
            //for(int i = 0; i < 5; i++) {
                waitPlayerOption("------ move card " + card.getTitle() + " to foundation pile ------");
                fromTest.clear();
                fromTest.add(card);
                moveToFoundationTest = true;
                //waitNSeconds(1);
           // }
        cardMoves.add(card.getTitle() + "-F");
        }
        return removeCard;
    }
    //returns two cards for tests, should return void when no testing.
    public void playGame(Card resultCard) {
        //TEST//

        pickupDeckCardTest = false;
        moveCardTest = false;
        drawTest = false;
        moveToFoundationTest = false;
        moveCardColoumnTest = false;
        emptyColoumn = -1;
        //TEST//
        roundCounterTest++;
        switch (gameState) {

            case INITIAL:
                if (!recognizedCardsContains(resultCard)){
                    System.out.println("RECOGNIZED SPECIFIC CARD:" + resultCard.getTitle());
                    recognizedCards.add(resultCard);
                    if (!TESTMODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardSuit.getAdapter().notifyItemInserted(recognizedCards.size());
                            }
                        });
                    }
                    cardColumns[cardColumnCounter].add(resultCard);
                    if (cardColumnCounter == 6) {
                        for (int i = 0; i < 7; i++) {
                            Card card = (Card) cardColumns[i].getFirst();
                            System.out.println("************ KNOWN CARDS IN COLUMN: " + i + "   "
                                    + card.getTitle().trim() + "**********");
                        }
                        //waitNSeconds(5);
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                        playGame((Card) (cardColumns[6].getFirst()));
                        return;
                    } else {
                        cardColumnCounter++;
                        waitPlayerOption("Film card on row " + (cardColumnCounter + 1));
                    }
                }

                break;

            case ANALYZE_CARD_MOVE:
                System.out.println("************* ENTER ANALYZE_CARD_MOVE_PHASE");
                PHASE_CHANGE_COUNTER = 0;
                gameState = handleCheckShownCards();
                break;

            case DISPLAY_HIDDEN_CARD:
                System.out.println("************* ENTER DISPLAY_HIDDEN_CARD ********");
                //Counter to change phase
                PHASE_CHANGE_COUNTER++;
                if (PHASE_CHANGE_COUNTER >= 10){
                    gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                    waitPlayerOption("Pick up new card from deck!");
                    break;
                }

                if (!recognizedCardsContains(resultCard)) {
                    recognizedCards.add(new Card(resultCard.getTitle()));
                    if (!TESTMODE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardSuit.getAdapter().notifyItemInserted(recognizedCards.size());
                            }
                        });
                    }
                    System.out.println("------- Find lately opened card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    for (int i = 0; i < 7; i++) {
                        if (cardColumns[i].isEmpty()){
                            cardColumns[i].add(resultCard);
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                        return;

                        }
                        if (i == 6){
                            System.out.println("----------------- No new card opened and no column to move, pickup new card from deck +++++++++++++++++++++++++++++++++++");
                            cardMoves.add("T");
                            gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                            break;
                            // pickupDeckCard = true;

                        }
                    }
                }


                break;
            case PICKUP_DECK_CARD:
                System.out.println("*************  ENTER PICKUP_DECK_CARD *****");
                //TEST//
                pickupDeckCardTest = true;
                moveCardTest = false;
                //TEST
                PHASE_CHANGE_COUNTER = 0;
                boolean cardCanBeUsed = false;
                if (!recognizedCardsContains(resultCard)){
                    System.out.println("-------- found a new card " + resultCard.getTitle() + "-------");
                    cardMoves.add("T");
                    if (!cardsToFoundationPile(resultCard)){
                        for (int i = 0; i < 7; i++) {
                            if ((!cardColumns[i].isEmpty()) && isCardCanBeUsed(((Card) cardColumns[i].getLast()), resultCard) && !finishedCard.contains(resultCard)) {
                                // add the new card to the list
                                Card oldListLastCard = ((Card) cardColumns[i].getLast());
                                cardColumns[i].addLast(resultCard);
                                recognizedCards.add(resultCard);
                                if (!TESTMODE){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cardSuit.getAdapter().notifyItemInserted(recognizedCards.size());
                                        }
                                    });
                                }
                                    waitPlayerOption("Move new card: " + resultCard.getTitle() +" to " + oldListLastCard.getTitle() );
                                    System.out.println("Move new card " + resultCard.getTitle() + "to" + oldListLastCard.getTitle());
                                    cardMoves.add(resultCard.getTitle() + "-" + ( i + 1));

                                fromDeckTest = resultCard;
                                toTest = ((Card) cardColumns[i].get(cardColumns[i].size()-2));
                                moveCardTest = true;
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                cardCanBeUsed = true;
                                break;
                            }
                            else if(emptyColoumn != -1){
                                // add the new card to the list
                                cardColumns[emptyColoumn].addLast(resultCard);
                                recognizedCards.add(new Card(resultCard.getTitle()));
                                //for (int k = 0; k < 10; k++) {
                                waitPlayerOption("Move new card: " + resultCard.getTitle() +" to " + "empty columnn" );
                                System.out.println("------ new card " + resultCard.getTitle() + " can be moved to " + "empty columnn" + "----------------------");

                                fromDeckTest = resultCard;
                                toEmptyTest = emptyColoumn;
                                moveCardTest = true;
                                emptyColoumn = -1;
                                cardCanBeUsed = true;
                                gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                                break;
                            }
                        }
                    }else{
                        cardCanBeUsed = true;
                        recognizedCards.add(resultCard);
                    }
                    if (!cardCanBeUsed) {
                        gameState = SOLITARE_STATES.PICKUP_DECK_CARD;
                        //for (int k = 0; k < 10; k++) {
                        waitPlayerOption(resultCard.getTitle() + " cannot be used anywhere, pick a new card.");
                        drawTest = true;
                        //  waitNSeconds(1);
                        //}
                    }else{
                        gameState = SOLITARE_STATES.ANALYZE_CARD_MOVE;
                    }
                }

                break;
            default:
                break;
        }
    }

    public void waitPlayerOption (String snackbarText) {
        if (TESTMODE == true){
            System.out.println(snackbarText);
            return;
        }

        continueGame = false;
        snackbar = Snackbar
                .make(findViewById(android.R.id.content).getRootView(), snackbarText, Snackbar.LENGTH_INDEFINITE)
                .setAction("Done" +
                        "\n\n\n\n", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        continueGame = true;
                        return;
                    }
                });
        snackbar.show();
        int inactiveCount = 0;
        while (!continueGame){
            inactiveCount++;
            // loop until player presses done
            if (inactiveCount >= 1000){
                continueGame = true;
                break;
            }

        }
    }
    final class MyResult{
        private final Card from;
        private final Card to;

        MyResult(Card from, Card to) {
            this.from = from;
            this.to = to;
        }
    }

    public void printBoard(){
        int biggestRow = 0;

        for (int k = 6; k >= 0; k--){
            if(biggestRow < cardColumns[k].size()){
                biggestRow = cardColumns[k].size();
            }
        }
        for(int j = 0; j < biggestRow; j++){
            for (int i = 0; i < 7; i++){
                try {
                    System.out.print(((Card) (cardColumns[i].get(j))).getTitle()+ "       ");
                } catch (Exception e){
                    System.out.print("         ");
                }
            }
            System.out.println();
        }
    }

    public void printMoves(){
        int last = 0;
        System.out.println("");
        for (String move : cardMoves){
            if (last == cardMoves.size() - 1){
                System.out.print(move);
            } else {
                System.out.print(" "+ move + " ,");
                last++;
            }
        }
        System.out.println("");
    }


}
