package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.adapterPostMenu2;
import com.example.myapplication.data.DatabaseHelper;
import com.example.myapplication.model.CircleTransform;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Menu2Fragment extends Fragment implements adapterPostMenu2.OnPostClickListener {
    private RecyclerView recyclerView;
    private adapterPostMenu2 adapter;
    private List<Post> postList;
    private DatabaseHelper databaseHelper;
    private User user;
//    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu2fragment, container, false);

//        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("user");
            ImageView imgviewMAvt = view.findViewById(R.id.imageViewMyAvt);
            if (user != null && user.getAvatarImage() != null && !user.getAvatarImage().isEmpty()) {
                Picasso.get().load(user.getAvatarImage())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.custom_border2)
                        .error(R.drawable.custom_border2)
                        .into(imgviewMAvt);
            } else {
                // Nếu không có đường dẫn ảnh hoặc đường dẫn rỗng, hiển thị ảnh mặc định từ thư mục drawable
                imgviewMAvt.setImageResource(R.drawable.user_icon2);
            }
        }

        RelativeLayout topRelativeLayout = view.findViewById(R.id.topRelativeLayout);
        topRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                User user = (User) bundle.getSerializable("user");
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getActivity());
        adapter = new adapterPostMenu2(getActivity(), postList, this, user, databaseHelper); // Gán listener
        recyclerView.setAdapter(adapter);
        retrievePostsFromDatabase();

        return view;
    }
    public void onResume() {
        super.onResume();
        retrievePostsFromDatabase();
//        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void retrievePostsFromDatabase() {
        postList.clear();
        postList.addAll(databaseHelper.getAllPosts());
        for (Post post : postList) {
            boolean isLiked = databaseHelper.isPostLikedByUser(post.getPostId(), user.getUserId());
            post.setLiked(isLiked);
            if(isLiked) {
                Log.e("post", "liked: " + post.getPostId());
            }
        }
        adapter.notifyDataSetChanged();

    }

    // Phương thức trong interface OnPostClickListener
    @Override
    public void onPostClick(int position) {
        // Lấy đối tượng Post tại vị trí được nhấn
        Post clickedPost = postList.get(position);
        int postId = clickedPost.getPostId();
        // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post
        Intent intent = new Intent(getActivity(), PostDetail.class);
        intent.putExtra("POST_DETAIL", clickedPost);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void onCommentClick(int position) {
        // Lấy đối tượng Post tại vị trí được nhấn
        Post clickedPost = postList.get(position);
        int postId = clickedPost.getPostId();
        // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post cũng như flag để chỉ định cuộn đến phần comment
        Intent intent = new Intent(getActivity(), PostDetail.class);
        intent.putExtra("POST_DETAIL", clickedPost);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("SCROLL_TO_COMMENT", true); // Gửi flag để báo cho PostDetail cuộn đến phần comment
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
