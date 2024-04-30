package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.myapplication.model.Comment;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và phiên bản
    private static final String DATABASE_NAME = "my_social_app.db";
    private static final int DATABASE_VERSION = 1;

    // Tên các bảng và cột trong cơ sở dữ liệu
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_USERACCNAME = "useraccname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String AVATAR_IMAGE = "avatar_image";
    private static final String COVER_IMAGE = "cover_image";
    private static final String COLUMN_IS_ADMIN = "is_admin";

    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_POST_GROUP= "post_group";
    private static final String COLUMN_POST_TITLE = "title";
    private static final String COLUMN_POST_CONTENT = "content";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String IS_RECIPE = "is_recipe";

    private static final String TABLE_PHOTOS = "photos";
    private static final String COLUMN_PHOTO_ID = "photo_id";
    private static final String COLUMN_IMAGE_URL = "image_url";

    private static final String TABLE_LIKES = "likes";
    private static final String COLUMN_LIKE_ID = "like_id";

    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_COMMENT_CONTENT = "comment_content";

    // Câu truy vấn tạo bảng người dùng
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_USERACCNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + AVATAR_IMAGE + " TEXT,"
            + COVER_IMAGE + " TEXT,"
            + COLUMN_IS_ADMIN + " INTEGER DEFAULT 0"
            + ")";

    // Câu truy vấn tạo bảng bài viết
    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + "("
            + COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_POST_GROUP + " INTEGER,"
            + IS_RECIPE + " INTEGER, "
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_POST_TITLE + " TEXT,"
            + COLUMN_POST_CONTENT + " TEXT,"
            + COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    // Câu truy vấn tạo bảng ảnh
    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE " + TABLE_PHOTOS + "("
            + COLUMN_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_POST_ID + " INTEGER,"
            + COLUMN_IMAGE_URL + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + COLUMN_POST_ID + ")"
            + ")";

    // Câu truy vấn tạo bảng lượt thích
    private static final String CREATE_TABLE_LIKES = "CREATE TABLE " + TABLE_LIKES + "("
            + COLUMN_LIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_POST_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
            + "FOREIGN KEY(" + COLUMN_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + COLUMN_POST_ID + ")"
            + ")";

    // Câu truy vấn tạo bảng bình luận
    private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + "("
            + COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_POST_ID + " INTEGER,"
            + COLUMN_COMMENT_CONTENT + " TEXT,"
            + COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
            + "FOREIGN KEY(" + COLUMN_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + COLUMN_POST_ID + ")"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo các bảng khi cơ sở dữ liệu được tạo lần đầu tiên
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_PHOTOS);
        db.execSQL(CREATE_TABLE_LIKES);
        db.execSQL(CREATE_TABLE_COMMENTS);

        // Thêm dữ liệu mẫu vào bảng người dùng
        db.execSQL("INSERT INTO " + TABLE_USERS + "(" + COLUMN_USERNAME + ", " + COLUMN_USERACCNAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + AVATAR_IMAGE + ", " + COVER_IMAGE + ", " + COLUMN_IS_ADMIN + ") VALUES ('chinhuy','user1', 'user1@example.com', 'password1', 'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054048/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567564813495.jpg', 'https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg', 0)");
        db.execSQL("INSERT INTO " + TABLE_USERS + "(" + COLUMN_USERNAME + ", " + COLUMN_USERACCNAME + ",  " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + AVATAR_IMAGE + ", " + COVER_IMAGE + ", " + COLUMN_IS_ADMIN + ") VALUES ('yongjun','user2', 'user2@example.com', 'password2', 'https://images.fpt.shop/unsafe/filters:quality(5)/fptshop.com.vn/uploads/images/tin-tuc/175607/Originals/avt-cho-cute%20(39).jpg', 'https://images.pexels.com/photos/531880/pexels-photo-531880.jpeg?cs=srgb&dl=pexels-pixabay-531880.jpg&fm=jpg', 0)");
        db.execSQL("INSERT INTO " + TABLE_USERS + "(" + COLUMN_USERNAME + ", " + COLUMN_USERACCNAME + ",  " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + AVATAR_IMAGE + ", " + COVER_IMAGE + ", " + COLUMN_IS_ADMIN + ") VALUES ('sdfsd','as', 'sdfsdf@example.com', 'as', '', '', 0)");
        // Thêm dữ liệu mẫu vào bảng bài viết
        db.execSQL("INSERT INTO " + TABLE_POSTS + "(" + COLUMN_POST_GROUP + ", " + COLUMN_USER_ID + ", " + IS_RECIPE + ", " + COLUMN_POST_TITLE +", " + COLUMN_POST_CONTENT + ") VALUES (1, 1, 0, '1user1의 게시물 1 내용','ấdasdsadas')");
        db.execSQL("INSERT INTO " + TABLE_POSTS + "(" + COLUMN_POST_GROUP + ", " + COLUMN_USER_ID + ", " + IS_RECIPE + ", " + COLUMN_POST_TITLE +", " + COLUMN_POST_CONTENT + ") VALUES (2, 1, 1,'my spaghetti very delicious', '2user1의 게시물 2 내용ádlajsdjaspdjpaspajpoadposaposadsaasdokaso kádosa sakdpokspakdkj jdiasjdpsa[dskaokd sjdpas')");
        db.execSQL("INSERT INTO " + TABLE_POSTS + "(" + COLUMN_POST_GROUP + ", " + COLUMN_USER_ID + ", " + IS_RECIPE + ", " + COLUMN_POST_TITLE +", " + COLUMN_POST_CONTENT + ") VALUES (3, 2, 0, 'ádasdas','3user2의 게시물 1 내용.\n"+"\n"+"ㅁㄴㅇㅁㄴㅇㅁ')");
        db.execSQL("INSERT INTO " + TABLE_POSTS + "(" + COLUMN_POST_GROUP + ", " + COLUMN_USER_ID + ", " + IS_RECIPE + ", " + COLUMN_POST_TITLE +", " + COLUMN_POST_CONTENT + ") VALUES (4, 2, 1, '볶음밥을 만들었어요','먼저, 끈끈이콩의 껍질을 벗기고 끝부분을 잘라내고 씻어서 입방체로 잘라야 합니다. 그런 다음 소시지를 씻고 바깥층을 벗겨낸 다음 입방체 또는 둥근 조각으로 자릅니다. 다음으로 당근 껍질을 벗기고 씻어서 입방체로 자릅니다.\n" +
                "\n" +
                "2단계 계란을 풀어주세요\n" +
                "계란 6개를 그릇에 담아 깨뜨려주세요. 다음으로 후추 1티스푼과 액젓 1티스푼을 넣고 잘 섞습니다.\n" +
                "\n" +
                "3단계 재료를 볶는다\n" +
                "먼저 다진 마늘을 향이 날 때까지 볶은 후 당근, 소시지, 잘게 썬 실콩 등 재료를 넣고 모든 재료가 익을 때까지 3분간 잘 저어줍니다.\n" +
                "\n" +
                "4단계 밥을 볶는다\n" +
                "먼저 밀가루를 이용해 밥을 푼 뒤 풀어놓은 계란을 넣고 후추 1티스푼, 간장 3티스푼, MSG 2티스푼을 사용하세요. 다음으로 3번에서 볶은 재료를 넣어주세요. 그런 다음 모래나 모래를 사용하여 쌀알이 분리될 때까지 중간 불로 6분간 저어줍니다. 맛을 본 다음 스토브를 꺼야합니다.\n" +
                "\n" +
                "좋은 팁: 혼합 볶음밥을 만들 때는 마른 쌀이나 차가운 쌀을 사용해야 하며, 뜨거운 쌀보다 더 맛있고, 밥이 풀어지길 원할 경우 취사 전 냉장고에 3시간 정도 넣어두세요.')");

        // Thêm dữ liệu mẫu vào bảng ảnh
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (1, 'https://static.hubzum.zumst.com/hubzum/2022/02/10/14/3ea39a0f140c44b8b26761a93d11aa18.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (2, 'https://forza.com.vn/wp-content/uploads/2021/07/cach-lam-mi-y-thom-ngon-chuan-vi-tai-nha-6.jpeg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (2, 'https://static.hubzum.zumst.com/hubzum/2022/02/10/14/3ea39a0f140c44b8b26761a93d11aa18.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (2, 'https://toplist.vn/images/800px/cu-cai-trang-230181.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (3, 'https://toplist.vn/images/800px/cu-cai-trang-230181.jpg')");

        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (4, 'https://img.choroc.com/newshop/goods/026709/026709_1.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (4, 'https://cdn.tgdd.vn/Files/2021/08/03/1372738/cach-lam-com-rang-thap-cam-vua-dep-mat-lai-ngon-mieng-202108031450180412.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (4, 'https://cdn.tgdd.vn/Files/2021/08/03/1372738/cach-lam-com-rang-thap-cam-vua-dep-mat-lai-ngon-mieng-202108031450342027.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PHOTOS + "(" + COLUMN_POST_ID + ", " + COLUMN_IMAGE_URL + ") VALUES (4, 'https://cdn.tgdd.vn/Files/2021/08/03/1372738/cach-lam-com-rang-thap-cam-vua-dep-mat-lai-ngon-mieng-202108031451090050.jpg')");

        db.execSQL("INSERT INTO " + TABLE_COMMENTS + "(" + COLUMN_USER_ID + ", " + COLUMN_POST_ID + ", " + COLUMN_COMMENT_CONTENT + ") VALUES (1, 4, '와우, 포스트 정말 흥미로워요! 정말 멋진 일이에요! 😊')");
        db.execSQL("INSERT INTO " + TABLE_COMMENTS + "(" + COLUMN_USER_ID + ", " + COLUMN_POST_ID + ", " + COLUMN_COMMENT_CONTENT + ") VALUES (2, 4, '맛있는 이탈리안 파스타! 여기서 만드는 법을 다른 분들과 공유해주시겠어요? 🍝')");
        db.execSQL("INSERT INTO " + TABLE_COMMENTS + "(" + COLUMN_USER_ID + ", " + COLUMN_POST_ID + ", " + COLUMN_COMMENT_CONTENT + ") VALUES (1, 4, '정말 아름다운 이미지네요! 😍')");
        db.execSQL("INSERT INTO " + TABLE_COMMENTS + "(" + COLUMN_USER_ID + ", " + COLUMN_POST_ID + ", " + COLUMN_COMMENT_CONTENT + ") VALUES (2, 4, '이 요리 정말 맛있어 보여요! 지금 만들어 먹어야겠어요! 공유해주셔서 감사합니다! 👍')");
        db.execSQL("INSERT INTO " + TABLE_COMMENTS + "(" + COLUMN_USER_ID + ", " + COLUMN_POST_ID + ", " + COLUMN_COMMENT_CONTENT + ") VALUES (1, 4, '훌륭해요! 설명이 매우 자세하고 이해하기 쉬워요. 지금 만들어 먹어 볼게요! 🥢')");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa và tạo lại các bảng khi có sự thay đổi phiên bản cơ sở dữ liệu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        // Thêm các bảng cần thiết khác nếu cần
        onCreate(db);
    }
    //add user
    public void insertUser(String username, String userAccName, String email, String password, String avatarImage, String coverImage, int isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_USERACCNAME, userAccName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(AVATAR_IMAGE, avatarImage);
        values.put(COVER_IMAGE, coverImage);
        values.put(COLUMN_IS_ADMIN, isAdmin);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    //kiểm tra trùng acc id
    public boolean isUsernameExists(String userAccname) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERACCNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userAccname});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Method to check if a username and password match in the database
    //check login
    public boolean checkUser(String useraccname, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERACCNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {useraccname, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
    //add post
    public long insertPost(int userId, int postGroup, int isRecipe,  String title, String content, List<String> imageUrls) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_POST_GROUP, postGroup);
        values.put(IS_RECIPE, isRecipe);
        values.put(COLUMN_POST_TITLE, title);
        values.put(COLUMN_POST_CONTENT, content);
        long postId = db.insert(TABLE_POSTS, null, values);
        if (postId != -1) {
            for (String imageUrl : imageUrls) {
                ContentValues photoValues = new ContentValues();
                photoValues.put(COLUMN_POST_ID, postId);
                photoValues.put(COLUMN_IMAGE_URL, imageUrl);
                db.insert(TABLE_PHOTOS, null, photoValues);
            }
        }
        db.close();
        return postId;
    }

    //delete post
    public void deletePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Xóa bài viết từ bảng "posts" với postId chỉ định
        db.delete(TABLE_POSTS, COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
        // Xóa các ảnh của bài viết từ bảng "photos" tương ứng với postId chỉ định
        db.delete(TABLE_PHOTOS, COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
        db.close();
    }

    public long addComment(int userId, int postId, String commentContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_POST_ID, postId);
        values.put(COLUMN_COMMENT_CONTENT, commentContent);
        // Thêm dòng mới vào bảng comments
        long newRowId = db.insert(TABLE_COMMENTS, null, values);
        db.close();
        return newRowId;
    }
    public List<Comment> getCommentsForPost(int postId) {
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                TABLE_USERS + "." + AVATAR_IMAGE + ", " +
                TABLE_COMMENTS + ".*, " +
                TABLE_USERS + "." + COLUMN_USERNAME + " " +
                "FROM " + TABLE_COMMENTS + " " +
                "JOIN " + TABLE_USERS + " ON " +
                TABLE_COMMENTS + "." + COLUMN_USER_ID + " = " +
                TABLE_USERS + "." + COLUMN_USER_ID + " " +
                "WHERE " + COLUMN_POST_ID + " = ?"+
                "ORDER BY " + COLUMN_CREATED_AT + " DESC", new String[]{String.valueOf(postId)});

        try {
            if (cursor.moveToFirst()) {
                do {
                    String commentAvatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String commentUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String commentContent = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_CONTENT));

                    Comment comment = new Comment(commentAvatarUrl, commentUsername, commentContent);
                    comments.add(comment);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return comments;
    }
    public int getCommentCountForPost(int postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int commentCount = 0;

        String query = "SELECT COUNT(*) FROM " + TABLE_COMMENTS +
                " WHERE " + COLUMN_POST_ID + " = " + postId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            commentCount = cursor.getInt(0);
            cursor.close();
        }

        return commentCount;
    }
    public int getLikeCountForPost(int postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int likeCount = 0;

        String query = "SELECT COUNT(*) FROM " + TABLE_LIKES +
                " WHERE " + COLUMN_POST_ID + " = " + postId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            likeCount = cursor.getInt(0);
            cursor.close();
        }

        return likeCount;
    }
