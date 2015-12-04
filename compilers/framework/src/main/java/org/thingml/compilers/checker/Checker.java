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
package org.thingml.compilers.checker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.sintef.thingml.*;
import org.thingml.compilers.Context;
import org.thingml.compilers.checker.Rule;
import org.thingml.compilers.checker.genericRules.*;

/**
 *
 * @author sintef
 */
abstract public class Checker {
    private Set<Rule> Rules;
    
    private Set<CheckerInfo> Errors;
    private Set<CheckerInfo> Warnings;
    private Set<CheckerInfo> Notices;
    
    private String compiler;
    private String generic;
    
    private Context ctx;

    public TypeChecker typeChecker = new TypeChecker();

    public Checker (String compiler) {
        Rules = new HashSet<Rule>();
        Errors = new HashSet<CheckerInfo>();
        Warnings = new HashSet<CheckerInfo>();
        Notices = new HashSet<CheckerInfo>();
        
        this.ctx = new Context(null);
        this.compiler = compiler;
        generic = "ThingML";
        
        Rules.add(new ThingsUsage());
        Rules.add(new PortsUsage());
        Rules.add(new MessagesUsage());
        Rules.add(new ConnectorCycles());
        Rules.add(new InternalTransitions());
        Rules.add(new AutotransitionCycles());
        Rules.add(new NonDeterministicTransitions());
        Rules.add(new FunctionUsage());
        Rules.add(new StatesUsage());
        Rules.add(new VariableUsage());
    }
    
    public void do_generic_check(Configuration cfg) {
        long start = System.currentTimeMillis();
        for(Rule r : Rules) {
            r.check(cfg, this);
        }
        System.out.println("checker took " + (System.currentTimeMillis() - start) + " ms");
    }
    
    // Must be implemented and must contain a call to do_generic_check(cfg)
    abstract public void do_check(Configuration cfg);
    
    
    // ---------------------- Accessors ----------------------
    
    public void addError(String msg, EObject el) {
        Errors.add(new CheckerInfo(InfoType.ERROR, compiler, msg, el));
    }
    
    public void addError(String compiler, String msg, EObject el) {
        Errors.add(new CheckerInfo(InfoType.ERROR, compiler, msg, el));
    }
    
    public void addGenericError(String msg, EObject el) {
        Errors.add(new CheckerInfo(InfoType.ERROR, generic, msg, el));
    }
    
    public void addWarning(String msg, EObject el) {
        Warnings.add(new CheckerInfo(InfoType.WARNING, compiler, msg, el));
    }
    
    public void addWarning(String compiler, String msg, EObject el) {
        Warnings.add(new CheckerInfo(InfoType.WARNING, compiler, msg, el));
    }
    
    public void addGenericWarning(String msg, EObject el) {
        Warnings.add(new CheckerInfo(InfoType.WARNING, generic, msg, el));
    }
    
    public void addNotice(String msg, EObject el) {
        Notices.add(new CheckerInfo(InfoType.NOTICE, compiler, msg, el));
    }
    
    public void addNotice(String compiler, String msg, EObject el) {
        Notices.add(new CheckerInfo(InfoType.NOTICE, compiler, msg, el));
    }
    
    public void addGenericNotice(String msg, EObject el) {
        Notices.add(new CheckerInfo(InfoType.NOTICE, generic, msg, el));
    }
    
    public boolean containsErrors() {
        return Errors.isEmpty();
    }
    
    public boolean containsWarnings() {
        return Warnings.isEmpty();
    }
    
    public boolean containsNotices() {
        return Notices.isEmpty();
    }
    
    public void printErrors() {
        for(CheckerInfo i : Errors) {
            System.out.print(i.toString());
        }
    }
    
    public void printWarnings() {
        for(CheckerInfo i : Warnings) {
            System.out.print(i.toString());
        }
    }
    
    public void printNotices() {
        for(CheckerInfo i : Notices) {
            System.out.print(i.toString());
        }
    }
    
    // ---------------------- Structures ----------------------
    
    public enum InfoType {ERROR, WARNING, NOTICE};
    
    public class CheckerInfo {
        public InfoType type;
        public String source;
        public String message;
        public EObject element;
        
        public CheckerInfo(InfoType type, String source, String message, EObject element) {
            this.type = type;
            this.source = source;
            this.message = message;
            this.element = element;
        }
        
        public String print(EObject el) {
            if (el instanceof ThingMLElement) {
                if(((ThingMLElement)el).getName() != null) {
                    return ((ThingMLElement)el).getName();
                }
            }
            return el.toString();
        }
        
        public String toString() {
            String t;
            switch(type) {
                case NOTICE:
                    t = "NOTICE";
                    break;
                    
                case WARNING:
                    t = "WARNING";
                    break;
                    
                case ERROR:
                    t = "ERROR";
                    break;
                default:
                    t = "";
            }
            
            
            return "[" + t + "] " + source + ": " + message + " (in " + print(element) + ")\n";
        }
    }
}