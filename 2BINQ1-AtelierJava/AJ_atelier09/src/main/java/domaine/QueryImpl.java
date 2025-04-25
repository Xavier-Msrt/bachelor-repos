package domaine;

class QueryImpl implements  Query {

    private String url;
    private QueryMethod  queryMethod;


    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setQueryMethod(QueryMethod queryMethod) {
        this.queryMethod = queryMethod;
    }

}
