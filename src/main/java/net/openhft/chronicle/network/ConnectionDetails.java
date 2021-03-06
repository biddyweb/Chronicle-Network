/*
 * Copyright 2016 higherfrequencytrading.com
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

package net.openhft.chronicle.network;

/**
 * Created by daniel on 10/02/2016.
 */
public class ConnectionDetails extends VanillaNetworkContext {
    private boolean isConnected;
    private String id;
    private String hostNameDescription;
    private boolean disable;

    public ConnectionDetails(String id, String hostNameDescription) {
        this.id = id;
        this.hostNameDescription = hostNameDescription;
    }

    public String getID() {
        return id;
    }

    boolean isConnected() {
        return isConnected;
    }

    void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getHostNameDescription() {
        return hostNameDescription;
    }

    public void setHostNameDescription(String hostNameDescription) {
        this.hostNameDescription = hostNameDescription;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    @Override
    public String toString() {
        return "ConnectionDetails{" +
                "isConnected=" + isConnected +
                ", id='" + id + '\'' +
                ", hostNameDescription='" + hostNameDescription + '\'' +
                ", disable=" + disable +
                '}';
    }
}
