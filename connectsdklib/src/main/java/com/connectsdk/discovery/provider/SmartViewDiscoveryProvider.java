/*
 * CastDiscoveryProvider
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 20 Feb 2014
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

package com.connectsdk.discovery.provider;

import android.content.Context;
import android.util.Log;

import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryProvider;
import com.connectsdk.discovery.DiscoveryProviderListener;
import com.connectsdk.service.MultiScreenService;
import com.connectsdk.service.config.ServiceDescription;
import com.samsung.multiscreen.Device;
import com.samsung.multiscreen.Error;
import com.samsung.multiscreen.Result;
import com.samsung.multiscreen.Search;
import com.samsung.multiscreen.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SmartViewDiscoveryProvider implements DiscoveryProvider {
    private static final long ROUTE_REMOVE_INTERVAL = 3000;

    private Search search;
    protected ConcurrentHashMap<String, ServiceDescription> foundServices;
    protected CopyOnWriteArrayList<DiscoveryProviderListener> serviceListeners;

    boolean isRunning = false;

    public SmartViewDiscoveryProvider(Context context) {
        // Get an instance of Search
        search = Service.search(context);

        // Add a listener for the service found event
        search.setOnServiceFoundListener(
                new Search.OnServiceFoundListener() {

                    @Override
                    public void onFound(final Service service) {

                        service.isDMPSupported(new Result<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {

                                service.getDeviceInfo(new Result<Device>() {
                                    @Override
                                    public void onSuccess(Device device) {

                                        ServiceDescription foundService = new ServiceDescription(MultiScreenService.ID, service.getId(), device.getIp());
                                        foundService.setFriendlyName(device.getName());
                                        foundService.setModelName(device.getModel());
                                        foundService.setModelNumber(device.getFirmwareVersion());
                                        foundService.setModelDescription(device.getDescription());
                                        foundService.setServiceID(MultiScreenService.ID);
                                        foundService.setDevice(service);

                                        foundServices.put(device.getId(), foundService);

                                        for (DiscoveryProviderListener listener: serviceListeners) {
                                            listener.onServiceAdded(SmartViewDiscoveryProvider.this, foundService);
                                        }
                                    }

                                    @Override
                                    public void onError(Error error) {}
                                });
                            }

                            @Override
                            public void onError(Error error) {}
                        });
                    }
                }
        );

        // Add a listener for the service lost event
        search.setOnServiceLostListener(
                new Search.OnServiceLostListener() {

                    @Override
                    public void onLost(final Service service) {
                        Log.d("", "Search.onLost() service: " + service.toString());

                        service.getDeviceInfo(new Result<Device>() {

                            @Override
                            public void onSuccess(Device device) {
                                ServiceDescription serviceDescriptor = foundServices.remove(device.getId());
                                for (DiscoveryProviderListener listener: serviceListeners) {
                                    listener.onServiceRemoved(SmartViewDiscoveryProvider.this, serviceDescriptor);
                                }
                            }

                            @Override
                            public void onError(Error error) {

                            }
                        });
                    }
                }
        );

        // Start the discovery process
        //search.start();

        foundServices = new ConcurrentHashMap<String, ServiceDescription>(8, 0.75f, 2);
        serviceListeners = new CopyOnWriteArrayList<DiscoveryProviderListener>();
    }

    @Override
    public void start() {
        if (!isRunning) {
            isRunning = true;
            search.start();
        }
    }

    @Override
    public void stop() {
        if(isRunning) {
            isRunning = false;
            search.stop();
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void reset() {
        stop();
        foundServices.clear();
    }

    @Override
    public void rescan() {
        // TODO
    }

    @Override
    public void addListener(DiscoveryProviderListener listener) {
        serviceListeners.add(listener);
    }

    @Override
    public void removeListener(DiscoveryProviderListener listener) {
        serviceListeners.remove(listener);
    }

    @Override
    public void addDeviceFilter(DiscoveryFilter filter) {}

    @Override
    public void removeDeviceFilter(DiscoveryFilter filter) {}

    @Override
    public void setFilters(java.util.List<DiscoveryFilter> filters) {}

    @Override
    public boolean isEmpty() {
        return false;
    }

}
