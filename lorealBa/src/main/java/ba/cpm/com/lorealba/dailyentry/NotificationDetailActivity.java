package ba.cpm.com.lorealba.dailyentry;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import ba.cpm.com.lorealba.R;

import static java.security.AccessController.getContext;

public class NotificationDetailActivity extends AppCompatActivity {

    TextView tv_title_text, tv_title_img, tv_title_video, tv_message;
    ImageView imgview;
    VideoView videoView;
    String title, body, path, type;

    CardView card_text, card_image, card_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        type = getIntent().getStringExtra("Type");
        title = getIntent().getStringExtra("Title");
        body = getIntent().getStringExtra("Body");
        path = getIntent().getStringExtra("Path");

        tv_title_text = (TextView) findViewById(R.id.tv_title) ;
        tv_title_img = (TextView) findViewById(R.id.tv_title_pic) ;
        tv_title_video = (TextView) findViewById(R.id.tv_title_video) ;
        tv_message = (TextView) findViewById(R.id.tv_message) ;
        card_text = (CardView) findViewById(R.id.parent_text_msg) ;
        card_image = (CardView) findViewById(R.id.parent_img) ;
        card_video = (CardView) findViewById(R.id.parent_video) ;

        imgview = (ImageView) findViewById(R.id.img_notification) ;
        videoView = (VideoView) findViewById(R.id.video_view);

        //if(type!=null && type.equalsIgnoreCase("")){
        if(type==null){
            card_text.setVisibility(View.VISIBLE);
            tv_title_text.setText(title);
            tv_message.setText(body);
        }
        else if(type!=null && type.equalsIgnoreCase("Image")){
            card_image.setVisibility(View.VISIBLE);
            tv_title_img.setText(title);

            //Picasso.with(getApplicationContext()).load(path).fit().into(imgview);
            //Picasso.with(NotificationDetailActivity.this).load("http://i.imgur.com/DvpvklR.png").into(imgview);

            Glide.with(this)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgview);

        }else if(type!=null && type.equalsIgnoreCase("Video")){
            card_video.setVisibility(View.VISIBLE);
            tv_title_video.setText(title);

            videoView.setVideoPath(path);
            videoView.start();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
