package Models;

import java.util.*;

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
        int i = tables.indexOf(inviter.getTable());
        for (int j = i+1; j < tables.size(); j++) {
            Table table = tables.get(j);
            if (!table.isFull()){
                return table;
            }
        }

        Table table = new Table(tables.size());
        tables.add(table);
        return table;
    }

    public static void levelUpUser(User user) {
        // FIXME: 6/6/2022 may cause error
        Table upTable = tables.get(tables.indexOf(user.getTable()) -1);
        if (!upTable.isFull()){
            upTable.addUser(user);
            return;
        }
        List<User> sortedUsers = (upTable.getUsers().stream().sorted(Comparator.comparing(User::getInvitationCount).thenComparing(User::getMoney)).toList());
        User worstUser = sortedUsers.get(sortedUsers.size() -1);
        User tempWorst = new User(worstUser.getUsername(), worstUser.getMoney(), worstUser.getTable(), worstUser.getInviter(), worstUser.getInvitationCount());
        User tempGood = new User(user.getUsername(), user.getMoney(), user.getTable(), user.getInviter(), user.getInvitationCount());
        user.getTable().getUsers().set(user.getTable().getUsers().indexOf(user), tempWorst);
        worstUser.getTable().getUsers().set(worstUser.getTable().getUsers().indexOf(worstUser), tempGood);
    }
}