//    public void likePost(int postId, int userId,) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_POST_ID, postId);
//        values.put(COLUMN_USER_ID, userId);
//        // Thêm dữ liệu vào bảng LIKES
//        db.insert(TABLE_LIKES, null, values);
//        db.close();
//    }
//
//    // Phương thức để xóa like bài viết khỏi cơ sở dữ liệu
//    public void unlikePost(int postId, int userId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        // Xóa dữ liệu từ bảng LIKES dựa trên postId và userId
//        db.delete(TABLE_LIKES, COLUMN_POST_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
//                new String[]{String.valueOf(postId), String.valueOf(userId)});
//        db.close();
//    }
    public void toggleLike(int userId, int postId, boolean isLiked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_POST_ID, postId);

        if (isLiked) {
            // Nếu đang like, thêm bản ghi vào bảng LIKES
            db.insert(TABLE_LIKES, null, values);
        } else {
            // Nếu đang unlike, xóa bản ghi từ bảng LIKES
            db.delete(TABLE_LIKES, COLUMN_USER_ID + " = ? AND " + COLUMN_POST_ID + " = ?",
                    new String[]{String.valueOf(userId), String.valueOf(postId)});
        }
        db.close();
    }

    public boolean isPostLikedByUser(int postId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LIKES + " WHERE " + COLUMN_POST_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(postId), String.valueOf(userId)});
        boolean isLiked = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isLiked;
    }

    public List<Integer> getLikedPostsByUserId(int userId) {
        List<Integer> likedPostIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_POST_ID +
                " FROM " + TABLE_LIKES +
                " WHERE " + COLUMN_USER_ID + " = " + userId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                likedPostIds.add(postId);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return likedPostIds;
    }


    public User getUserByUseraccname(String useraccname) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERACCNAME + "=?", new String[]{useraccname}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String avatarImage = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
            String coverImage = cursor.getString(cursor.getColumnIndexOrThrow(COVER_IMAGE));
            int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN));
            // Khởi tạo đối tượng User từ dữ liệu lấy từ cơ sở dữ liệu
            user = new User(userId, username, useraccname, email, avatarImage, coverImage, isAdmin);
            cursor.close();
        }
        db.close();
        return user;
    }

    public List<Post> searchPost(String query) {
        List<Post> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn SQL để tìm kiếm bài viết dựa trên tiêu đề, nội dung và tên người dùng
        String sqlQuery = "SELECT " + TABLE_USERS + "." + AVATAR_IMAGE + ", " + TABLE_POSTS + ".*, " + TABLE_USERS + "." + COLUMN_USERNAME + ", " + TABLE_POSTS + "." + COLUMN_CREATED_AT + ", " + TABLE_POSTS + "." + COLUMN_POST_TITLE + "," + COLUMN_POST_CONTENT + " FROM " + TABLE_POSTS + " JOIN " + TABLE_USERS + " ON " + TABLE_POSTS + "." + COLUMN_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USER_ID +
                " WHERE " + COLUMN_POST_TITLE + " LIKE ? OR " +
                COLUMN_POST_CONTENT + " LIKE ? OR " +
                TABLE_USERS + "." + COLUMN_USERNAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};
        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy các thông tin cơ bản của bài đăng
                    String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_CONTENT));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                    // Lấy ID của bài đăng
                    int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                    int isRecipe = cursor.getInt(cursor.getColumnIndexOrThrow(IS_RECIPE));

                    List<String> imageUrls = null;
                    Post post = new Post(userId, postId, avatarUrl, username, date, title, content, imageUrls, isRecipe,new ArrayList<>());
                    // Tạo một danh sách để lưu trữ các URL hình ảnh cho bài đăng hiện tại
                    imageUrls = new ArrayList<>();

                    // Truy vấn để lấy tất cả các URL hình ảnh từ bảng PHOTOS tương ứng với postId
                    Cursor photoCursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (photoCursor.moveToNext()) {
                        String imageUrl = photoCursor.getString(photoCursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                        // Thêm URL hình ảnh vào danh sách
                        imageUrls.add(imageUrl);
                    }
                    photoCursor.close();

                    // Gán danh sách URL hình ảnh cho bài đăng
                    post.setImageUrls(imageUrls);

                    // Thêm đối tượng Post vào danh sách
                    searchResults.add(post);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return searchResults;
    }
    public List<Post> getPostsByGroupIdAndSearchText(int groupId, String searchText) {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Xây dựng câu truy vấn dựa trên groupId và từ được tìm kiếm
        String query = "SELECT " + TABLE_USERS + "." + AVATAR_IMAGE + ", " + TABLE_POSTS + ".*, " + TABLE_USERS + "." + COLUMN_USERNAME + ", " + TABLE_POSTS + "." + COLUMN_CREATED_AT + ", " + TABLE_POSTS + "." + COLUMN_POST_TITLE + "," + COLUMN_POST_CONTENT + " FROM " + TABLE_POSTS + " JOIN " + TABLE_USERS + " ON " + TABLE_POSTS + "." + COLUMN_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USER_ID;

        if (groupId != 0) {
            // Nếu groupId không phải là 0, thêm điều kiện lọc theo groupId vào câu truy vấn
            query += " WHERE " + TABLE_POSTS + "." + COLUMN_POST_GROUP + " = " + groupId;
        }

        // Thêm điều kiện lọc theo từ được tìm kiếm vào câu truy vấn
        if (!searchText.isEmpty()) {
            // Nếu từ được tìm kiếm không trống, thêm điều kiện lọc theo từ vào câu truy vấn
            if (groupId == 0) {
                // Nếu groupId là 0 (mục "All"), sử dụng điều kiện OR để lọc theo từ được tìm kiếm trong tiêu đề và nội dung bài đăng
                query += " WHERE " + COLUMN_POST_TITLE + " LIKE '%" + searchText + "%' OR " + COLUMN_POST_CONTENT + " LIKE '%" + searchText + "%'";
            } else {
                // Nếu groupId khác 0, sử dụng điều kiện AND để lọc theo từ được tìm kiếm trong tiêu đề và nội dung bài đăng và groupId
                query += " AND (" + COLUMN_POST_TITLE + " LIKE '%" + searchText + "%' OR " + COLUMN_POST_CONTENT + " LIKE '%" + searchText + "%')";
            }
        }

        // Sắp xếp kết quả theo thời gian tạo bài đăng (created_at) theo thứ tự giảm dần
        query += " ORDER BY " + TABLE_POSTS + "." + COLUMN_CREATED_AT + " DESC";

        // Thực hiện câu truy vấn
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy các thông tin cơ bản của bài đăng
                    String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_CONTENT));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                    // Lấy ID của bài đăng
                    int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                    int isRecipe = cursor.getInt(cursor.getColumnIndexOrThrow(IS_RECIPE));

                    List<String> imageUrls = null;
                    Post post = new Post(userId,postId, avatarUrl, username, date, title, content, imageUrls, isRecipe,new ArrayList<>());
                    // Tạo một danh sách để lưu trữ các URL hình ảnh cho bài đăng hiện tại
                    imageUrls = new ArrayList<>();

                    // Truy vấn để lấy tất cả các URL hình ảnh từ bảng PHOTOS tương ứng với postId
                    Cursor photoCursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (photoCursor.moveToNext()) {
                        String imageUrl = photoCursor.getString(photoCursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                        // Thêm URL hình ảnh vào danh sách
                        imageUrls.add(imageUrl);
                    }
                    photoCursor.close();

                    // Gán danh sách URL hình ảnh cho bài đăng
                    post.setImageUrls(imageUrls);

                    // Thêm đối tượng Post vào danh sách
                    posts.add(post);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return posts;
    }

    // Method to get all posts by post_group
    public List<Post> getPostsByGroupId(int groupId) {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TABLE_USERS + "." + AVATAR_IMAGE + ", " + TABLE_POSTS + ".*, " + TABLE_USERS + "." + COLUMN_USERNAME + ", " + TABLE_POSTS + "." + COLUMN_CREATED_AT + ", " + TABLE_POSTS + "." + COLUMN_POST_TITLE + "," + COLUMN_POST_CONTENT + " FROM " + TABLE_POSTS + " JOIN " + TABLE_USERS + " ON " + TABLE_POSTS + "." + COLUMN_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USER_ID + " WHERE " + TABLE_POSTS + "." + COLUMN_POST_GROUP + " = ?" + " ORDER BY " + TABLE_POSTS + "." + COLUMN_CREATED_AT + " DESC", new String[]{String.valueOf(groupId)});

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy các thông tin cơ bản của bài đăng
                    String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_CONTENT));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                    // Lấy ID của bài đăng
                    int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                    int isRecipe = cursor.getInt(cursor.getColumnIndexOrThrow(IS_RECIPE));

                    List<String> imageUrls = null;
                    Post post = new Post(userId,postId, avatarUrl, username, date, title, content, imageUrls, isRecipe,new ArrayList<>());
                    // Tạo một danh sách để lưu trữ các URL hình ảnh cho bài đăng hiện tại
                    imageUrls = new ArrayList<>();

                    // Truy vấn để lấy tất cả các URL hình ảnh từ bảng PHOTOS tương ứng với postId
                    Cursor photoCursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (photoCursor.moveToNext()) {
                        String imageUrl = photoCursor.getString(photoCursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                        // Thêm URL hình ảnh vào danh sách
                        imageUrls.add(imageUrl);
                    }
                    photoCursor.close();

                    // Gán danh sách URL hình ảnh cho bài đăng
                    post.setImageUrls(imageUrls);

                    // Thêm đối tượng Post vào danh sách
                    posts.add(post);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return posts;
    }



    public List<Post> getPopularPosts() {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TABLE_USERS + "." + AVATAR_IMAGE + ", " + TABLE_POSTS + ".*, " + TABLE_USERS + "." + COLUMN_USERNAME + ", " + TABLE_POSTS + "." + COLUMN_CREATED_AT + ", " + TABLE_POSTS + "." + COLUMN_POST_TITLE + "," + COLUMN_POST_CONTENT + ", " +
                "COUNT(" + TABLE_LIKES + "." + COLUMN_USER_ID + ") AS likeCount, " + // Đếm số lượt thích cho mỗi bài viết
                "CASE WHEN " + TABLE_LIKES + "." + COLUMN_USER_ID + " IS NULL THEN 0 ELSE 1 END AS isLiked " + // Kiểm tra xem người dùng đã thích bài viết chưa
                "FROM " + TABLE_POSTS +
                " JOIN " + TABLE_USERS + " ON " + TABLE_POSTS + "." + COLUMN_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USER_ID +
                " LEFT JOIN " + TABLE_LIKES + " ON " + TABLE_POSTS + "." + COLUMN_POST_ID + " = " + TABLE_LIKES + "." + COLUMN_POST_ID +
                " WHERE " + TABLE_POSTS + "." + COLUMN_CREATED_AT + " >= date('now', '-30 days') " + // Chỉ lấy các bài viết được tạo trong vòng 30 ngày
                " GROUP BY " + TABLE_POSTS + "." + COLUMN_POST_ID + // Nhóm các bài viết theo ID để đếm số lượt thích
                " ORDER BY likeCount DESC " + // Sắp xếp kết quả theo số lượt thích giảm dần
                " LIMIT 5", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy các thông tin cơ bản của bài đăng
                    String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_CONTENT));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                    // Lấy ID của bài đăng
                    int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                    int isRecipe = cursor.getInt(cursor.getColumnIndexOrThrow(IS_RECIPE));
                    // Kiểm tra trạng thái like của bài viết
                    boolean isLiked = cursor.getInt(cursor.getColumnIndexOrThrow("isLiked")) == 1;

                    List<String> imageUrls = null;
                    Post post = new Post(userId, postId, avatarUrl, username, date, title, content, imageUrls, isRecipe, new ArrayList<>());
                    post.setLiked(isLiked); // Cập nhật trạng thái like của bài viết

                    // Tạo một danh sách để lưu trữ các URL hình ảnh cho bài đăng
                    imageUrls = new ArrayList<>();

                    // Truy vấn để lấy tất cả các URL hình ảnh từ bảng PHOTOS tương ứng với postId
                    Cursor photoCursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (photoCursor.moveToNext()) {
                        String imageUrl = photoCursor.getString(photoCursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                        // Thêm URL hình ảnh vào danh sách
                        imageUrls.add(imageUrl);
                    }
                    photoCursor.close();

                    // Gán danh sách URL hình ảnh cho bài đăng
                    post.setImageUrls(imageUrls);
                    // Lấy danh sách bình luận cho bài đăng
                    List<Comment> comments = new ArrayList<>();
                    Cursor commentCursor = db.rawQuery("SELECT " +
                            TABLE_USERS + "." + AVATAR_IMAGE + ", " +
                            TABLE_COMMENTS + ".*, " +
                            TABLE_USERS + "." + COLUMN_USERNAME + " " +
                            "FROM " + TABLE_COMMENTS + " " +
                            "JOIN " + TABLE_USERS + " ON " +
                            TABLE_COMMENTS + "." + COLUMN_USER_ID + " = " +
                            TABLE_USERS + "." + COLUMN_USER_ID + " " +
                            "WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (commentCursor.moveToNext()) {
                        String commentAvatarUrl = commentCursor.getString(commentCursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                        String commentUsername = commentCursor.getString(commentCursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                        String commentContent = commentCursor.getString(commentCursor.getColumnIndexOrThrow(COLUMN_COMMENT_CONTENT));

                        Comment comment = new Comment(commentAvatarUrl, commentUsername, commentContent);
                        comments.add(comment);
                    }
                    commentCursor.close();
                    // Gán danh sách bình luận cho bài đăng
                    post.setComments(comments);
                    // Thêm đối tượng Post vào danh sách
                    posts.add(post);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return posts;
    }


    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TABLE_USERS + "." + AVATAR_IMAGE + ", " + TABLE_POSTS + ".*, " + TABLE_USERS + "." + COLUMN_USERNAME + ", " + TABLE_POSTS + "." + COLUMN_CREATED_AT + ", " + TABLE_POSTS + "." + COLUMN_POST_TITLE + "," + COLUMN_POST_CONTENT + ", " +
                "CASE WHEN " + TABLE_LIKES + "." + COLUMN_USER_ID + " IS NULL THEN 0 ELSE 1 END AS isLiked " + // Kiểm tra xem người dùng đã thích bài viết chưa
                "FROM " + TABLE_POSTS +
                " JOIN " + TABLE_USERS + " ON " + TABLE_POSTS + "." + COLUMN_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USER_ID +
                " LEFT JOIN " + TABLE_LIKES + " ON " + TABLE_POSTS + "." + COLUMN_POST_ID + " = " + TABLE_LIKES + "." + COLUMN_POST_ID +
                " ORDER BY " + TABLE_POSTS + "." + COLUMN_CREATED_AT + " DESC", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy các thông tin cơ bản của bài đăng
                    String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TITLE));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_CONTENT));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                    // Lấy ID của bài đăng
                    int postId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POST_ID));
                    int isRecipe = cursor.getInt(cursor.getColumnIndexOrThrow(IS_RECIPE));
                    // Kiểm tra trạng thái like của bài viết
                    boolean isLiked = cursor.getInt(cursor.getColumnIndexOrThrow("isLiked")) == 1;

                    List<String> imageUrls = null;
                    Post post = new Post(userId, postId, avatarUrl, username, date, title, content, imageUrls, isRecipe, new ArrayList<>());
                    post.setLiked(isLiked); // Cập nhật trạng thái like của bài viết

                    // Tạo một danh sách để lưu trữ các URL hình ảnh cho bài đăng
                    imageUrls = new ArrayList<>();

                    // Truy vấn để lấy tất cả các URL hình ảnh từ bảng PHOTOS tương ứng với postId
                    Cursor photoCursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + COLUMN_POST_ID + " = ?", new String[]{String.valueOf(postId)});
                    while (photoCursor.moveToNext()) {
                        String imageUrl = photoCursor.getString(photoCursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                        // Thêm URL hình ảnh vào danh sách
                        imageUrls.add(imageUrl);
                    }
                    photoCursor.close();

                    // Gán danh sách URL hình ảnh cho bài đăng
                    post.setImageUrls(imageUrls);
                    // Lấy danh sách bình luận cho bài đăng
                    List<Comment> comments = new ArrayList<>();
                    Cursor commentCursor = db.rawQuery("SELECT DISTINCT " +
                            TABLE_USERS + "." + AVATAR_IMAGE + ", " +
                            TABLE_COMMENTS + ".*, " +
                            TABLE_USERS + "." + COLUMN_USERNAME + " " +
                            "FROM " + TABLE_COMMENTS + " " +
                            "JOIN " + TABLE_USERS + " ON " +
                            TABLE_COMMENTS + "." + COLUMN_USER_ID + " = " +
                            TABLE_USERS + "." + COLUMN_USER_ID + " " +
                            "WHERE " + COLUMN_POST_ID + " = ? " + " ORDER BY " + COLUMN_CREATED_AT + " DESC", new String[]{String.valueOf(postId)});
                    while (commentCursor.moveToNext()) {
                        String commentAvatarUrl = commentCursor.getString(commentCursor.getColumnIndexOrThrow(AVATAR_IMAGE));
                        String commentUsername = commentCursor.getString(commentCursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                        String commentContent = commentCursor.getString(commentCursor.getColumnIndexOrThrow(COLUMN_COMMENT_CONTENT));

                        Comment comment = new Comment(commentAvatarUrl, commentUsername, commentContent);
                        comments.add(comment);
                    }
                    commentCursor.close();
                    // Gán danh sách bình luận cho bài đăng
                    post.setComments(comments);
                    // Thêm đối tượng Post vào danh sách
                    if (!posts.contains(post)) {
                        posts.add(post);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return posts;
    }

}
