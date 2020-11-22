package network.retrofit;

public class RS<T> {
    public T create(Class<T> clazz) {
        return RetrofitManager.getInstance().create(clazz);
    }
}
