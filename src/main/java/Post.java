import java.util.List;

public class Post {
    public int ID;
    public String Title;
    public String Body;
    public int UserID;
    public List<User> Users;

    public Post() {

    }

    public Post(int ID, String title, String body, int userID, List<User> users) {
        this.ID = ID;
        Title = title;
        Body = body;
        UserID = userID;
        Users = users;
    }
}
