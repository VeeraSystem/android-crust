/*************************************************************************
 * Veera CONFIDENTIAL
 * __________________
 * <p>
 * [2016] Veera System Incorporated
 * All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains
 * the property of Veera System Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Veera System Incorporated
 * and its suppliers and may be covered by IR. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Veera System Incorporated.
 */

package com.veerasystem.crust.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FailedConnectionModel {

    @SerializedName("users_fail_counts")
    @Expose
    List<UsersFailCount> usersFailCounts = new ArrayList<UsersFailCount>();

    public List<UsersFailCount> getUsersFailCounts() {
        return usersFailCounts;
    }

    public void setUsersFailCounts(List<UsersFailCount> usersFailCounts) {
        this.usersFailCounts = usersFailCounts;
    }

    public class UsersFailCount {
        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("source_ip")
        @Expose
        private String sourceIp;

        @SerializedName("state")
        @Expose
        private String state;

        @SerializedName("total")
        @Expose
        private int total;

        @SerializedName("fail_reason")
        @Expose
        private String failReason;

        public int getTotal() {
            return total;
        }

        public String getFailReason() {
            return failReason;
        }

        public String getSourceIp() {
            return sourceIp;
        }

        public String getState() {
            return state;
        }

        public String getUsername() {
            return username;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public void setSourceIp(String sourceIp) {
            this.sourceIp = sourceIp;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
