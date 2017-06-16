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

import com.veerasystem.crust.data.ActiveConnectionModel;
import com.veerasystem.crust.data.ActiveSessionModel;
import com.veerasystem.crust.data.FailedConnectionModel;
import com.veerasystem.crust.data.FailedSessionModel;
import com.veerasystem.crust.data.RemoteUsersModel;
import com.veerasystem.crust.data.ServerAccountModel;
import com.veerasystem.crust.data.ServerCountModel;
import com.veerasystem.crust.data.ServerGroupCountModel;
import com.veerasystem.crust.data.TokenModel;

import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Observable;


public interface ApiAccess {

    Observable<ResponseBody> getOtp(String username, String password);

    Observable<TokenModel> login(String username, String password, String otp);

    Observable<ResponseBody> logout(String token);

    Observable<ServerAccountModel> getServerAccountCount(String tokenID);

    Observable<ServerGroupCountModel> getServerGroupCount(String tokenID);

    Observable<ServerCountModel> getServerCount(String tokenID);

    Observable<RemoteUsersModel> getRemoteUsersCount(String tokenID);

    Observable<ActiveConnectionModel> getActiveConnections(String tokenID, int active, int page, int pageSize);

    Observable<FailedConnectionModel> getFailedConnections(String tokenID);

    Observable<ActiveSessionModel> getActiveSessions(String tokenID, int active, int page, int pageSize);

    Observable<FailedSessionModel> getFailedSessions(String tokenID);

    Observable<ActiveSessionModel> getFilterActiveSessions(String tokenID, int active, HashMap<String, String> parameters);

    Observable<ResponseBody> killSession(String tokenID, int sessionID);

    Observable<ResponseBody> sendSessionMessage(String tokenID, String message, int sessionID);

}
