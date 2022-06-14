import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";
        String[] commandParts;
        while (!command.equals("End")) {
            command = scanner.nextLine();
            commandParts = command.split(" ");
            if (command.startsWith("Create_a_table_for ") && commandParts.length ==4) {
                createTable(commandParts);
            } else if (command.startsWith("Invitation_request_from") && commandParts.length==6) {
                invitationRequest(commandParts);

            } else if (command.startsWith("Join_request_for") && commandParts.length==4) {
                joinRequest(commandParts);
            } else if (command.startsWith("Number_of_levels")) {
                System.out.println(Chain.getTables().size());
            } else if (command.startsWith("Number_of_users_in_level") && commandParts.length == 2 ) {
                usersInLevel(commandParts);
            } else if (command.startsWith("Number_of_users")) {
                System.out.println(Chain.getUsers().size());
            } else if (command.startsWith("Introducer_of ") && commandParts.length == 2) {
                introducerOf(commandParts);
            } else if (command.startsWith("Friends_of ") && commandParts.length == 2) {
                friendsOf(commandParts);
            } else if (command.startsWith("Credit_of ") && commandParts.length == 2) {
                creditOf(commandParts);
            } else if (command.startsWith("Users_on_the_same_level_with") && commandParts.length == 2) {
                usersOnSameLevelWith(commandParts);
            } else if (command.startsWith("How_much_have_we_made_yet")) {
                System.out.println((int) Chain.getMahdizMoney());
            }
//            for (Table table : Chain.getTables()) {
//                    System.out.print(table.tableNumber + " : ");
//                for (User user : table.getUsers()) {
//                    System.out.print(user.getUsername() + " ");
//                }
//                System.out.println("");
//            }
        }
    }

    private static void usersOnSameLevelWith(String[] commandParts) {
        for (User user : Chain.getUsers()) {
            if (user.getUsername().equals(commandParts[1])) {
                ArrayList<User> list = user.getTable().getUsers();
                if (list.size() == 1) {
                    System.out.println("He_is_all_by_himself");
                    return;
                } else
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
        int level = Integer.parseInt(commandParts[1]);
        try {
            System.out.println(Chain.getTables().get(level).getUsers().size());
        } catch (Exception e) {
            System.out.println("No_such_level_found");
        }
    }

    private static void friendsOf(String[] commandParts) {
        if (Chain.isUsernameFree(commandParts[1])){
            System.out.println("No_such_user_found");
            return;
        }
        for (User user : Chain.getUsers()) {
            if (user.getUsername().equals(commandParts[1])) {
                ArrayList<User> users = user.getTable().getUsers();
                int index = users.indexOf(user);
                if (users.size() == 1) {
                    System.out.println("No_friend");
                    return;
                }
                if (users.size() == 2) {
                    if (users.get(0).equals(user))
                        System.out.println(users.get(1).getUsername());
                    else
                        System.out.println(users.get(0).getUsername());
                }else{
                    // Left
                    if (index > 0){
                        System.out.print(users.get(index - 1).getUsername() + " ");
                    } else{
                        System.out.print(users.get(users.size() -1).getUsername() + " ");
                    }

                    // Right
                    if (index < users.size() -1){
                        System.out.print(users.get(index + 1).getUsername());
                    }else{
                        System.out.print(users.get(0).getUsername());
                    }

                    System.out.println("");
                }
            }
        }
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
        System.out.println((int) Chain.getUserByUsername(username).getMoney());
    }

    private static void invitationRequest(String[] commandParts) {
        String username = commandParts[3];
        if (!Chain.isUsernameFree(username)) {
            System.out.println("Username already taken");
            return;
        }
        User inviter = Chain.getUserByUsername(commandParts[1].trim());
        double deposit = Integer.parseInt(commandParts[5]);
        User user = new User(username, deposit * 0.20, inviter);
        Chain.getUsers().add(user);
        inviter.addMoney(deposit * 0.05);
        Chain.addMoneyToMahdiz(deposit * 0.15);
        Chain.getChainFounder().addMoney(deposit * 0.10);
        Table table = Chain.getFirstNotFullTableAfterUser(inviter);
        table.addUser(user);
        user.setTable(table);
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
        System.out.println("User added successfully in level " + user.getTable().getTableNumber());
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
        Chain.getUsers().add(founder);
        table.getUsers().add(founder);
        Chain.addMoneyToMahdiz(5000);
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
            user.setTable(table);
            Chain.getUsers().add(user);
            Chain.getTables().add(table);
            ArrayList<Table> tables = Chain.getTables();
            deposit *= 0.5;
            addMoneyToUsers(deposit, table, tables);
            System.out.println("User added successfully in level " + (tables.size() -1));
        } else
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

    public static class Chain {
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
            return null;
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
            if (user.getTable().getTableNumber() <2) return;
            Table upTable = tables.get(tables.indexOf(user.getTable()) -1);
            if (!upTable.isFull()){
                upTable.addUser(user);
                user.setInvitationCount(0);
                return;
            }
            user.setInvitationCount(0);
            List<User> sortedUsers = (upTable.getUsers().stream().sorted(Comparator.comparing(User::getInvitationCount).thenComparing(User::getMoney)).collect(Collectors.toList()));
            User worstUser = sortedUsers.get(sortedUsers.size() -1);

            worstUser.getTable().getUsers().remove(worstUser);
            user.getTable().getUsers().remove(user);
            worstUser.getTable().addUser(user);
            user.getTable().addUser(worstUser);
            worstUser.setTable(user.getTable());
//            user.getTable().addUser(worstUser);
//            upTable.addUser(user);
            user.setTable(upTable);
            user.setInvitationCount(0);
//
//            User tempWorst = new User(worstUser.getUsername(), worstUser.getMoney(), worstUser.getTable(), worstUser.getInviter(), worstUser.getInvitationCount());
//            User tempGood = new User(user.getUsername(), user.getMoney(), user.getTable(), user.getInviter(), 0);
//            user.getTable().getUsers().set(user.getTable().getUsers().indexOf(user), tempWorst);
//            worstUser.getTable().getUsers().set(worstUser.getTable().getUsers().indexOf(worstUser), tempGood);
//            Chain.getUsers().remove(user);
//            Chain.getUsers().remove(worstUser);
//            Chain.getUsers().add(tempGood);
//            Chain.getUsers().add(tempWorst);
        }
    }

    public static class Table {
        private ArrayList<User> users;
        private int tableNumber;
        private int capacity;

        public Table(int tableNumber) {
            this.users = new ArrayList<>();
            this.tableNumber = tableNumber;
            this.capacity = tableNumber*tableNumber;
        }

        public ArrayList<User> getUsers() {
            return users;
        }

        public void setUsers(ArrayList<User> users) {
            this.users = users;
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(int tableNumber) {
            this.tableNumber = tableNumber;
        }

        public boolean isFull(){
            return (users.size() >= capacity);
        }

        public void addUser(User user) {
            users.add(user);
        }
    }

    public static class User {
        private String username;
        private double money;
        private Table table;
        private User inviter;
        private int invitationCount;

        public User(String username, double money, Table table, User inviter, int invitationCount) {
            this.username = username;
            this.money = money;
            this.table = table;
            this.inviter = inviter;
            this.invitationCount = invitationCount;
        }

        public User(String username, double money, User inviter) {
            this.username = username;
            this.money = money;
            this.inviter = inviter;
            this.invitationCount = 0;
        }
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public Table getTable() {
            return table;
        }

        public void setTable(Table table) {
            this.table = table;
        }

        public User getInviter() {
            return inviter;
        }

        public void setInviter(User inviter) {
            this.inviter = inviter;
        }

        public int getInvitationCount() {
            return invitationCount;
        }

        public void setInvitationCount(int invitationCount) {
            this.invitationCount = invitationCount;
        }

        public void addMoney(double amount) {
            money += amount;
        }
    }
}
