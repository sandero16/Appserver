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

    ArrayList<Game> waitingGame2=new ArrayList<>();
    ArrayList<Game> waitingGame3=new ArrayList<>();
    ArrayList<Game> waitingGame4=new ArrayList<>();


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
    @Override
    public void startGame(ArrayList<String>players){
        Game tempGame=new Game();
        System.out.println("Game started");
        tempGame.startGame(players);
        for (String player:players) {
            busyGame.put(player, tempGame);
        }
    }
}