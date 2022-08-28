/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    public static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int hilos) throws InterruptedException {
        ArrayList<ThreadingSearch> hilosbusqueda = new ArrayList<>();
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        //Agregamos la variable ocurrences como entero atomico que nos va permitir tener un conteo sincronizado entre los hilos
        //y asi poder detener el proceso una vez encontremos que es insegura la direccion
        AtomicInteger ocurrences = new AtomicInteger(0);
        HostBlacklistsDataSourceFacade skds=new HostBlacklistsDataSourceFacade();
        AtomicInteger checkedListCount = new AtomicInteger(0);
        int tamano = skds.getRegisteredServersCount()/hilos;
        int primerserver;
        int finalserver;

        for(int i =0; i<hilos; i++){
            primerserver = tamano * i;
            finalserver = tamano * (i+1);
            hilosbusqueda.add(new ThreadingSearch(skds, primerserver, finalserver, ipaddress, ocurrences,checkedListCount,blackListOcurrences));
        }

        for( ThreadingSearch hilo : hilosbusqueda){
            hilo.start();
        }

        for(ThreadingSearch hilo : hilosbusqueda){
            hilo.join();
            blackListOcurrences.addAll(hilo.getBlackListOcurrences());
        }

        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListCount.get(), skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
