package deadlock_magic;

import java.util.HashMap;

public class Grimoire {



    public enum Incantation {
        FLAMBOIEMENT_ARDENT,
        DANSE_DES_COMETES,
        ECLAT_DU_PHENIX,
        RESONNANCE_DES_SAGES,
        MURMURES_GIVRES,
        VORTEX_ANCESTRAL
    }

    public enum Ceremonie {
        SYMPOSIUM_DES_ILLUSIONS_PRISMATIQUES(Incantation.FLAMBOIEMENT_ARDENT, Incantation.ECLAT_DU_PHENIX, Incantation.MURMURES_GIVRES),
        ASSEMBLEE_DES_ONDES_FRISSONNATES(Incantation.MURMURES_GIVRES, Incantation.VORTEX_ANCESTRAL, Incantation.FLAMBOIEMENT_ARDENT),
        RITUEL_DU_CREPUSCAL_ABYSSAL(Incantation.RESONNANCE_DES_SAGES, Incantation.MURMURES_GIVRES, Incantation.FLAMBOIEMENT_ARDENT);
        final Incantation incantation1;
        final Incantation incantation2;
        final Incantation incantation3;

        Ceremonie(Incantation incantation1, Incantation incantation2, Incantation incantation3) {
            int inc1 = incantation1.ordinal();
            int inc2 = incantation2.ordinal();
            int inc3 = incantation3.ordinal();


            Incantation locInc1 = incantation1, locInc2 = incantation2, locInc3 = incantation3;
            if (inc1 < inc2){
                if (inc1 < inc3){
                    locInc1 = incantation1;
                    inc1 = Integer.MAX_VALUE;

                }else{
                    locInc1 = incantation3;
                    inc3 = Integer.MAX_VALUE;
                }
            } else if (inc2 < inc3) {
                locInc1 = incantation2;
                inc2 = Integer.MAX_VALUE;
            }

            if (inc1 < inc2){
                if (inc1 < inc3){
                    locInc2 = incantation1;
                    inc1 = Integer.MAX_VALUE;
                }else{
                    locInc2 = incantation3;
                    inc3 = Integer.MAX_VALUE;
                }
            } else if (inc2 < inc3) {
                locInc2 = incantation2;
                inc2 = Integer.MAX_VALUE;
            }

            if (inc1 <= inc2){
                if (inc1 < inc3){
                    locInc3 = incantation1;
                }else{
                    locInc3 = incantation3;
                }
            } else if (inc2 < inc3) {
                locInc3 = incantation2;
            }
            this.incantation1 = locInc1;
            this.incantation2 = locInc2;
            this.incantation3 = locInc3;

        }

        public void debuter(){
            System.out.println(name() + " débute");
            synchronized (incantation1){
                System.out.println(incantation1 + " pris pour " + name());
                mediter();
                synchronized (incantation2){
                    System.out.println(incantation2 + " pris pour " + name());
                    mediter();
                    synchronized (incantation3){
                        System.out.println(incantation3 + " pris pour " + name());
                        mediter();
                        System.out.println(name() + " lancé !");
                    }
                }
            }
        }

        public void mediter(){
            System.out.println("Méditation");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
