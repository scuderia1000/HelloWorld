package com.example.valek.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Valek on 11.12.2016.
 */

public class BoxAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<User> users;

    public BoxAdapter(Context ctx, List<User> users) {
        this.ctx = ctx;
        this.users = users;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item, parent, false);
        }

        User user = getUser(position);
        ImageView avatarImageView = (ImageView) view.findViewById(R.id.imageView);

        ((TextView) view.findViewById(R.id.tvDescr)).setText(user.getLogin());

        if (!user.getAvatar().equals("")) {

            Picasso.with(ctx).load("https://chatclick.ru:8102/avatar/" + user.getAvatar())
                    .transform(new CircularTransformation())
                    .into(avatarImageView);
        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(user.getLogin().substring(0, 1), Color.RED);
            avatarImageView.setImageDrawable(drawable);
        }

        return view;
    }

    User getUser(int position) {
        return ((User) getItem(position));
    }
}


