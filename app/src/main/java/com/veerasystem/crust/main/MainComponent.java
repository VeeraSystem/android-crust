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

package com.veerasystem.crust.main;

import com.veerasystem.crust.dashboard.DashboardPresenterModule;
import com.veerasystem.crust.data.source.RemoteComponent;

import dagger.Component;

@MainActivityScope
@Component(dependencies = RemoteComponent.class, modules = {MainPresenterModule.class, DashboardPresenterModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);

}
