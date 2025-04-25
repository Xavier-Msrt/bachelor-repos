//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CleRonde cleRonde = new CleRonde();

        // vise carrer
        AdapateurRondeCarrer adapateurRondeCarrer= new AdapateurRondeCarrer(cleRonde);
        adapateurRondeCarrer.tourneCleCarrer();


    }
}