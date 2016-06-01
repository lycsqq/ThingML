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
package org.thingml.testjar;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.thingml.testjar.lang.TargetedLanguage;

/**
 *
 * @author sintef
 */
public class TestHelper {
	

    public static Set<File> listTestFiles(final File folder, String pattern) {
        Set<File> res = new HashSet<>();
        Pattern p = Pattern.compile(pattern);
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                res.addAll(listTestFiles(fileEntry, pattern));
            } else {
                Matcher m = p.matcher(fileEntry.getName());
                
                if (m.matches()) {
                    res.add(fileEntry);
                }
            }
        }
        
        return res;
    }
	

    public static Set<File> whiteListFiles(final File folder, Set<String> whiteList) {
        String testPattern = "test(.+)\\.thingml";
        Set<File> res = new HashSet<>();
        
        for (final File fileEntry : listTestFiles(folder, testPattern)) {
            if (fileEntry.isDirectory()) {
                res.addAll(whiteListFiles(fileEntry, whiteList));
            } else {
                String fileName = fileEntry.getName().split("\\.thingml")[0];
                boolean found = false;
                for(String s : whiteList) {
                    if (fileName.compareTo(s) == 0) {
                        found = true;
                    }
                }
                if(found)
                    res.add(fileEntry);
            }
        }
        
        return res;
    }
	

    public static Set<File> blackListFiles(final File folder, Set<String> blackList) {
        String testPattern = "test(.+)\\.thingml";
        Set<File> res = new HashSet<>();
        
        for (final File fileEntry : listTestFiles(folder, testPattern)) {
            if (fileEntry.isDirectory()) {
                res.addAll(blackListFiles(fileEntry, blackList));
            } else {
                String fileName = fileEntry.getName().split("\\.thingml")[0];
                boolean found = false;
                for(String s : blackList) {
                    if (fileName.compareTo(s) == 0) {
                        found = true;
                    }
                }
                if(!found)
                    res.add(fileEntry);
            }
        }
        
        return res;
    }
    
    public static Set<File> listTestDir(final File folder, String pattern) {
        Set<File> res = new HashSet<>();
        Pattern p = Pattern.compile(pattern);
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                //res.addAll(listTestFiles(fileEntry, pattern));
                Matcher m = p.matcher(fileEntry.getName());
                
                if (m.matches()) {
                    res.add(fileEntry);
                }
            }
        }
        
        return res;
    }
    
    public static List<TestCase> listSamples(File srcDir, List<TargetedLanguage> langs, File compilerJar, File genCodeDir, File logDir) {
        String pattern = "(.+)\\.thingml";
        Pattern p = Pattern.compile(pattern);
        List<TestCase> res = new LinkedList<>();
        System.out.println("List samples:");
        //Explorer de manière récursive les dossiers
        for (final File fileEntry : srcDir.listFiles()) {
            if (!fileEntry.isDirectory()) {
                //res.addAll(listTestFiles(fileEntry, pattern));
                Matcher m = p.matcher(fileEntry.getName());
                
                if (m.matches()) {
                    boolean specificLang = false;
                    for(TargetedLanguage lang : langs) {
                        if(lang.compilerID.compareToIgnoreCase("_" + fileEntry.getParent()) == 0) {
                            specificLang = true;
                            System.out.println("    -" + fileEntry.getName() + "(" + lang.compilerID + ")");
                            res.add(new TestCase(fileEntry, compilerJar, lang, genCodeDir, fileEntry.getParentFile().getParentFile(), logDir, true));
                        }
                    }
                    
                    if(!specificLang) {
                    }
                }
            }
        }
        
        return res;
    }
    
}