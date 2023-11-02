package com.sunshineoxygen.inhome.ui.dto;

import java.io.Serializable;

public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 9216107700514073589L;

    private UserDetailDTO updatedUser;

    private String updatedDate;

    private UserDetailDTO createdUser;

    private String createdDate;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserDetailDTO getUpdateUser() {
        return updatedUser;
    }

    public void setUpdateUser(UserDetailDTO updateUser) {
        this.updatedUser = updateUser;
    }

    public String getUpdateDate() {

		/*if (updateDate != null) {
			return CCMConstants.formatDate(updateDate);
		}
		*/
        return updatedDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updatedDate = updateDate;
    }

    public UserDetailDTO getCreateUser() {
        return createdUser;
    }

    public void setCreateUser(UserDetailDTO createUser) {
        this.createdUser = createUser;
    }

    public String getCreateDate() {

		/*if (createDate != null) {
			return CCMConstants.formatDate(createDate);
		}
		*/
        return createdDate;
    }

    public void setCreateDate(String createDate) {
        this.createdDate = createDate;
    }



}
