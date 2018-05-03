package com.weebly.bottleneckdevelopers.tkzy.models;

@SuppressWarnings("FieldCanBeLocal")
public class User {

    private String branch;
    private String email;
    private String name;
    private String phone;
    private String profile_image;
    private String security_level;
    private String semester;
    private String user_id;

    public User(String branch, String email, String name, String phone, String profile_image, String security_level,
                String semester, String user_id) {
        this.branch = branch;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.profile_image = profile_image;
        this.security_level = security_level;
        this.semester = semester;
        this.user_id = user_id;
    }

    public User() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "branch='" + branch + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", security_level='" + security_level + '\'' +
                ", semester='" + semester + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

}
