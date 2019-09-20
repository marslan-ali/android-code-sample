/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       shayansolutions@gmail.com
 */
package co.appdev.invited.data.model;

import com.google.gson.annotations.SerializedName;

public class User {



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("id")
    private Integer id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    @SerializedName("gender_id")
    private Integer genderId;

    @SerializedName("dob")
    private String dob;

    @SerializedName("dateofrelation")
    private String dor;

    @SerializedName("profileImage")
    private String profileImage;

    @SerializedName("facebook_id")
    private String facebookId;

    @SerializedName("instagram_id")
    private String instagramId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    @SerializedName("created_at")
    private  String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;
}
