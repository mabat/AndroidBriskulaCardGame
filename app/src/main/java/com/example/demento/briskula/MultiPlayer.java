package com.example.demento.briskula;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class MultiPlayer extends Initialization {

    private int playerScore;
    private int oponentScore;
    private final List<Object> player = new LinkedList<>(); //niz sa igracevim kartama
    private final List<Object> oponent = new LinkedList<>(); //niz sa protivnikovim kartama
    private final Object briskulaCard; //tip u koji je igra
    private Card oponentCurrentCard; //trenutna karta koju je odigrao protivnik
    private HashMap<String, Object> gameCard2; //trenutna karta koju primamo
    private Card playerCurrentCard; //trenutna igraceva karta
    private boolean flag;           //odredjuje ciji je red na igru, lokalno
    private DatabaseReference ref;
    private String gameID; //string igre na firebasu, jedinstven je za partiju
    private int indexOfOponent; //pamti index bacene karte il liste karata za kasnije vracanje na isto mjesto nove karte
    private int indexOfPlayer;
    private int c; //ako je c = 3 znaci da je kraj igre.
    private boolean ultimaFlag; //gleda je li podignuta ultima
    public boolean test = true; //locker da eventlistener ne pristupa metodi nakon prvog pristupa


    public MultiPlayer() {
        super();
        playerScore = 0;
        oponentScore = 0;
        // briskulaCard = null;
        flag = false;
        c = 0;
        test = true;


        // u listu player dodaj 3 karte i izbrisi ih iz spila
        this.player.add(cardsSpil.remove(0));
        this.player.add(cardsSpil.remove(1));
        this.player.add(cardsSpil.remove(2));

        //u listu comp dodaj tri karte i izbrisi ih iz spila
        this.oponent.add(cardsSpil.remove(3));
        this.oponent.add(cardsSpil.remove(4));
        this.oponent.add(cardsSpil.remove(5));

        //gameCard je karta u koju je igra(briskula), dodaj je i izbrisi iz spila
        briskulaCard = cardsSpil.remove(6);

        //ovo ide na firebase, pocetna inicijalizacija
        FirebaseBriskula fb = new FirebaseBriskula(briskulaCard, Long.valueOf(0), Long.valueOf(playerScore),
                Long.valueOf(oponentScore), null, null, this.player, this.oponent);
        ref = FirebaseDatabase.getInstance().getReference();
        gameID = ref.push().getKey();
        ref.child(gameID).setValue(fb);

    }

    //gameID, string iz firebasea koji je jedinstven za pojedinu partiju, saljemo ga prijatelju preko deeplinka
    public String getGameID() {
        return this.gameID;
    }

    public void oponentPlaySecond() {
        ref.child(gameID).child("flagGameTurn").setValue(1); //red je na protivnika za bacanje karte
    }

    /*upravlja nizom oponent koji sadrzi karte od protivnika,
    nakon sto je protivnik poslao kartu brisemo je lokalno iz niza te na to mjesto nadopisujemo string "null"
    dok se ponovno ne podijele karte, nakon toga na to mjesto ide nova karta. Ako je na firebase-u upisa string "null"
    tada eventlistener ne prima nikakvu kartu za tu poziciju i karta ce biti prazna na activityu*/
    public void setOponentCurrentCard(HashMap<String, Object> gameCard) {

        if (test) {

            test = false;
            gameCard2 = gameCard;
            indexOfOponent = (((Long) gameCard2.get("indexInSpil")).intValue());

            oponent.set(indexOfOponent, "null");
            ref.child(gameID).child("oponent").setValue(oponent);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {
                    if (ds.child(gameID).child("tableCard1").exists() && ds.child(gameID).child("tableCard2").exists()) {
                        hand();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /*upravlja igracevim nizom karata "player" na slican nacin kao i gore, samo je ovdje lokalno primanje karte*/
    public void playerThrowedCard(int n) {

        indexOfPlayer = n;
        this.playerCurrentCard = (Card) player.get(n);

        this.player.set(n, "null");

        ref.child(gameID).child("player").setValue(this.player);
        ref.child(gameID).child("tableCard1").setValue(playerCurrentCard);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                if (ds.child(gameID).child("tableCard1").exists() && ds.child(gameID).child("tableCard2").exists()) {
                    hand();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*racunanje punata, cije su karte, prazni karte sa stola, dijeli nove*/
    public void hand() { //metoda koja odredjuje tko nosi ruku, zbraja punte, dodjeljuje flag iducem igracu i prazni karte sa stola

        oponentCurrentCard = new Card((String) gameCard2.get("type"), (String) gameCard2.get("name"), ((Long) gameCard2.get("value")).intValue(),
                ((Long) gameCard2.get("score")).intValue(), ((Long) gameCard2.get("cardImage")).intValue(), (String) gameCard2.get("cardID"));

        try {

            Thread.sleep(1500);

            if (playerCurrentCard.type.equals(oponentCurrentCard.type) && playerCurrentCard.value > oponentCurrentCard.value) { //ako je isti tip i ako je player vrijednost veca
                playerScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("playerScore").setValue(playerScore); //zapisivanje u firebase
                flag = false;
                ref.child(gameID).child("flagGameTurn").setValue(0); //red je na igraca za bacanje karte
            } else if (playerCurrentCard.type.equals(oponentCurrentCard.type) && playerCurrentCard.value < oponentCurrentCard.value) { //ako je isti tip i ako je comp vrijednost veca
                oponentScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("oponentScore").setValue(oponentScore);
                flag = true;
                ref.child(gameID).child("flagGameTurn").setValue(1); //red je na protivnika za bacanje karte
            } else if (playerCurrentCard.type.equals(((Card) briskulaCard).type) && !(oponentCurrentCard.type.equals(((Card) briskulaCard).type))) { //ako je player briskula, a protivnik nije
                playerScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("playerScore").setValue(playerScore);
                flag = false;
                ref.child(gameID).child("flagGameTurn").setValue(0); //red je na igraca za bacanje karte
            } else if (!playerCurrentCard.type.equals(((Card) briskulaCard).type) && (oponentCurrentCard.type.equals(((Card) briskulaCard).type))) { //ako je protivnik briskula, a player nije
                oponentScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("oponentScore").setValue(oponentScore);
                flag = true;
                ref.child(gameID).child("flagGameTurn").setValue(1); //red je na protivnika za bacanje karte
            } else if (!playerCurrentCard.type.equals(oponentCurrentCard.type) && !flag) { //ako player prvi baca, a comp baci razlicit tip
                playerScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("playerScore").setValue(playerScore);
                flag = false;
                ref.child(gameID).child("flagGameTurn").setValue(0); //red je na igraca za bacanje karte
            } else if (!playerCurrentCard.type.equals(oponentCurrentCard.type) && flag) { //ako comp prvi baca, a player baci razlicit tip
                oponentScore += playerCurrentCard.score + oponentCurrentCard.score;
                ref.child(gameID).child("oponentScore").setValue(oponentScore);
                flag = true;
                ref.child(gameID).child("flagGameTurn").setValue(1); //red je na protivnika za bacanje karte
            }
            playerCurrentCard = null;
            oponentCurrentCard = null;
            setTableCardsToNull();
            dealCards();
            checkWin();
        } catch (Exception e) {

        }

    }

    /*dijeli karte igracima*/
    public void dealCards() {

        /*ako je podignuta ultima, tada se ne dijele karte nego
            se "c" uvecava tri puta (toliko ima poteza do kraja)*/
        if (!this.ultimaFlag) {
            /*flag odredjuje ciji je red na igru, pa s obzirom na to pojedini igrac ima prednost, vazno za ultimu*/

            if (!flag) {

                try {
                    this.player.set(indexOfPlayer, cardsSpil.remove(0));
                    this.oponent.set(indexOfOponent, cardsSpil.remove(0));
                } catch (Exception ex) {
                    this.oponent.set(indexOfOponent, briskulaCard);
                    ref.child(gameID).child("briskulaCard").setValue(null);
                    this.ultimaFlag = true;
                }
            } else {
                try {
                    this.oponent.set(indexOfOponent, cardsSpil.remove(0));
                    this.player.set(indexOfPlayer, cardsSpil.remove(0));
                } catch (Exception e) {
                    this.player.set(indexOfPlayer, briskulaCard);
                    ref.child(gameID).child("briskulaCard").setValue(null);
                    this.ultimaFlag = true;
                }
            }

            //update firebase
            ref.child(gameID).child("player").setValue(this.player);
            ref.child(gameID).child("oponent").setValue(this.oponent);
        } else {
            c++; //ako je c = 3 znaci da je gotova igra, tj. skupljena je ultima i tri puta su kupljene karte
        }
    }

    /*karte od igre se postavljaju null*/
    public void setTableCardsToNull() {

        ref.child(gameID).child("tableCard1").setValue(null);
        ref.child(gameID).child("tableCard2").setValue(null);
    }

    /*provjera pobjednika, c je index koji se uvecava kada se podigne ultima.
    ako nema dijeljenja zadnja tri puta c = 3 i partija je gotova*/
    public void checkWin() {

        if (c == 3) {

            //0-pobjeda servera, 1-pobjeda clienta, 2-izjednacenje

            if (this.oponentScore > this.playerScore) {
                ref.child(gameID).child("WIN").setValue(1);
            } else if (this.oponentScore < this.playerScore) {
                ref.child(gameID).child("WIN").setValue(0);
            } else {
                ref.child(gameID).child("WIN").setValue(2);
            }
        }
    }
}

