package com.adrianj.trainapp.general;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileManager {
    private static final String FILE_NAME = "credentials.txt";
    private static final String TOKEN_FILE = "token.txt";

    public static void saveCredentials(Context context, String username, String password, String id, String name, String surname, String birth) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String credentials = username + ":" + password + ":" + id + ":" + name + ":" + surname + ":" + birth;
            fileOutputStream.write(credentials.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e("FileManager", "Error to save credentials: " + e.getMessage());
        }
    }

    public static void saveToken(Context context, String token){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput(TOKEN_FILE, Context.MODE_PRIVATE);
            String tokenString = token;
            fileOutputStream.write(tokenString.getBytes());
            fileOutputStream.close();
        }catch (Exception e) {
            Log.e("FileManager", "Error to save token: " + e.getMessage());
        }
    }

    public static String getToken(Context context){

        try{
            FileInputStream fileInputStream = context.openFileInput(TOKEN_FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String token = bufferedReader.readLine();
            bufferedReader.close();
            return token;
        }catch (Exception e) {
            Log.e("FileManager", "Error to get credentials: " + e.getMessage());
        }
        return null;
    }

    public static String[] getCredentials(Context context) {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String credentials = bufferedReader.readLine();
            bufferedReader.close();
            return credentials.split(":");
        } catch (Exception e) {
            Log.e("FileManager", "Error to get credentials: " + e.getMessage());
        }
        return null;
    }
    public static void deleteCredentials(Context context) {
        context.deleteFile(FILE_NAME);
    }
    public static void deleteToken(Context context) {
        context.deleteFile(TOKEN_FILE);
    }
}
