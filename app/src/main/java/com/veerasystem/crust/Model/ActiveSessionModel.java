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

public class ActiveSessionModel {
    @SerializedName("count")
    @Expose
    private int mCount;

    @SerializedName("next")
    @Expose
    private String mNext;

    @SerializedName("previous")
    @Expose
    private String mPrevious;

    @SerializedName("results")
    @Expose
    private List<Result> sResult = new ArrayList<Result>();

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public String getmNext() {
        return mNext;
    }

    public void setmNext(String mNext) {
        this.mNext = mNext;
    }

    public String getmPrevious() {
        return mPrevious;
    }

    public void setmPrevious(String mPrevious) {
        this.mPrevious = mPrevious;
    }

    public List<Result> getsResult() {
        return sResult;
    }

    public void setsResult(List<Result> sResult) {
        this.sResult = sResult;
    }

    public class Result {
        @SerializedName("id")
        @Expose
        private int mId;

        @SerializedName("created_at")
        @Expose
        private String mCreatedAt;

        @SerializedName("remoteuser")
        @Expose
        private String mRemoteUser;

        @SerializedName("server")
        @Expose
        private String mServer;

        @SerializedName("serveraccount")
        @Expose
        private String mServerAccount;

        @SerializedName("status")
        @Expose
        private String mStatus;

        @SerializedName("terminated_at")
        @Expose
        private String mTerminatedAt;

        @SerializedName("pid")
        @Expose
        private int mPid;

        @SerializedName("termination_cause")
        @Expose
        private String mTerminationCause;

        @SerializedName("session_id")
        @Expose
        private String mSessionId;

        @SerializedName("client_ip")
        @Expose
        private String mClientIp;

        @SerializedName("failed")
        @Expose
        private boolean mFailed;

        @SerializedName("fail_reason")
        @Expose
        private String mFailReason;

        public int getmId() {
            return mId;
        }

        public String getmTerminationCause() {
            return mTerminationCause;
        }

        public String getmTerminatedAt() {
            return mTerminatedAt;
        }

        public int getmPid() {
            return mPid;
        }

        public String getmCreatedAt() {
            return mCreatedAt;
        }

        public String getmClientIp() {
            return mClientIp;
        }

        public String getmFailReason() {
            return mFailReason;
        }

        public String getmRemoteUser() {
            return mRemoteUser;
        }

        public String getmServer() {
            return mServer;
        }

        public String getmServerAccount() {
            return mServerAccount;
        }

        public String getmSessionId() {
            return mSessionId;
        }

        public String getmStatus() {
            return mStatus;
        }

        public void setmTerminationCause(String mTerminationCause) {
            this.mTerminationCause = mTerminationCause;
        }

        public void setmTerminatedAt(String mTerminatedAt) {
            this.mTerminatedAt = mTerminatedAt;
        }

        public void setmClientIp(String mClientIp) {
            this.mClientIp = mClientIp;
        }

        public void setmCreatedAt(String mCreatedAt) {
            this.mCreatedAt = mCreatedAt;
        }

        public void setmFailed(boolean mFailed) {
            this.mFailed = mFailed;
        }

        public void setmFailReason(String mFailReason) {
            this.mFailReason = mFailReason;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public void setmPid(int mPid) {
            this.mPid = mPid;
        }

        public void setmRemoteUser(String mRemoteUser) {
            this.mRemoteUser = mRemoteUser;
        }

        public void setmServer(String mServer) {
            this.mServer = mServer;
        }

        public void setmServerAccount(String mServerAccount) {
            this.mServerAccount = mServerAccount;
        }

        public void setmSessionId(String mSessionId) {
            this.mSessionId = mSessionId;
        }

        public void setmStatus(String mStatus) {
            this.mStatus = mStatus;
        }
    }
}


