package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private int gameId;
    private boolean end;
    private ArrayList<String>players;
    private ArrayList<Integer>scores;
    private ArrayList<Integer>keuzes;
    private ArrayList<Integer>plaatsen;
   // private int plaats1;
    //private int plaats2;
    private String beurt;
    private int[][] matrix;
    private ArrayList<ArrayList<Integer>> reedsGezet;
    private int[] gegokt;
    private boolean initialized;
    private ArrayList<Boolean>update;
    private ArrayList<Boolean> viewerupdate;

    private int aantalSpeler;

    public Game(){

    }
    public Game(int aantalSpelers, String host){
        this.aantalSpeler=aantalSpelers;
        players=new ArrayList<>();
        players.add(host);
    }

    public  Game(String player1, String player2){
        players=new ArrayList<>();
        scores=new ArrayList<>();
        keuzes=new ArrayList<>();
        plaatsen=new ArrayList<>();
        update=new ArrayList<>();
        viewerupdate=new ArrayList<>();
        end=false;
        reedsGezet=new ArrayList<>();
        for(int i=0;i<2;i++) {
            update.add(false);
        }
        players.add(player1);
        players.add(player2);

        initialized=false;
    }
    public Game(String player1, String player2, String player3){
        players=new ArrayList<>();
        scores=new ArrayList<>();
        update=new ArrayList<>();
        reedsGezet=new ArrayList<>();
        viewerupdate=new ArrayList<>();
        end=false;

        for(int i=0;i<3;i++) {
            update.add(false);
        }

        players.add(player1);
        players.add(player2);
        players.add(player3);

        initialized=false;
    }
    public Game(String player1, String player2, String player3, String player4){
        players=new ArrayList<>();
        scores=new ArrayList<>();
        update=new ArrayList<>();
        reedsGezet=new ArrayList<>();
        viewerupdate=new ArrayList<>();
        end=false;

        for(int i=0;i<4;i++) {
            update.add(false);
        }

        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        initialized=false;
    }
    public void setGameId(int id){
        gameId=id;
    }
    public void addPlayer(String sessionToken){
        players.add(sessionToken);
    }
    public boolean isFull(){
        if(players.size()==aantalSpeler)return true;
        return false;
    }
    public ArrayList<String> getPlayers(){
        return players;
    }
    public void startGame(ArrayList<String> gamePlayers){
        players=new ArrayList<>();
        for (String player: gamePlayers) {
            players.add(player);
        }
        scores=new ArrayList<>();
        keuzes=new ArrayList<>();
        plaatsen=new ArrayList<>();
        update=new ArrayList<>();
        viewerupdate=new ArrayList<>();
        end=false;
        reedsGezet=new ArrayList<>();
        for(int i=0;i<gamePlayers.size();i++) {
            update.add(false);
        }
        initialized=false;
    }
    public void generateMatrix(){

        System.out.println("generating"+ " beurt: "+beurt);
        for (String sessionToken: players) {
            System.out.print(sessionToken+" ");
        }
        System.out.println();
        if(!initialized) {
            System.out.println("for loop");
            for(int i=0;i<players.size();i++){
                System.out.println("i :"+players.get(i));
            }
            System.out.println("foreach");
            for (String sessionToken:players) {
                System.out.println("player: "+sessionToken);
            }

            for(int i=0;i<players.size();i++){
                scores.add(0);
            }


            gegokt=new int[2];

            beurt=players.get(0);
            initialized=true;
            List<Integer> solution = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                solution.add(i);
                solution.add(i);
            }
            Collections.shuffle(solution);
            for (int i : solution) {
                System.out.print(i + " ");
            }
            matrix = new int[4][4];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrix[i][j] = solution.get(i * 4 + j);
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }

    }

    public synchronized int getFromMatrix(int i){
        System.out.println("value= "+i);
        i--;
        int row=i/4;
        int column=i%4;
        System.out.println("rij "+row+ "column: "+column);
        gegokt[0]=i;
        gegokt[1]=matrix[row][column];
        for(int j=0;j<viewerupdate.size();j++){
            viewerupdate.set(j,true);
        }
        //notifyAll();
        /*for(int j=0;j<update.size();j++){
            update.set(j,true);
        }*/
        keuzes.add(matrix[row][column]);
        plaatsen.add(i);
        /*if(keuze1==-1){
            keuze1=matrix[row][column];
            plaats1=i;
        }
        else{
            keuze2=matrix[row][column];
            plaats2=i;
        }*/
        if(keuzes.size()==2){
            if(keuzes.get(0)==keuzes.get(1)){
                scores.set(players.indexOf(beurt),scores.get(players.indexOf(beurt))+1);

                ArrayList<Integer>temp=new ArrayList<>();
                temp.add(keuzes.get(0));
                temp.add(plaatsen.get(0));
                temp.add(plaatsen.get(1));
                reedsGezet.add(temp);

                if(reedsGezet.size()==8)end=true;

                for (Integer k:scores) {
                    System.out.println("score: "+k);
                }
            }
            keuzes.clear();
            plaatsen.clear();

        }
        return matrix[row][column];
    }
    public void changeBeurt(){
        //beveiliging
        int index=players.indexOf(beurt);
        /*if(keuze1==keuze2){
            scores.set(index,scores.get(index)+1);
            System.out.println("#############");
                for (Integer i:scores) {
                    System.out.println("score: "+i);
                }
            System.out.println("##############");
            ArrayList<Integer>temp=new ArrayList<>();
            temp.add(keuze1);
            temp.add(plaats1);
            temp.add(plaats2);
            reedsGezet.add(temp);
            if(reedsGezet.size()==8)end=true;
        }*/

        if(index==(players.size()-1)){
            beurt=players.get(0);
        }
        else{
            beurt=players.get(index+1);
        }

    }
    public String checkBeurt(){
       // System.out.println("checkbeurt "+beurt);
        return beurt;

    }
    public boolean getEnd(){
        return end;
    }
    public int getIndex(String sessionToken){
        return players.indexOf(sessionToken);
    }
    public synchronized int[] getTegenspelerGok(String sessionToken){
        try{
            wait();
            return gegokt;
        }
        catch (Exception e){
            System.out.println("waiting failed");
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
         /*int index=players.indexOf(sessionToken);
       if(update.get(index)) {
            update.set(index,false);
            return gegokt;
        }
        else{
            return null;
        }*/
    }
    public int[] getGok(int viewerid){
        if(viewerupdate.get(viewerid)) {
            System.out.println("binnenaaaaaaaaaaaa");
            viewerupdate.set(viewerid,false);
            return gegokt;
        }
        else{
            return null;
        }

    }
    public int getViewerId(){
        int viewerId=viewerupdate.size();
        viewerupdate.add(false);
        return viewerId;
    }
    public boolean getResult(String sessionToken){
        int index=players.indexOf(sessionToken);
        boolean gewonnen=true;
        int hoogste=scores.get(index);
        for(int i=0;i<scores.size();i++){
            if(index!=i){
                if(scores.get(i)>hoogste)gewonnen=false;
            }
        }
        return gewonnen;
    }
    public int getScore(String sessionToken){
       return scores.get(players.indexOf(sessionToken));
    }
    public ArrayList<ArrayList<Integer>> getReedsGezet(){
        return reedsGezet;
    }
    public synchronized void geefNotify(){
        notifyAll();
    }
}
