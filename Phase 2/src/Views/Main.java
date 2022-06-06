package Views;

import Models.Chain;
import Models.Table;
import Models.User;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";
        String[] commandParts;
        while (!command.equals("End")) {
            command = scanner.nextLine();
            commandParts = command.split(" ");
            if (command.startsWith("Create_a_table_for")) {
                createTable(commandParts);
            } else if (command.startsWith("Invitation_request_from")) {
                invitationRequest(commandParts);
            } else if (command.startsWith("Join_request_for")) {
                joinRequest(commandParts);
            } else if (command.startsWith("Number_of_levels")) {
                //numberOfLevels
                System.out.println(Chain.getTables().size());
            } else if (command.startsWith("Number_of_users")) {
                //numberOfUsers
                System.out.println(Chain.getUsers().size());
            } else if (command.startsWith("Number_of_users_in_level")) {
                usersInLevel(commandParts);
            } else if (command.startsWith("Introducer_of")) {
                introducerOf(commandParts);
            } else if (command.startsWith("Friends_of")) {
                friendsOf(commandParts);
            } else if (command.startsWith("Credit_of")) {
                creditOf(commandParts);
            } else if (command.startsWith("Users_on_the_same_level_with")) {
                usersOnSameLevelWith(commandParts);
            } else if (command.startsWith("How_much_have_we_made_yet")) {
                System.out.println(Chain.getMahdizMoney());
            }
        }
    }

    private static void usersOnSameLevelWith(String[] commandParts) {
        for (User user : Chain.getUsers()) {
            if (user.getUsername().equals(commandParts[0])) {
                ArrayList<User> list = user.getTable().getUsers();
                if (list.size() == 1)
                    System.out.println("He_is_all_by_himself");
                else
                    for (User member : list)
                        if (!member.equals(user))
                            System.out.print(member.getUsername() + " ");
                System.out.println();
                return;
            }
        }
        System.out.println("No_such_user_found");
    }

    private static void usersInLevel(String[] commandParts) {
        int level = Integer.parseInt(commandParts[0]);
        try {
            System.out.println(Chain.getTables().get(level).getUsers().size());
        } catch (Exception e) {
            System.out.println("No_such_level_found");
        }
    }

    private static void friendsOf(String[] commandParts) {
        for (User user : Chain.getUsers()) {
            if (user.getUsername().equals(commandParts[0])) {
                ArrayList<User> users = user.getTable().getUsers();
                if (users.size() == 1)
                    System.out.println("No_friend");
                if (users.size() == 2) {
                    if (users.get(0).equals(user))
                        System.out.println(users.get(1).getUsername());
                    else
                        System.out.println(users.get(0).getUsername());
                } else {
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).equals(user)) {
                            try {
                                System.out.print(users.get(i - 1) + " ");
                            } catch (Exception ignored) {
                            }
                            try {
                                System.out.print(users.get(i + 1));
                            } catch (Exception ignored) {
                            }
                            System.out.println();
                        }
                    }
                }
                return;
            }
        }
        System.out.println("No_such_user_found");
    }

    private static void introducerOf(String[] commandParts) {
        String username = commandParts[1];
        if (Chain.isUsernameFree(username)) {
            System.out.println("No_such_user_found");
            return;
        }
        User user = Chain.getUserByUsername(username);

        if (user.getInviter() == null) {
            System.out.println("No_introducer");
            return;
        }
        System.out.println(user.getInviter().getUsername());

    }

    private static void creditOf(String[] commandParts) {
        String username = commandParts[1];
        if (Chain.isUsernameFree(username)) {
            System.out.println("No_such_user_found");
            return;
        }
        System.out.println(Chain.getUserByUsername(username).getMoney());
    }


    private static void numberOfUsers() {
        System.out.println(Chain.getUsers().size());
    }

    private static void numberOfLevels() {
        System.out.println(Chain.getTables().size());
    }

    private static void invitationRequest(String[] commandParts) {
        String username = commandParts[3];
        if (!Chain.isUsernameFree(username)) {
            System.out.println("Username already taken");
            return;
        }
        User inviter = Chain.getUserByUsername(commandParts[1]);
        double deposit = Integer.parseInt(commandParts[5]);
        User user = new User(username, deposit * 0.20, inviter);
        inviter.addMoney(deposit * 0.05);
        Chain.addMoneyToMahdiz(deposit * 0.15);
        Chain.getChainFounder().addMoney(deposit * 0.10);
        Table table = Chain.getFirstNotFullTableAfterUser(inviter);
        table.addUser(user);
        ArrayList<Table> tables = Chain.getTables();
        double eachTableMoneyShare = (deposit / 2) / tables.indexOf(table);
        for (int i = 0; i < tables.indexOf(table); i++) {
            Table thisTable = tables.get(i);
            for (User user1 : thisTable.getUsers()) {
                user1.addMoney(eachTableMoneyShare / thisTable.getUsers().size());
            }
        }
        inviter.setInvitationCount(inviter.getInvitationCount() + 1);
        if (inviter.getInvitationCount() >= 5) {
            Chain.levelUpUser(inviter);
        }
        // TODO: 6/5/2022 NOT FINISHED
    }

    private static void createTable(String[] commandParts) {
        if (Chain.getChainFounder() != null) {
            System.out.println("We already have a founder");
            return;
        }
        int money = Integer.parseInt(commandParts[3]);
        if (money < 5000) {
            System.out.println("Money is not enough");
            return;
        }
        Table table = new Table(0);
        Chain.getTables().add(table);
        User founder = new User(commandParts[1], money - 5000, null);
        founder.setTable(table);
        Chain.setChainFounder(founder);
        table.getUsers().add(founder);
        System.out.println("You now own a table");
    }

    private static void joinRequest(String[] commandParts) {
        String username = commandParts[1];
        if (Chain.isUsernameFree(username)) {
            double deposit = Double.parseDouble(commandParts[3]);
            User user = new User(username, deposit * 0.15, null);
            Chain.addMoneyToMahdiz(deposit * 0.25);
            Chain.getChainFounder().addMoney(deposit * 0.1);
            Table table = new Table(Chain.getTables().size());
            table.addUser(user);
            Chain.getTables().add(table);
            ArrayList<Table> tables = Chain.getTables();
            deposit *= 0.5;
            addMoneyToUsers(deposit, table, tables);
            System.out.println("User added successfully in level " + tables.size());
        }
        else
            System.out.println("Username already taken");
    }

    private static void addMoneyToUsers(double deposit, Table table, ArrayList<Table> tables) {
        double eachTableMoneyShare = deposit / tables.indexOf(table);
        for (int i = 0; i < tables.indexOf(table); i++) {
            Table thisTable = tables.get(i);
            for (User thisTableUser : thisTable.getUsers()) {
                thisTableUser.addMoney(eachTableMoneyShare / thisTable.getUsers().size());
            }
        }
    }
}
