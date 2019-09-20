/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */

package co.appdev.invited.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DefaultResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("email")
    private List<String> errorEmail;
    @SerializedName("phone")
    private List<String> errorphone;
    @SerializedName("Error")
    private String error;

    public String getNonUsers() {
        return nonUsers;
    }

    public void setNonUsers(String nonUsers) {
        this.nonUsers = nonUsers;
    }

    @SerializedName("non_users")
    private String nonUsers;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getErrorphone() {
        return errorphone;
    }

    public void setErrorphone(List<String> errorphone) {
        this.errorphone = errorphone;
    }

    public List<String> getErrorEmail() {
        return errorEmail;
    }

    public void setErrorEmail(List<String> errorEmail) {
        this.errorEmail = errorEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
 }
