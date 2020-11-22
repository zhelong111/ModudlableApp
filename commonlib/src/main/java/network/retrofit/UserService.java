package network.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("")
    Call<User> getUser();
}
