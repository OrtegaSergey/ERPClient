package DesktopClient.Secondary;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
//import java.util.Date;

public class Person implements Serializable {

    private String note;
    private int id;
    private String address;
    private Timestamp agreed;
    private String name;
    private String phone;
    private String email;
    private Date created;
    private Date archived;
    private String createdBy;
    private int branch;
    private boolean alert;

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public Date getCreated() {
        return created;
    }

    public Date getArchived() {
        return archived;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Timestamp getAgreed() {
        return agreed;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public int getBranch() {
        return branch;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAgreed(Timestamp agreed) {
        this.agreed = agreed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setArchived(Date archived) {
        this.archived = archived;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Person(String note, ResultSet rs) throws SQLException {
        this.note = note;
        this.id = rs.getInt("id");
        this.address = rs.getString("address");
        this.agreed = rs.getTimestamp("agreed");
        this.name = rs.getString("name");
        this.phone = rs.getString("phone");
        this.email = rs.getString("mail");
        this.created = rs.getDate("creation_date");
        this.archived = rs.getDate("archived");
        this.createdBy = rs.getString("createdBy");
        this.branch = rs.getInt("branch");
        this.alert = rs.getBoolean("alert");
    }


    public Person(){
    }
}
