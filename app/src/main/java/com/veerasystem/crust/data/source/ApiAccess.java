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

package com.veerasystem.crust.data.source;

import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Observable;


public interface ApiAccess {

    Observable<ResponseBody> getOtp(String username, String password);

    Observable<ResponseBody> login(String username, String password, String otp);

    Observable<ResponseBody> logout(String token);

    Observable<ResponseBody> getServerAccountCount(String tokenID);

    Observable<ResponseBody> getServerGroupCount(String tokenID);

    Observable<ResponseBody> getServerCount(String tokenID);

    Observable<ResponseBody> getRemoteUsersCount(String tokenID);

    Observable<ResponseBody> getActiveConnections(String tokenID, int active, int page, int pageSize);

    Observable<ResponseBody> getFailedConnections(String tokenID);

    Observable<ResponseBody> getActiveSessions(String tokenID, int active, int page, int pageSize);

    Observable<ResponseBody> getFailedSessions(String tokenID);

    Observable<ResponseBody> getFilterActiveSessions(String tokenID, int active, HashMap<String, String> parameters);

    Observable<ResponseBody> killSession(String tokenID, int sessionID);

    Observable<ResponseBody> sendSessionMessage(String tokenID, String message, int sessionID);

}
