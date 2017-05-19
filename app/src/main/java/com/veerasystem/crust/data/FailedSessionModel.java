/*
 * Copyright (c) 2017 VEERA SYSTEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.veerasystem.crust.data;

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
