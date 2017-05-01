package com.example.demento.briskula;

import java.util.LinkedList;

public class SinglePlayer extends Initialization {
    private int playerScore;
    private int compScore;
    private final LinkedList<Card> player = new LinkedList<>();
    private final LinkedList<Card> comp = new LinkedList<>();
    private final Card briskulaCard; //tip u koji je igra
    private final Card playerCard;
    private final Card compCard;
    private boolean flag;           //odredjuje ciji je red na igru
    private boolean gameCardCheck; //je li podignuta ultima

    public SinglePlayer() {

        super();
        playerScore = 0;
        compScore = 0;
        playerCard = null;
        compCard = null;
        // briskulaCard = null;
        flag = false;

        //####################################### INICIJALIZACIJA DVIJU LISTA S KOJIMA VODIMU IGRU ###############
        // u listu player dodaj 3 karte i izbrisi ih iz spila
        player.add(cardsSpil.remove(0));
        player.add(cardsSpil.remove(1));
        player.add(cardsSpil.remove(2));

        //u listu comp dodaj tri karte i izbrisi ih iz spila
        comp.add(cardsSpil.remove(3));
        comp.add(cardsSpil.remove(4));
        comp.add(cardsSpil.remove(5));

        //gameCard je karta u koju je igra(briskula), dodaj je i izbrisi iz spila
        briskulaCard = cardsSpil.remove(6);
        gameCardCheck = false;
    }

    //vraca bodove od playera
    public int playerScore() {
        return playerScore;
    }

    //vraca bodove od comp-a
    public int compScore() {
        return compScore;
    }

    //vraca flag ovisno ciji je red za igru. true je player, a false je comp
    public boolean checkFlag() {
        return flag;
    }

    //vrati cijelu listu karata od playera
    public LinkedList<Card> getPlayerCards() {
        return player;
    }

    //vrati cijelu listu karata od compa
    public LinkedList<Card> getCompCards() {
        return comp;
    }

    //vrati pojedinacnu kartu iz liste playera (sluzi za prvu vizualnu inicijalizaciju karte)
    public Card getMe(int num) {

        return player.get(num);
    }

    //vrati pojedinacnu kartu iz liste compa (sluzi za prvu vizualnu inicijalizaciju karte)
    public Card getComp(int num) {

        return comp.get(num);
    }

    //vrati kartu u koju je igra (briskula) sluzi za vizualnu inicijalizaciju karte)
    public Card getGameCard() {

        return briskulaCard;
    }

    //vrati kartu sa poledjinom (sluzi za vizualnu inicijalizaciju karte
    public Card getBackCard() {
        return backCard;
    }

    //vrati kartu sa rotiranom poledjinom (sluzi za vizualnu inicijalizaciju karte
    public Card getBackRotatedCard() {
        return backRotatedCard;
    }

    //dodaj kartu u listu playera i izbrisi je iz spila
    public void setMe() {
        //int r = ran.nextInt(cardsSpil.size() - 0) + 0;
        player.add(cardsSpil.remove(0));
    }

    //dodaj kartu u listu comp i izbrisi je iz spila
    public void setComp() {
        //int r = ran.nextInt(cardsSpil.size() - 0) + 0;
        comp.add(cardsSpil.remove(0));
    }

    //######################################################################## IGRA #######################################
    //vrati kartu sa odredjenog indexa kada player klikne labelu i odmah taj index popuni sa novom kartom iz spila
    public Card getMeCard(int num) {
        Card temp;
        if (!gameCardCheck) { //ako nije podignuta ultima peskaj
            temp = player.get(num);
            player.set(num, newHand());
        } else {
            temp = player.get(num);
            player.set(num, null); //ne smijemo izbrisati mjesto radi zadnjeg poteza kada se podigne ultima
        }
        return temp;
    }

    //metoda koja sluzi za bacanje karte kada comp igra prvi
    public Card bestCompCard() {

        Card tempCard = comp.get(0);
        int i = 0;

        for (Card c : comp) {

            if (c.value < tempCard.value && !c.type.equals(briskulaCard.type)) {
                tempCard = c;
                i = comp.indexOf(c);
            } else if (c.value < tempCard.value) {
                tempCard = c;
                i = comp.indexOf(c);
            }
        }
        if (gameCardCheck == false) { //ako nije podignuta ultima peskaj

            comp.set(i, newHand());
            return tempCard;
        } else { //ako je podignuta ultima nemoj peskat, samo izbrisi kartu i

            comp.remove(i);
            System.out.println(i);
            return tempCard;
        }
    }

