/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.spamkeywordsdatasource;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
//Thread-safe class
public class HostBlacklistsDataSourceFacade {
    boolean confiable =true ;

    public static String[] servidor1 = new String[]{"1.1.1.1", "2.2.2.2","3.3.3.3.3","4.4.4.4","5.5.5.5"};
    public static String[] servidor2 = new String[]{"1.1.1.1", "2.2.2.3", "3.3.3.4", "4.4.4.5", "5.5.5.5"};
    public static String[] servidor3 = new String[]{"1.1.1.1", "2.2.3.2","3.3.3.3.3","4.4.5.4","5.5.5.5"};
    public static String[] servidor4 = new String[]{"1.1.1.1", "2.3.2.2","3.3.3.3.3","4.4.4.4","5.5.5.5"};
    public static String[] servidor5 = new String[]{"1.1.1.1", "3.2.2.2","3.3.3.3.3","4.4.4.4","5.5.5.5"};
    public ArrayList<String[]> servidores = new ArrayList<String[]>();

    public HostBlacklistsDataSourceFacade(){
        servidores.add(servidor1);
        servidores.add(servidor2);
        servidores.add(servidor3);
        servidores.add(servidor4);
        servidores.add(servidor5);
    }
    public int getRegisteredServersCount(){
        return 5;
    }

    public boolean isInBlackListServer(int i,String ip){
        String[] servidor = servidores.get(i);
        for(String server : servidor){
            if(server.equals(ip)){
                return true;
            }
        }
        return false;
    }

    public void reportAsNotTrustworthy(String ipaddress) {
        confiable = false;
    }

    public void reportAsTrustworthy(String ipaddress) {
        confiable = true;
    }

    public boolean isConfiable(){
        return confiable;
    }

//TOCAR ESTE CODIGO!!

    
}