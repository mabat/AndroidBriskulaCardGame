package com.example.demento.briskula;


import java.util.List;

public class FirebaseBriskula {

    public Object briskulaCard;
    public Long flagGameTurn;
    public Long playerScore;
    public Long oponentScore;
    public Object tableCard1;
    public Object tableCard2;
    public List <Object> player;
    public List <Object> oponent;

    public FirebaseBriskula(){}

    public FirebaseBriskula(Object briskulaCard, Long flagGameTurn, Long playerScore, Long oponentScore,
                            Object tableCard1, Object tableCard2, List <Object> player, List <Object> oponent){

        this.briskulaCard=briskulaCard;
        this.flagGameTurn=flagGameTurn;
        this.playerScore =playerScore;
        this.oponentScore = oponentScore;
        this.tableCard1 = tableCard1;
        this.tableCard2 = tableCard2;
        this.player = player;
        this.oponent = oponent;
    }
}
