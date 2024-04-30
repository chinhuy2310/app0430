package com.example.foodapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.example.foodapp.adapter.adapterPost;
//import com.example.foodapp.data.DatabaseAll;
//import com.example.foodapp.model.Truyen;


public class Menu3Fragment extends Fragment {
//    adapterPost adaptertruyen;
//    ListView listViewNew;
//    ArrayList<Truyen> TruyenArrayList;
//    DatabaseAll databaseDocTruyen;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.menu3fragment, container, false);
//        databaseDocTruyen = new DatabaseAll(requireContext());
//
//        Mapping(view);
//        //listview Truyen
//        listViewNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(requireActivity(), Content.class);
//                String tent =   TruyenArrayList.get(position).getTenTruyen();
//                String noidungt = TruyenArrayList.get(position).getNoiDung();
//                intent.putExtra("postname",tent);
//                intent.putExtra("content",noidungt);
//                startActivity(intent);
//            }
//        });
        return view;
    }
    private void Mapping(View view){

//        listViewNew = view.findViewById(R.id.listviewNew);
//
//        //New Post
//        TruyenArrayList = new ArrayList<>();
//
//        Cursor cursor1 = databaseDocTruyen.getData3();
//        while (cursor1.moveToNext()){
//            int id = cursor1.getInt(0);
//            String postname = cursor1.getString(1);
//            String content = cursor1.getString(2);
//            String anh = cursor1.getString(3);
//            int id_tk = cursor1.getInt(4);
//            TruyenArrayList.add(new Truyen(id,postname,content,anh,id_tk));
//        }
//        cursor1.close();
//
//        adaptertruyen = new adapterPost(requireContext(), TruyenArrayList);
//        listViewNew.setAdapter(adaptertruyen);
    }

}
