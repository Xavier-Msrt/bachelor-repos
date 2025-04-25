package server;

import domaine.Query;
import domaine.QueryFactory;


import java.util.Scanner;

public class ProxyServer {

    private QueryFactory QueryFactory;

    public ProxyServer(QueryFactory QueryFactory) {
        this.QueryFactory = QueryFactory;
    }

    public void startServer(){
        Scanner scanner = new Scanner(System.in);
        String url;
        try (scanner){
            while (true){

                System.out.print("Entrez l'adresse du site: ");
                url = scanner.nextLine();
                Query query = QueryFactory.getQuery();
                query.setUrl(url);
                query.setQueryMethod(Query.QueryMethod.GET);
                QueryHandler queryHandler = new QueryHandler(query);

                queryHandler.start();
            }
        }finally {
            scanner.close();
        }


    }
}
