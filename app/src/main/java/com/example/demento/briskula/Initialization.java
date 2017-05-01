package com.example.demento.briskula;

import java.util.ArrayList;
import java.util.Collections;


public class Initialization {

    protected final ArrayList<Card> cardsSpil = new ArrayList<>();
    protected Card backCard = null;
    protected Card backRotatedCard = null;
    protected Card dummy = null;
    protected String dummyID = null;

    public Initialization() {

        cardsSpil.add(new Card("bastoni", "as", 10, 11, R.drawable.b10, "B10"));
        cardsSpil.add(new Card("bastoni", "trica", 9, 10, R.drawable.b9, "B9"));
        cardsSpil.add(new Card("bastoni", "kralj", 8, 4, R.drawable.b8, "B8"));
        cardsSpil.add(new Card("bastoni", "konj", 7, 3, R.drawable.b7, "B7"));
        cardsSpil.add(new Card("bastoni", "fanta", 6, 2, R.drawable.b6, "B6"));
        cardsSpil.add(new Card("bastoni", "sedmica", 5, 0, R.drawable.b5, "B5"));
        cardsSpil.add(new Card("bastoni", "sestica", 4, 0, R.drawable.b4, "B4"));
        cardsSpil.add(new Card("bastoni", "petica", 3, 0, R.drawable.b3, "B3"));
        cardsSpil.add(new Card("bastoni", "cetvorka", 2, 0, R.drawable.b2, "B2"));
        cardsSpil.add(new Card("bastoni", "dvojka", 1, 0, R.drawable.b1, "B1"));

        cardsSpil.add(new Card("dinari", "as", 10, 11, R.drawable.d10, "D10"));
        cardsSpil.add(new Card("dinari", "trica", 9, 10, R.drawable.d9, "D9"));
        cardsSpil.add(new Card("dinari", "kralj", 8, 4, R.drawable.d8, "D8"));
        cardsSpil.add(new Card("dinari", "konj", 7, 3, R.drawable.d7, "D7"));
        cardsSpil.add(new Card("dinari", "fanta", 6, 2, R.drawable.d6, "D6"));
        cardsSpil.add(new Card("dinari", "sedmica", 5, 0, R.drawable.d5, "D5"));
        cardsSpil.add(new Card("dinari", "sestica", 4, 0, R.drawable.d4, "D4"));
        cardsSpil.add(new Card("dinari", "petica", 3, 0, R.drawable.d3, "D3"));
        cardsSpil.add(new Card("dinari", "cetvorka", 2, 0, R.drawable.d2, "D2"));
        cardsSpil.add(new Card("dinari", "dvojka", 1, 0, R.drawable.d1, "D1"));

        cardsSpil.add(new Card("spade", "as", 10, 11, R.drawable.s10, "S10"));
        cardsSpil.add(new Card("spade", "trica", 9, 10, R.drawable.s9, "S9"));
        cardsSpil.add(new Card("spade", "kralj", 8, 4, R.drawable.s8, "S8"));
        cardsSpil.add(new Card("spade", "konj", 7, 3, R.drawable.s7, "S7"));
        cardsSpil.add(new Card("spade", "fanta", 6, 2, R.drawable.s6, "S6"));
        cardsSpil.add(new Card("spade", "sedmica", 5, 0, R.drawable.s5, "S5"));
        cardsSpil.add(new Card("spade", "sestica", 4, 0, R.drawable.s4, "S4"));
        cardsSpil.add(new Card("spade", "petica", 3, 0, R.drawable.s3, "S3"));
        cardsSpil.add(new Card("spade", "cetvorka", 2, 0, R.drawable.s2, "S2"));
        cardsSpil.add(new Card("spade", "dvojka", 1, 0, R.drawable.s1, "S1"));

        cardsSpil.add(new Card("coppe", "as", 10, 11, R.drawable.c10, "C10"));
        cardsSpil.add(new Card("coppe", "trica", 9, 10, R.drawable.c9, "C9"));
        cardsSpil.add(new Card("coppe", "kralj", 8, 4, R.drawable.c8, "C8"));
        cardsSpil.add(new Card("coppe", "konj", 7, 3, R.drawable.c7, "C7"));
        cardsSpil.add(new Card("coppe", "fanta", 6, 2, R.drawable.c6, "C6"));
        cardsSpil.add(new Card("coppe", "sedmica", 5, 0, R.drawable.c5, "C5"));
        cardsSpil.add(new Card("coppe", "sestica", 4, 0, R.drawable.c4, "C4"));
        cardsSpil.add(new Card("coppe", "petica", 3, 0, R.drawable.c3, "C3"));
        cardsSpil.add(new Card("coppe", "cetvorka", 2, 0, R.drawable.c2, "C2"));
        cardsSpil.add(new Card("coppe", "dvojka", 1, 0, R.drawable.c1, "C1"));

        backCard = new Card("back", "back", 0, 0, R.drawable.back,"back");
        backRotatedCard = new Card("back", "back", 0, 0, R.drawable.back_rotated, "backrot");

        Collections.shuffle(cardsSpil);
    }

    public int cardSpilSize() {
        return cardsSpil.size();
    }
}

