package com.piccologrupa.zamjenasoba;

import com.piccologrupa.zamjenasoba.dao.OglasRepository;
import com.piccologrupa.zamjenasoba.dao.StudentRepository;
import com.piccologrupa.zamjenasoba.domain.Oglas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTasks {

    private long zadnjiIdLanca = 0;

    @Autowired
    private ZamjenaHandler zamjenaHandler;

    @Scheduled(fixedRate = 10000, initialDelay = 5000)
    public void ObnoviLanceZamjene() {
        int brojIzbrisanihLanaca;
        System.out.println("\nAutomatska obnova lanaca u tijeku");

        brojIzbrisanihLanaca = zamjenaHandler.provjeriSveLance();
        if (brojIzbrisanihLanaca == 0)
            System.out.format("Provjera prije generiranja novih lanaca: svi lanci ispravni\n");
        else {
            System.out.format("Provjera prije generiranja novih lanaca: izbrisano %d neispravnih lanaca\n", brojIzbrisanihLanaca);
        }

        List<Long> noviLanci = zamjenaHandler.generirajNoveLance();
        System.out.format("Generirano %d novih lanaca\n", noviLanci.size());

        brojIzbrisanihLanaca = zamjenaHandler.provjeriSveLance();
        if (brojIzbrisanihLanaca == 0)
            System.out.format("Provjera nakon generiranja novih lanaca: svi lanci ispravni\n");
        else {
            System.out.format("Provjera nakon generiranja novih lanaca: izbrisano %d neispravnih lanaca\n", brojIzbrisanihLanaca);
        }

        System.out.println("Automatska obnova lanaca zavrsena");
    }

}
