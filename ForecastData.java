import java.util.ArrayList;

public class ForecastData {
    private int cod;
    private String message;
    private int cnt;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public ArrayList<List> getList() {
        return list;
    }

    public void setList(ArrayList<List> list) {
        this.list = list;
    }

    class List{
        long dt;

        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public void setWeather(ArrayList<Weather> weather) {
            this.weather = weather;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public int getVisibility() {
            return visibility;
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public double getPop() {
            return pop;
        }

        public void setPop(double pop) {
            this.pop = pop;
        }

        public Rain getRain() {
            return rain;
        }

        public void setRain(Rain rain) {
            this.rain = rain;
        }

        public Sys getSys() {
            return sys;
        }

        public void setSys(Sys sys) {
            this.sys = sys;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        class Main {
            private double temp;
            private double feels_like;
            private double temp_min;
            private double temp_max;
            private int pressure;
            private int sea_level;
            private int grnd_level;
            private int humidity;
            private double temp_kf;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public double getFeels_like() {
                return feels_like;
            }

            public void setFeels_like(double feels_like) {
                this.feels_like = feels_like;
            }

            public double getTemp_min() {
                return temp_min;
            }

            public void setTemp_min(double temp_min) {
                this.temp_min = temp_min;
            }

            public double getTemp_max() {
                return temp_max;
            }

            public void setTemp_max(double temp_max) {
                this.temp_max = temp_max;
            }

            public int getPressure() {
                return pressure;
            }

            public void setPressure(int pressure) {
                this.pressure = pressure;
            }

            public int getSea_level() {
                return sea_level;
            }

            public void setSea_level(int sea_level) {
                this.sea_level = sea_level;
            }

            public int getGrnd_level() {
                return grnd_level;
            }

            public void setGrnd_level(int grnd_level) {
                this.grnd_level = grnd_level;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }

            public double getTemp_kf() {
                return temp_kf;
            }

            public void setTemp_kf(double temp_kf) {
                this.temp_kf = temp_kf;
            }
        }
        private Main main;
        class Weather {
            private int id;
            private String main;
            private String description;
            private String icon;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
        private ArrayList<Weather> weather;
        class Clouds {
            private int all;

            public int getAll() {
                return all;
            }

            public void setAll(int all) {
                this.all = all;
            }
        }
        private Clouds clouds;
        class Wind {
            private double speed;
            private int deg;
            private double gust;

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }

            public int getDeg() {
                return deg;
            }

            public void setDeg(int deg) {
                this.deg = deg;
            }

            public double getGust() {
                return gust;
            }

            public void setGust(double gust) {
                this.gust = gust;
            }
        }
        private Wind wind;
        private int visibility;
        private double pop;
        class Rain {
            private double h3;

            public double getH3() {
                return h3;
            }

            public void setH3(double h3) {
                this.h3 = h3;
            }
        }
        private Rain rain;
        class Sys {
            private String pod;

            public String getPod() {
                return pod;
            }

            public void setPod(String pod) {
                this.pod = pod;
            }
        }
        private Sys sys;
        private String dt_txt;
    }
    private ArrayList<List> list;
}
