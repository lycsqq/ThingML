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
package org.thingml.externalthingplugins.c.posix.dnssd;

import org.sintef.thingml.Thing;
import org.thingml.compilers.DebugProfile;
import org.thingml.compilers.c.CCompilerContext;
import org.thingml.compilers.c.CThingApiCompiler;
import org.thingml.externalthingplugins.c.posix.PosixDNSSDExternalThingPlugin;

/**
 * Created by vassik on 01.11.16.
 */
public class PosixDNSSDThingApiCompiler extends CThingApiCompiler {

    private PosixDNSSDExternalThingPlugin plugin;

    public PosixDNSSDThingApiCompiler(PosixDNSSDExternalThingPlugin _plugin) {
        plugin = _plugin;
    }

    @Override
    protected void generateCHeaderCode(Thing thing, CCompilerContext ctx, StringBuilder builder, DebugProfile debugProfile) {
        super.generateCHeaderCode(thing, ctx, builder, debugProfile);

        builder.append("// Declaration of prototypes to integrate with DNSSD. Generated by " + this.getClass().getSimpleName() + "\n");

        builder.append("void ");
        builder.append(thing.getName() + "_setup");
        ctx.appendFormalParametersEmptyHandler(thing, builder);
        builder.append(";\n");

        builder.append("void ");
        builder.append(thing.getName() + "_startup");
        ctx.appendFormalParametersEmptyHandler(thing, builder);
        builder.append(";\n");

        builder.append("void ");
        builder.append(thing.getName() + "_shutdown");
        ctx.appendFormalParametersEmptyHandler(thing, builder);
        builder.append(";\n");

        builder.append("void ");
        builder.append(thing.getName() + "_add_dnssd_service");
        ctx.appendFormalParametersEmptyHandler(thing, builder);
        builder.append(";\n");

        builder.append("void ");
        builder.append(thing.getName() + "_remove_dnssd_service");
        ctx.appendFormalParametersEmptyHandler(thing, builder);
        builder.append(";\n");
    }
}