package sample;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class CounterImpl extends UnicastRemoteObject implements Counter{
    public int[][] gok;
    public  DBServerInterface stub;

    public CounterImpl (DBServerInterface stub) throws RemoteException{
        this.stub = stub;
    }


    HashMap hashMap=new HashMap();
    ArrayList<Integer> sessiontoken=new ArrayList<>();


    ArrayList<String> waitingPlayers2=new ArrayList<>();
    ArrayList<String> waitingPlayers3=new ArrayList<>();
    ArrayList<String> waitingPlayers4=new ArrayList<>();
    ArrayList<String> occupiedPlayers=new ArrayList<>();
    ArrayList<Game> watchGames=new ArrayList<>();

    HashMap busyGame=new HashMap();

    @Override
    public boolean SignIn(String a, String b) throws RemoteException{
        System.out.println("username: "+a+ "ww: "+b);
        return stub.signIn(a,b);
    }

    @Override
    public String LogIn(String a, String b) throws RemoteException {
        System.out.println("username: "+a+ "ww: "+b);
        return stub.logIn(a,b);
    }

    @Override
    public void logOut(String sessionToken) throws RemoteException {
        stub.removeFromOnlinePlayers(sessionToken);
    }

    @Override
    public String geefWW(String key){
        return hashMap.get(key).toString();
    }
    @Override
    public void changeBeurt(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        game.changeBeurt();

    }
    public boolean checkBeurt(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        System.out.println("het is nu aan"+game.checkBeurt());
        System.out.println("checkbeurt: "+sessionToken);
        return (sessionToken.equals(game.checkBeurt()));
    }


    @Override
    public void testConnectie(){
        System.out.println("connectie is er");
    }
    @Override
    public boolean setGame(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        game.generateMatrix();
        if(game.checkBeurt().equals(sessionToken)){
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public synchronized void addToGame(String sessionToken, int aantalspelers){
        System.out.println("1token: "+sessionToken);
        if(aantalspelers==2) {

            if (!waitingPlayers2.contains(sessionToken)) {
                waitingPlayers2.add(sessionToken);
            }
            if (waitingPlayers2.size() == 2&&waitingPlayers2.get(1).equals(sessionToken)) {
                notifyAll();
                System.out.println("3token: "+sessionToken);
                String speler1 = waitingPlayers2.get(0);
                String speler2 = waitingPlayers2.get(1);
                waitingPlayers2.clear();
                Game game = new Game(speler1, speler2);
                busyGame.put(speler1, game);
                busyGame.put(speler2, game);
                watchGames.add(game);
                occupiedPlayers.add(speler1);
                occupiedPlayers.add(speler2);
            }
            else{
                System.out.println("vindt");
                vindtTegenspeler(sessionToken);
            }

        }
        else if(aantalspelers==3) {
            if (!waitingPlayers3.contains(sessionToken)) {
                waitingPlayers3.add(sessionToken);
            }
            if (waitingPlayers3.size() == 3) {
                notifyAll();
                String  speler1 = waitingPlayers3.get(0);
                String speler2 = waitingPlayers3.get(1);
                String speler3 = waitingPlayers3.get(2);
                waitingPlayers3.clear();
                System.out.println(speler1+" "+speler2+" "+ speler3+" ");
                Game game = new Game(speler1, speler2 ,speler3);

                busyGame.put(speler1, game);
                busyGame.put(speler2, game);
                busyGame.put(speler3, game);
                watchGames.add(game);
                occupiedPlayers.add(speler1);
                occupiedPlayers.add(speler2);
                occupiedPlayers.add(speler3);

            }
            else{
                vindtTegenspeler(sessionToken);
            }

        }
        else{
            if (!waitingPlayers4.contains(sessionToken)) {
                waitingPlayers4.add(sessionToken);
            }
            if (waitingPlayers4.size() == 4) {
                notifyAll();
                String speler1 = waitingPlayers4.get(0);
                String speler2 = waitingPlayers4.get(1);
                String speler3 = waitingPlayers4.get(2);
                String speler4 = waitingPlayers4.get(3);
                waitingPlayers4.clear();
                Game game = new Game(speler1, speler2 ,speler3, speler4);

                busyGame.put(speler1, game);
                busyGame.put(speler2, game);
                busyGame.put(speler3, game);
                busyGame.put(speler4, game);
                watchGames.add(game);
                occupiedPlayers.add(speler1);
                occupiedPlayers.add(speler2);

                occupiedPlayers.add(speler3);

            }
            else{
                vindtTegenspeler(sessionToken);
            }

        }
    }
    @Override
    public int getGame(int i){
        if(watchGames.isEmpty()){
            return -1;
        }
        else if(i>=watchGames.size()){
            return 0;
        }
        else {
            return i;
        }

    }

    @Override
    public synchronized boolean vindtTegenspeler(String sessionToken){
        try {
            while (!occupiedPlayers.contains(sessionToken)) {
                System.out.println("waitings");
                wait();
            }
            System.out.println("dobby is free");
            return true;
        }
        catch (Exception e){

        }
        return false;
    }

    @Override
    public int[]getAndereGok(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        int[]gok=game.getTegenspelerGok(sessionToken);
        return gok;
    }
    public int getZet(int i, String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        int value=game.getFromMatrix(i);
        return value;
    }
    @Override
    public boolean getResult(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        return game.getResult(sessionToken);
    }
    @Override
    public int getScore(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        return game.getScore(sessionToken);
    }
    @Override
    public ArrayList<ArrayList<Integer>> getReedsGezet(int i){
        Game game=watchGames.get(i);
        return game.getReedsGezet();
    }
    @Override
    public boolean getEnd(int game){
        Game tempgame=watchGames.get(game);
        return tempgame.getEnd();
    }
    @Override
    public int[] getGameGok(int game, int viewerId){
        Game tempgame=watchGames.get(game);
        return tempgame.getGok(viewerId);
    }
    @Override
    public int getViewerId(int game){
        Game tempgame=watchGames.get(game);
        return tempgame.getViewerId();
    }
    @Override
    public void geefNotify(String sessionToken){
        Game game=(Game)busyGame.get(sessionToken);
        game.geefNotify();
    }
}