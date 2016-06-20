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

public class FailedSessionModel {

    @SerializedName("sessions_fail_counts")
    @Expose
    List<SessionFailCount> SessionFailCounts = new ArrayList<SessionFailCount>();

    public List<SessionFailCount> getSessionFailCounts() {
        return SessionFailCounts;
    }

    public void setSessionFailCounts(List<SessionFailCount> SessionFailCounts) {
        this.SessionFailCounts = SessionFailCounts;
    }

    public class SessionFailCount {
        @SerializedName("remoteuser")
        @Expose
        private String remoteUser;

        @SerializedName("client_ip")
        @Expose
        private String clientIp;

        @SerializedName("state")
        @Expose
        private String state;

        @SerializedName("server")
        @Expose
        private String server;

        @SerializedName("serveraccount")
        @Expose
        private String serverAccount;

        @SerializedName("total")
        @Expose
        private int total;

        @SerializedName("fail_reason")
        @Expose
        private String failReason;

        public int getTotal() {
            return total;
        }

        public String getClientIp() {
            return clientIp;
        }

        public String getFailReason() {
            return failReason;
        }

        public String getRemoteUser() {
            return remoteUser;
        }

        public String getServer() {
            return server;
        }

        public String getState() {
            return state;
        }

        public String getServerAccount() {
            return serverAccount;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public void setRemoteUser(String remoteUser) {
            this.remoteUser = remoteUser;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public void setServerAccount(String serverAccount) {
            this.serverAccount = serverAccount;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
