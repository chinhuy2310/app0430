package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.PostDetail;
import com.example.foodapp.R;
import com.example.foodapp.adapter.adapterPostMenu2;
import com.example.foodapp.data.DatabaseHelper;
import com.example.foodapp.model.CircleTransform;
import com.example.foodapp.model.Post;
import com.example.foodapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Menu2Fragment extends Fragment implements adapterPostMenu2.OnPostClickListener {
    private RecyclerView recyclerView;
    private adapterPostMenu2 adapter;
    private List<Post> postList;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu2fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            User user = (User) bundle.getSerializable("user");
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
        adapter = new adapterPostMenu2(getActivity(), postList, this); // Gán listener
        recyclerView.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(getActivity());
        retrievePostsFromDatabase();

        return view;
    }
    public void onResume() {
        super.onResume();
        retrievePostsFromDatabase();
    }

    private void retrievePostsFromDatabase() {
        postList.clear();
        postList.addAll(databaseHelper.getAllPosts());
        adapter.notifyDataSetChanged();
    }

    // Phương thức trong interface OnPostClickListener
    @Override
    public void onPostClick(int position) {
        // Lấy đối tượng Post tại vị trí được nhấn
        Post clickedPost = postList.get(position);

        // Gửi Intent đến PostDetail Activity và đính kèm đối tượng Post
        Intent intent = new Intent(getActivity(), PostDetail.class);
        intent.putExtra("POST_DETAIL", clickedPost);
        startActivity(intent);
    }

}
