package com.example.demento.briskula;
import android.media.Image;

import java.net.URL;

public class Card {

    public final String type; // coppe, dinare, bastoni, spade
    public final String name; //as, trica, kralj, konj, fanta
    public final int value; //as je 10, 3 je 9, king je 8, horse je 7, fante je 6, 7 je 5, 6-4, 5-3, 4-2, 3-1, 2-0
    public final int score;
    public final int cardImage;
    public final String cardID;
    public Integer indexInSpil;

    public Card(String Type, String Name, int Value, int Score, int cImage, String CardID) {

        this.type = Type;
        this.name = Name;
        this.value = Value;
        this.score = Score;
        this.cardImage = cImage;
        this.cardID = CardID;
        this.indexInSpil = null;
    }
}
