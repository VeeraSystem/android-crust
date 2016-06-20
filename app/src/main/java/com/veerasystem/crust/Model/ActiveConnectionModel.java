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

public class ActiveConnectionModel {
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

        @SerializedName("terminated_at")
        @Expose
        private String mTerminatedAt;

        @SerializedName("username")
        @Expose
        private String mUsername;

        @SerializedName("termination_cause")
        @Expose
        private String mTerminationCause;

        @SerializedName("pid")
        @Expose
        private int mPid;

        @SerializedName("source_ip")
        @Expose
        private String mSourceIp;

        @SerializedName("successful")
        @Expose
        private boolean mSuccessful;

        @SerializedName("fail_reason")
        @Expose
        private String mFailReason;

        @SerializedName("state")
        @Expose
        private String mState;

        public int getmId() {
            return mId;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public int getmPid() {
            return mPid;
        }

        public void setmPid(int mPid) {
            this.mPid = mPid;
        }

        public String getmCreatedAt() {
            return mCreatedAt;
        }

        public void setmCreatedAt(String mCreatedAt) {
            this.mCreatedAt = mCreatedAt;
        }

        public String getmFailReason() {
            return mFailReason;
        }

        public void setmFailReason(String mFailReason) {
            this.mFailReason = mFailReason;
        }

        public String getmSourceIp() {
            return mSourceIp;
        }

        public void setmSourceIp(String mSourceIp) {
            this.mSourceIp = mSourceIp;
        }

        public String getmState() {
            return mState;
        }

        public void setmState(String mState) {
            this.mState = mState;
        }

        public String getmTerminatedAt() {
            return mTerminatedAt;
        }

        public void setmTerminatedAt(String mTerminatedAt) {
            this.mTerminatedAt = mTerminatedAt;
        }

        public String getmTerminationCause() {
            return mTerminationCause;
        }

        public void setmTerminationCause(String mTerminationCause) {
            this.mTerminationCause = mTerminationCause;
        }

        public String getmUsername() {
            return mUsername;
        }

        public void setmUsername(String mUsername) {
            this.mUsername = mUsername;
        }

        public void setmSuccessful(boolean mSuccessful) {
            this.mSuccessful = mSuccessful;
        }

        public Boolean getmSuccessful() {
            return this.mSuccessful;
        }
    }
}


