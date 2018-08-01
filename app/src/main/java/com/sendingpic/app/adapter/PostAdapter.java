package com.sendingpic.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.helper.PrivacidadeTipoEnum;
import com.sendingpic.app.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {

    private ArrayList<Post> posts;
    private Context context;

    private ImageView image_post;
    private TextView date_post;
    private TextView tipo_post;


    public PostAdapter(Context context, ArrayList<Post> objects){
        super(context, 0, objects);
        this.context = context;
        this.posts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (posts != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.post_list, parent, false);

            image_post = view.findViewById(R.id.post_image);
            date_post = view.findViewById(R.id.date_post);
            tipo_post = view.findViewById(R.id.tipo_post);

            final Post post = posts.get(position);

            PrivacidadeTipoEnum privacidade = post.getPrivacidadeTipo();

            Picasso.with(context).load(post.getImageUrl()).into(image_post);
            date_post.setText(post.getDataPublicacao());
            tipo_post.setText(privacidade.getDescricao());
        }


        return view;
    }
}
