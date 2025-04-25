public class AdapateurRondeCarrer implements CleCarrer{
    private CleRonde ronde;

    public AdapateurRondeCarrer(CleRonde ronde) {
        this.ronde = ronde;
    }

    @Override
    public void tourneCleCarrer() {
        ronde.tourneCleRonde();
    }
}
