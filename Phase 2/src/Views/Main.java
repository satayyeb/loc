package Views;

import Models.Chain;
import Models.Table;
import Models.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";
        String[] commandParts;
        while (!command.equals("End")){
            command = scanner.nextLine();
            commandParts = command.split(" ");
            if (command.startsWith("Create_a_table_for")){
                createTable(commandParts);
            } else if(command.startsWith("Invitation_request_from")){
                invitationRequest(commandParts);
            } else if(command.startsWith("Join_request_for")){
//                joinRequest(commandParts);
            } else if (command.startsWith("Number_of_levels")){
//                numberOfLevels();
            } else if(command.startsWith("Number_of_users")){
//                numberOfUsers();
            } else if(command.startsWith("Number_of_users_in_level")){
//                usersInLevel(commandParts);
            } else if(command.startsWith("Introducer_of")){
//                introducerOf(commandParts);
            } else if(command.startsWith("Friends_of")){
//                friendsOf(commandParts);
            } else if (command.startsWith("Credit_of")){
//                creditOf(commandParts);
            } else if (command.startsWith("Users_on_the_same_level_with")){
//                usersOnSameLevelWith(commandParts);
            } else if (command.startsWith("How_much_have_we_made_yet")){
//                MahdizMoneyMade();
            }
        }
    }

    private static void invitationRequest(String[] commandParts) {
        String username = commandParts[3];
        if (!Chain.isUsernameFree(username)){
            System.out.println("Username already taken");
            return;
        }
        User inviter = Chain.getUserByUsername(commandParts[1]);
        int deposit = Integer.parseInt(commandParts[5]);
        User user = new User(username, deposit * 0.2, inviter);
        inviter.addMoney(deposit * 0.05);
        Chain.addMoneyToMahdiz(deposit * 0.15);
        Table table = Chain.getFirstNotFullTableAfterUser(inviter);
        // TODO: 6/5/2022 NOT FINISHED
    }

    private static void createTable(String[] commandParts) {
        if (Chain.getChainFounder() != null){
            System.out.println("We already have a founder");
            return;
        }
        int money = Integer.parseInt(commandParts[3]);
        if (money < 5000){
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
}
