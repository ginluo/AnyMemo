/*
Copyright (C) 2010 Haowen Ning

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.liberty.android.fantastischmemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;

/* This class handles the operations on recent list */
public class RecentListUtil{
    private static final int RECENT_LENGTH = 7;

    public static String getRecentDBName(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString("recentdbname0", null);
    }
    public static String getRecentDBPath(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return trimPath(settings.getString("recentdbpath0", null));
    }
    public static String[] getAllRecentDBName(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String[] ret = new String[RECENT_LENGTH];
        for(int i = 0; i < RECENT_LENGTH; i++){
            ret[i] = settings.getString("recentdbname" + i, null);
        }
        return ret;
    }
    public static String[] getAllRecentDBPath(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String[] ret = new String[RECENT_LENGTH];
        for(int i = 0; i < RECENT_LENGTH; i++){
            ret[i] = trimPath(settings.getString("recentdbpath" + i, null));
        }
        return ret;
    }
    public static void clearRecentList(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        for(int i = 0; i < RECENT_LENGTH; i++){
            editor.putString("recentdbname" + i, null);
            editor.putString("recentdbpath" + i, null);
        }
        editor.commit();
    }

    public static void deleteFromRecentList(Context context, String dbpath, String dbname){
        dbpath = trimPath(dbpath);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        String[] allNames = getAllRecentDBName(context);
        String[] allPaths = getAllRecentDBPath(context);
        clearRecentList(context);
        for(int i = 0, counter = 0; i < RECENT_LENGTH; i++){
            if(allNames[i] == null || allPaths[i] == null || (allNames[i].equals(dbname) &&  allPaths[i].equals(dbpath))){
                continue;
            }
            else{
                editor.putString("recentdbname" + counter, allNames[i]);
                editor.putString("recentdbpath" + counter, allPaths[i]);
                counter++;
            }
        }
        editor.commit();
    }

    public static void addToRecentList(Context context, String dbpath, String dbname){
        dbpath = trimPath(dbpath);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        deleteFromRecentList(context, dbpath, dbname);
        String[] allNames = getAllRecentDBName(context);
        String[] allPaths = getAllRecentDBPath(context);
        for(int i = RECENT_LENGTH - 1; i >= 1; i--){
            System.out.println("Index: " + i);
            editor.putString("recentdbname" + i, allNames[i - 1]);
            editor.putString("recentdbpath" + i, allPaths[i - 1]);
        }
        editor.putString("recentdbname" + 0, dbname);
        editor.putString("recentdbpath" + 0, dbpath);
        editor.commit();
    }

    private static String trimPath(String path){
        if(path == null || path.length() <= 1){
            return path;
        }
        /* trim all / at the end of the path */
        while(path.endsWith("/")){
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}

