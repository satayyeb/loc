package Models;

public class User {
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