    //metoda koja vraca kartu kada comp igra nakon playera, odgovor
    public Card getCompCard(Card currentPlayerCard) {

        Card tempComp = comp.get(0);
        int i = 0;
        boolean checked = false; //ako pronadje bilo koju kartu ne provjeravaj druge uvjete

        if (currentPlayerCard.type.equals(briskulaCard.type)) { //ako JE player bacio briskulu

            if (currentPlayerCard.value == 9 || currentPlayerCard.value == 8 || currentPlayerCard.value == 7 || currentPlayerCard.value == 6) {//trica,kralj,konj,fanat

                for (Card c : comp) {
                    //veca od igraceve bacene                    //od igre
                    if (c.value > currentPlayerCard.value && c.type.equals(briskulaCard.type)) {//
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                    } else if (checked == false && !c.type.equals(briskulaCard.type)) { //baci bilo koju drugu kartu
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                    }
                    //ubi najmanjom vecom briskulom
                    if (checked == true && c.value > currentPlayerCard.value && c.value < tempComp.value && c.type.equals(tempComp.type)) {
                        tempComp = c;
                        i = comp.indexOf(c);
                    } else if (checked == true && c.value < tempComp.value && !c.type.equals(briskulaCard.type)) { //ta druga karta neka bude najmanja iz liste
                        tempComp = c;
                        i = comp.indexOf(c);
                    }

                }

            } else { //ako player baci asa ili liso briskule
                for (Card c : comp) {
                    if (!c.type.equals(briskulaCard.type) && c.value < tempComp.value) { //baci ne briskulu, najmanju iz liste
                        tempComp = c;
                        i = comp.indexOf(c);
                    }
                }
            }

        } else //ako player NIJE bacio briskulu
            if (currentPlayerCard.value == 10 || currentPlayerCard.value == 9 || currentPlayerCard.value == 8 || currentPlayerCard.value == 7 || currentPlayerCard.value == 6) { //ako igrac baci punte

                boolean throwAnySmall = false; //- za bacanje najmanje karte ako nema boljeg odgovora
                for (Card c : comp) {
                    //veca od igraceve                   //istog tipa kojeg je bacio igrac
                    if (c.value > currentPlayerCard.value && c.type.equals(currentPlayerCard.type)) { //baci vecu od igracevog tipa
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                    } else if (checked == false && c.type.equals(briskulaCard.type)) { // briskulu
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                    } else if (checked == false && c.value <= tempComp.value) { //bilo koju najmanju kartu
                        tempComp = c;
                        i = comp.indexOf(c);
                        throwAnySmall = true;
                    }
                    //baci najvecu od igre
                    if (checked == true && c.value > tempComp.value && c.type.equals(tempComp.type) && c.type.equals(currentPlayerCard.type)) {//najveci stroc
                        tempComp = c;
                        i = comp.indexOf(c);
                    } else if (checked == true && c.value < tempComp.value && c.type.equals(tempComp.type)) {//najmanju briskulu
                        tempComp = c;
                        i = comp.indexOf(c);
                    } else if (throwAnySmall == true && c.value < tempComp.value) {//najmanju kartu
                        tempComp = c;
                        i = comp.indexOf(c);
                    }
                }

            } else { //ako igrac baci bilo koju liso kartu

                for (Card c : comp) {
                    //strocaj
                    if (c.type.equals(currentPlayerCard.type) && c.value > currentPlayerCard.value) { //najveca od igre
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                        //bilo koja najmanja vrijednost
                    } else if (checked == false && c.value < tempComp.value) {
                        tempComp = c;
                        i = comp.indexOf(c);
                        checked = true;
                    }

                    if (checked == true && c.value > tempComp.value && c.type.equals(tempComp.type) && c.type.equals(currentPlayerCard.type)) {//najveci stroc
                        tempComp = c;
                        i = comp.indexOf(c);
                    } else if (checked == true && c.value < tempComp.value && c.type.equals(tempComp.type)) {//najmanja kartu
                        tempComp = c;
                        i = comp.indexOf(c);
                    }
                }
            }

        if (gameCardCheck == false) { //ako nije podignuta ultima peskaj
            comp.set(i, newHand()); //kartu koju peskamo postavljamo na mjesto na kojem je bila karta koju smo vratili
            return tempComp;
        } else { //ako je podignuta ultima nemoj peskat, samo izbrisi kartu iz liste
            comp.remove(i);
            System.out.println(i);
            return tempComp;
        }
    }

    //metoda za peskanje nove karte
    public Card newHand() {

        if (cardsSpil.isEmpty()) { //ako je spil sa kartama prazan
            if (gameCardCheck == false) { //ultima
                gameCardCheck = true;
                return briskulaCard; //vrati ultimu
            }
        } else {
            return cardsSpil.remove(0); //vrati prvu kartu iz spila
        }
        return null;
    }

    public void hand(Card player, Card comp) { //metoda koja odredjuje tko nosi ruku, zbraja punte i dodjeljuje flag iducem igracu

        if (player.type.equals(comp.type) && player.value > comp.value) { //ako je isti tip i ako je player vrijednost veca
            playerScore += player.score + comp.score;
            flag = true;
        } else if (player.type.equals(comp.type) && player.value < comp.value) { //ako je isti tip i ako je comp vrijednost veca
            compScore += player.score + comp.score;
            flag = false;
        } else if (player.type.equals(briskulaCard.type) && !comp.type.equals(briskulaCard.type)) { //ako je player briskula, a comp nije
            playerScore += player.score + comp.score;
            flag = true;
        } else if (!player.type.equals(briskulaCard.type) && comp.type.equals(briskulaCard.type)) { //ako je comp briskula, a player nije
            compScore += player.score + comp.score;
            flag = false;
        } else if (!player.type.equals(comp.type) && flag == true) { //ako player prvi baca, a comp baci razlicit tip
            playerScore += player.score + comp.score;
            flag = true;
        } else if (!player.type.equals(comp.type) && flag == false) { //ako comp prvi baca, a player baci razlicit tip
            compScore += player.score + comp.score;
            flag = false;
        }
    }

    //vrati je li ultima podignuta
    public boolean checkUltima() {
        return gameCardCheck;
    }

    public void reset() {

    }
}
