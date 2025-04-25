public class Player {
    private String name;
    private int year_old;

    public Player(String name, int year_old) {
        this.name = name;
        this.year_old = year_old;
    }

    public String getName() {
        return name;
    }

    public int getYear_old() {
        return year_old;
    }

    public static class Builder {
        private String name;
        private int year;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Player build(){
            return new Player(name, year);
        }
    }
}
