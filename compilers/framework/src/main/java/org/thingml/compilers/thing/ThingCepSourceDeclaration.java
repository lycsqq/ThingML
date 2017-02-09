/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */
package org.thingml.compilers.thing;

import org.thingml.xtext.thingML.*;
import org.thingml.compilers.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ludovic
 */
public class ThingCepSourceDeclaration {
    public void generate(Stream stream, Source source, StringBuilder builder, Context context) {
        if (source instanceof SimpleSource) {
            generate(stream, (SimpleSource) source, builder, context);
        } else if (source instanceof MergeSources) {
            generate(stream, (MergeSources) source, builder, context);
        } else if (source instanceof JoinSources) {
            generate(stream, (JoinSources) source, builder, context);
        } else {
            throw new UnsupportedOperationException("CEP source " + source.getClass().getName() + " is not supported");
        }
    }

    public void generate(Stream stream, SimpleSource source, StringBuilder builder, Context context) {
        throw new UnsupportedOperationException("Cep declaration are platform-specific. Cannot generate stream inout for " + stream.getName());
    }

    public void generate(Stream stream, MergeSources source, StringBuilder builder, Context context) {
        throw new UnsupportedOperationException("Cep declaration are platform-specific. Cannot generate stream inout for " + stream.getName());
    }

    public void generate(Stream stream, JoinSources sources, StringBuilder builder, Context context) {
        throw new UnsupportedOperationException("Cep declaration are platform-specific. Cannot generate stream inout for " + stream.getName());
    }

    protected void generateOperatorCalls(String name, Source source, StringBuilder builder, Context context) {
        List<ViewSource> windows = new ArrayList<ViewSource>();
        if (source.getOperators().size() > 0) {
            builder.append(name + " = " + name);
            for (ViewSource view : source.getOperators()) {
                if (!(view instanceof TimeWindow) && !(view instanceof LengthWindow)) {
                    context.getCompiler().getCepCompiler().getCepViewCompiler().generate(view, builder, context);
                } else {
                    windows.add(view);
                }
            }
            for (ViewSource view : windows) {
                context.getCompiler().getCepCompiler().getCepViewCompiler().generate(view, builder, context);
            }
            builder.append(";\n");
        }
    }
}
