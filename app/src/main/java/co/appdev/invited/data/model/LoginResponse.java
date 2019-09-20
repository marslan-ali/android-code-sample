/**
 * Class Module
 * @package     Application
 * @author      Arslan Ali
 * @email       marslan.ali@gmail.com
 */
package co.appdev.invited.data.model;

import com.google.gson.annotations.SerializedName;


public class LoginResponse {

    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("error")
    private String error;
    @SerializedName("message")
    private String message;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
