/*************************************************************************
 *
 * Veera CONFIDENTIAL
 * __________________
 *
 *  [2016] Veera System Incorporated
 *  All Rights Reserved.
 *
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

import java.util.List;

/**
 * Created by tux on 5/9/16.
 */
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerCountChartModel {

    @SerializedName("server_counts")
    @Expose
    private List<List<String>> serverCounts = new ArrayList<List<String>>();

    /**
     *
     * @return
     * The serverCounts
     */
    public List<List<String>> getServerCounts() {
        return serverCounts;
    }

    /**
     *
     * @param serverCounts
     * The server_counts
     */
    public void setServerCounts(List<List<String>> serverCounts) {
        this.serverCounts = serverCounts;
    }

}

//public class ServerCountChartModel {
//
//
//    List<Chart> charts;
//
//    public List<Chart> getCharts() {
//        return charts;
//    }
//
//    public void setCharts(List<Chart> charts) {
//        this.charts = charts;
//    }
//
//    public class Chart {
//
//        List<Value> values;
//
//        public List<Value> getValues() {
//            return values;
//        }
//
//        public void setValues(List<Value> values) {
//            this.values = values;
//        }
//
//        public class Value {
//            String name;
//            int value;
//
//            public int getValue() {
//                return value;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public void setValue(int value) {
//                this.value = value;
//            }
//        }
//    }
//}
