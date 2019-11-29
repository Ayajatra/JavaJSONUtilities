import org.json.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Latihan6 {
    public static void main(String[] args) {
        List<User> users = IntStream
            .range(0, 5)
            .mapToObj(x -> new User(x, "Name" + String.valueOf(x)))
            .collect(Collectors.toList());

        List<Post> posts = IntStream
            .range(0, 5)
            .mapToObj(x -> new Post(x, "Title" + String.valueOf(x), "Body" + String.valueOf(x), x, users))
            .collect(Collectors.toList());

        JSONArray jsonArray = JSONUtils.toJSONArray(posts);

        List<Post> _posts = JSONUtils.fromJSONArray(jsonArray, Post.class);
        int test = 1;
    }
}
