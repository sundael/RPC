package com.ljw.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  15:42
 * @Description: TODO
 * @Version: 1.0
 */
public class ReflectUtil {

    public static String getStackTrace(){
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        return stackTrace[stackTrace.length-1].getClassName();
    }

    //由路径和包名递归获得包下面的所有class文件
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes){
        File dir = new File(packagePath);

        if (!dir.exists()||!dir.isDirectory()){
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (recursive&&pathname.isDirectory())||(pathname.getName().endsWith(".class"));

            }
        });
        for (File file:dirFiles){
            if (file.isDirectory()){
                findAndAddClassesInPackageByFile(packageName+"."+file.getName(),file.getAbsolutePath(),recursive,classes);
            }else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 由包名获取所有的类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClasses(String packageName){
        LinkedHashSet<Class<?>> classes = new LinkedHashSet<>();
        boolean recursive=true;
        String packageDirName = packageName.replace(".", "/");
        Enumeration<URL> dirs;
        try {
            dirs=Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)){
                    String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                    findAndAddClassesInPackageByFile(packageName,filePath,recursive,classes);
                }else if ("jar".equals(protocol)) {
                    JarFile jar;
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while(entries.hasMoreElements()){
                        JarEntry jarEntry = entries.nextElement();
                        String name = jarEntry.getName();
                        if (name.charAt(0) == '/'){
                            name=name.substring(1);
                        }
                        if (name.startsWith(packageDirName)){
                            int index = name.lastIndexOf('/');
                            if (index!=-1){
                                packageName=name.substring(0,index).replace('/','.');
                            }
                            if (index!=-1||recursive){
                                if (name.endsWith(".class")&&!jarEntry.isDirectory()){
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        classes.add(Class.forName(packageName+"."+className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

}
