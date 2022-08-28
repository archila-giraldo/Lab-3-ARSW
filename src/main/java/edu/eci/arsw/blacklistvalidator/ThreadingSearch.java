package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.eci.arsw.blacklistvalidator.HostBlackListsValidator.BLACK_LIST_ALARM_COUNT;

public class ThreadingSearch extends Thread {
    AtomicInteger ocurrences;
    String ip;
    int inicio;
    int fin;
    ArrayList<Integer> blackListOcurrences = new ArrayList<Integer>();
    AtomicInteger checkedListsCount;
    HostBlacklistsDataSourceFacade skds;


    ThreadingSearch(HostBlacklistsDataSourceFacade skds, int inicio, int fin, String ip, AtomicInteger ocurrences, AtomicInteger checkedListCount, LinkedList<Integer> blackListOcurrences){
        this.ip  = ip;
        this.inicio = inicio;
        this.fin = fin;
        this.skds = skds;
        this.ocurrences = ocurrences;
        this.checkedListsCount = checkedListCount;

    }

    @Override
    public void run() {
        //con el siguiente condicional podemos verificar si una direccion ya se dio como insegura y no seguir ejecutando el run
        if (skds.isConfiable()) {
            for (int i = inicio; i < fin; i++) {
                checkedListsCount.getAndIncrement();
                if (skds.isInBlackListServer(i, ip)) {
                    blackListOcurrences.add(i);
                    ocurrences.getAndIncrement();
                    if (ocurrences.get() >= BLACK_LIST_ALARM_COUNT) {
                        skds.reportAsNotTrustworthy(ip);
                        System.out.println("no confiable");
                    }
                }
            }
        }
    }

    public ArrayList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }



}
