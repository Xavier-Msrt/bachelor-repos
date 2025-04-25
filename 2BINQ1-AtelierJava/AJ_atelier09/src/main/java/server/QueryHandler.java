package server;

import domaine.Query;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

public class QueryHandler extends Thread{
    private Query query;

    public QueryHandler(Query query) {
        this.query = query;
    }

    @Override
    public void run() {

        if(query.getQueryMethod() == Query.QueryMethod.GET){

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                ClassicHttpRequest httpRequest = ClassicRequestBuilder.get(query.getUrl()).build();

                Thread.sleep(10000);

                httpClient.execute(httpRequest, response -> {
                    System.out.println(response.getCode() + ": " + response.getReasonPhrase());

                    System.out.println(EntityUtils.toString(response.getEntity()));
                    return null;
                });

            } catch (Exception e) {
            }
        }
    }




}
