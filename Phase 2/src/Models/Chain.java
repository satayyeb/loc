package Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Chain {
    private static Set<User> users = new HashSet<>();
    private static ArrayList<Table> tables = new ArrayList<>();
    private static double MahdizMoney = 0;
    private static User chainFounder = null;

    public static Set<User> getUsers() {
        return users;
    }

    public static void setUsers(Set<User> users) {
        Chain.users = users;
    }

    public static ArrayList<Table> getTables() {
        return tables;
    }

    public static void setTables(ArrayList<Table> tables) {
        Chain.tables = tables;
    }

    public static double getMahdizMoney() {
        return MahdizMoney;
    }

    public static void setMahdizMoney(double mahdizMoney) {
        MahdizMoney = mahdizMoney;
    }

    public static User getChainFounder() {
        return chainFounder;
    }

    public static void setChainFounder(User chainFounder) {
        Chain.chainFounder = chainFounder;
    }

    public static boolean isUsernameFree(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)) return false;
        }
        return true;
    }
    public static User getUserByUsername(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        throw new RuntimeException("USERNAME NOT FOUND!");
    }

    public static void addMoneyToMahdiz(double amount){
        setMahdizMoney(getMahdizMoney() + amount);
    }

    public static Table getFirstNotFullTableAfterUser(User inviter) {
        return null;
    }
}
