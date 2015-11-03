/**
 * Copyright (C) 2014 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.compilers.c;

import java.util.HashSet;
import java.util.Set;
import org.sintef.thingml.Configuration;
import org.sintef.thingml.ExternalConnector;
import org.thingml.compilers.c.CCompilerContext;

/**
 *
 * @author sintef
 */
public abstract class NetworkLibraryGenerator {
    private Set<ExternalConnector> ecoList;
    public CCompilerContext ctx;
    public Configuration cfg;
    
    
    public NetworkLibraryGenerator(Configuration cfg, CCompilerContext ctx) {
        this.cfg = cfg;
        this.ctx = ctx;
        this.ecoList = new HashSet<ExternalConnector>();
    }
    
    public NetworkLibraryGenerator(Configuration cfg, CCompilerContext ctx, Set<ExternalConnector> ExternalConnectors) {
        this.cfg = cfg;
        this.ctx = ctx;
        this.ecoList = ExternalConnectors;
    }
    
    public Set<ExternalConnector> getExternalConnectors() {
        return ecoList;
    }
    
    public void addExternalCnnector(ExternalConnector eco) {
        ecoList.add(eco);
    }
    
    /*
     * For each External Connector the implemented function must generate a file named Instance_port_PORT_NAME.c (and .h)
     * including at least the following functions:
     * -> void PORT_NAME_set_listener_id(uint16_t id);
     * -> void PORT_NAME_setup();
     * -> void PORT_NAME_start_receiver_process() ;
     * -> void PORT_NAME_forwardMessage(char * msg, int length);
     * Note that this last one can have additional parameters if they are correctly handled by generateMessageForwarders
     */
    public abstract void generateNetworkLibrary();
    
    /*
     * For each PORT_NAME::Thing::Port::Message
     */
    public abstract void generateMessageForwarders(StringBuilder builder);
}
