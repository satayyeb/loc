package Models;

import java.util.ArrayList;

public class Table {
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
}

