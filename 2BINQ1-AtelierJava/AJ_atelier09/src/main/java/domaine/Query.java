package domaine;

public interface Query {
    String getUrl();

    QueryMethod getQueryMethod();

    void setUrl(String url);

    void setQueryMethod(QueryMethod queryMethod);

    public enum QueryMethod {
        GET, POST
    }
}
