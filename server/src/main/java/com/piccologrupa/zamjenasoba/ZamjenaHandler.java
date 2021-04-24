package com.piccologrupa.zamjenasoba;

import com.piccologrupa.zamjenasoba.dao.LajkaniOglasRepository;
import com.piccologrupa.zamjenasoba.dao.MogucaZamjenaRepository;
import com.piccologrupa.zamjenasoba.dao.OglasRepository;
import com.piccologrupa.zamjenasoba.dao.StudentRepository;
import com.piccologrupa.zamjenasoba.domain.*;
import com.piccologrupa.zamjenasoba.response.ZamjenaResponse;
import com.piccologrupa.zamjenasoba.service.EmailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ZamjenaHandler {

    @Autowired
    private MogucaZamjenaRepository moguceZamjene;

    @Autowired
    private StudentRepository studenti;

    @Autowired
    private OglasRepository oglasi;

    @Autowired
    private LajkaniOglasRepository lajkaniOglasi;

    private long zadnjiIdLanca = 0;

    public long noviIdLanca() {
        do {
            zadnjiIdLanca++;
        } while (moguceZamjene.findByIdLanca(zadnjiIdLanca).size() > 0);
        return zadnjiIdLanca;
    }

    public List<MogucaZamjena> getLanac(Long idLanca) {
        return moguceZamjene.findByIdLanca(idLanca);
    }

    public int getLanacSize(Long idLanca) {
        return moguceZamjene.findByIdLanca(idLanca).size();
    }

    public int getBrojPotvrda(Long idLanca) {
        int brojPotvrda = 0;
        List<MogucaZamjena> zamjene = moguceZamjene.findByIdLanca(idLanca);
        for (MogucaZamjena zamjena : zamjene) {
            if (zamjena.getIdLanca().equals(zamjena.getOglas().getStudent().getPotvrdjeniLanac()))
                brojPotvrda++;
        }
        return brojPotvrda;
    }

    public MogucaZamjena getIducaKarika(Long idLanca, Short redniBroj) {
        Optional<MogucaZamjena> zamjena = moguceZamjene.findById(new ZamjenaID(idLanca, redniBroj));
        if (zamjena.isEmpty()) {
            return null;
        }
        zamjena = moguceZamjene.findById(new ZamjenaID(idLanca, (short)(redniBroj + 1)));
        if (zamjena.isPresent()) {
            return zamjena.get();
        }
        zamjena = moguceZamjene.findById(new ZamjenaID(idLanca, (short)1));
        if (zamjena.isPresent()) {
            return zamjena.get();
        }
        return null;
    }

    public List<Long> getOglasiLanca(Long idLanca) {
        List<Long> oglasi = new ArrayList<>();
        for (MogucaZamjena zamjena : moguceZamjene.findByIdLanca(idLanca))
            oglasi.add(zamjena.getOglas().getIdOglas());
        return oglasi;
    }

    public List<Long> getPonudjeniOglasi(Oglas oglas) {
        List<Long> ponude = new ArrayList<>();
        for (MogucaZamjena zamjena : getPonudjeneZamjene(oglas)) {
            ponude.add(zamjena.getOglas().getIdOglas());
        }
        return ponude;
    }

    public List<MogucaZamjena> getPonudjeneZamjene(Oglas oglas) {
        List<MogucaZamjena> zamjene = moguceZamjene.findByOglas(oglas);
        List<MogucaZamjena> ponude = new ArrayList<>();
        for (MogucaZamjena zamjena : zamjene) {
            MogucaZamjena ponuda = getIducaKarika(zamjena.getIdLanca(), zamjena.getRedniBroj());
            if (ponuda != null) {
                ponude.add(ponuda);
            }
        }
        return ponude;
    }

    public List<ZamjenaResponse> getPonudjeneZamjeneSaPotvrdom(Oglas oglas) {
        List<MogucaZamjena> zamjene = moguceZamjene.findByOglas(oglas);
        List<ZamjenaResponse> ponude = new ArrayList<>();
        for (MogucaZamjena zamjena : zamjene) {
            MogucaZamjena ponuda = getIducaKarika(zamjena.getIdLanca(), zamjena.getRedniBroj());
            if (ponuda != null) {
                ponude.add(new ZamjenaResponse(ponuda, ponuda.getIdLanca().equals(oglas.getStudent().getPotvrdjeniLanac())));
            }
        }
        return ponude;
    }

    public List<Long> getPotvrdjeniLanci() {
        List<Long> lanci = moguceZamjene.findAllIdLanca();
        List<Long> potvrdjeniLanci = new ArrayList<>();
        for (Long lanac : lanci)
            if (getBrojPotvrda(lanac) == getLanacSize(lanac))
                potvrdjeniLanci.add(lanac);
        return potvrdjeniLanci;
    }

    public void brisiLanac(Long idLanca) {
        List<MogucaZamjena> lanac = moguceZamjene.findByIdLanca(idLanca);
        for (MogucaZamjena zamjena : lanac) {
            Optional<Student> temp = studenti.findById(zamjena.getOglas().getStudent().getId());
            if (temp.isPresent()) {
                Student student = temp.get();
                student.setPotvrdjeniLanac(null);
                studenti.save(student);
            }
        }
        moguceZamjene.deleteByIdLanca(idLanca);
    }

    public int provjeriSveLance() {
        int brojIzbrisanihLanaca = 0;
        List<Long> lanci = moguceZamjene.findAllIdLanca();
        for (Long lanac : lanci) {
            List<MogucaZamjena> zamjene = moguceZamjene.findByIdLanca(lanac);
            boolean valid = true;
            for (MogucaZamjena zamjena : zamjene) {
                if (zamjena.getRedniBroj() > zamjene.size() || !zamjena.getOglas().isAktivan()) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                brisiLanac(lanac);
                brojIzbrisanihLanaca++;
            }
        }
        return brojIzbrisanihLanaca;
    }

    public List<Long> generirajNoveLance() {
        List<Long> noviLanci =  new ArrayList<>();

        for (Oglas oglas1 : oglasi.findAll())
            for (Oglas oglas2 : oglasi.findAll()) {
                if (!oglas1.isAktivan() || !oglas2.isAktivan() || oglas1.getIdOglas().equals(oglas2.getIdOglas()) || getPonudjeniOglasi(oglas1).contains(oglas2.getIdOglas()) || getPonudjeniOglasi(oglas2).contains(oglas1.getIdOglas()))
                    continue;

                boolean lajkani = false;
                for (Oglas lajkaniOglas : oglasi.findByLajkoviStudent(oglas1.getStudent())) {
                    if (lajkaniOglas.getIdOglas().equals(oglas2.getIdOglas()) &&
                            lajkaniOglasi.findByIdLajk(new LajkaniOglasKey(oglas1.getStudent().getId(), oglas2.getIdOglas())).get().getStupanjLajkanja() < 4) {
                        lajkani = true;
                        break;
                    }
                }
                if (!lajkani)
                    continue;

                lajkani = false;
                for (Oglas lajkaniOglas : oglasi.findByLajkoviStudent(oglas2.getStudent())) {
                    if (lajkaniOglas.getIdOglas().equals(oglas1.getIdOglas())) {
                        lajkani = true;
                        break;
                    }
                }
                if (!lajkani)
                    continue;

                long idLanca = noviIdLanca();
                moguceZamjene.save(new MogucaZamjena(idLanca, (short)1, oglas1));
                moguceZamjene.save(new MogucaZamjena(idLanca, (short)2, oglas2));
                noviLanci.add(idLanca);
                
                Student student1 = oglas1.getStudent();
                Student student2 = oglas2.getStudent();
        		EmailServiceImpl emailService = new EmailServiceImpl();
        		String emailText = "Poštovani, \n\nImate nove dostupne zamjene! Prijavite se u aplikaciju i provjerite nove obavijesti!";
        		emailService.sendEmail(student1.getEmail(), "Obavijest", emailText);
        		emailService.sendEmail(student2.getEmail(), "Obavijest", emailText);
            }

        return noviLanci;
    }

}
