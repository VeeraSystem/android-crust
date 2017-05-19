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
