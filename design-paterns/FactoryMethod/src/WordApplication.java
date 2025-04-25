public class WordApplication extends Application{
    @Override
    public Document createDocument() {
        return new DocumentWord();
    }
}
