package com.example.user.demo.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.demo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 02.02.2016.
 */
public class VideoFragment extends BaseFragment {


    ProgressBar progressBar;
    ListView musicList;
    Button btnPlay, btnPause, btnStop;
    TextView playingMusicName;
//    final String MP3_PATH = "/sdcard/MP3Downloads/"; // 파일 경로 지정

    final String MP3_PATH = "/sdcard";


    ArrayList<File> musicFiles = new ArrayList<File>();
    ArrayList<String> musicNames = new ArrayList<String>();


    //ArrayList<String> songs = new ArrayList<String>();
    String selectedSong = "";
    MediaPlayer mPlayer;
    ArrayAdapter<String> adapter;

    //    File[] listFiles;
    String mRoot = "";
    String mPath = "";


    int selectedIndex = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        musicList = (ListView) view.findViewById(R.id.musicList);
        btnPlay = (Button) view.findViewById(R.id.playBtn);
        btnPause = (Button) view.findViewById(R.id.pauseBtn);
        btnStop = (Button) view.findViewById(R.id.stopBtn);
        playingMusicName = (TextView) view.findViewById(R.id.playingMusicName);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, musicNames);
        musicList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        musicList.setAdapter(adapter);
        musicList.setItemChecked(0, true);

        selectMusicFile(MP3_PATH);

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String strItem = songs.get(position);
//                String strPath = getAbsolutePath(strItem);
//                String[] fileList = getFileList(strPath);
//                File fileRoot = new File(strPath);

                String strItem = musicNames.get(position);
                String strPath = getAbsolutePath(strItem);
                String[] fileList = getFileList(strPath);
                File fileRoot = new File(strPath);


                //Toast.makeText(getApplicationContext(), strPath, Toast.LENGTH_SHORT).show();

//                if (fileRoot.isDirectory() != true) {
//                    if (strItem.endsWith(".mp3") || strItem.endsWith(".MP3") || strItem.endsWith(".mp4")) {
//                        //songs.add(fileRoot.getName());
//                    }
//                }

                //Toast.makeText(getApplicationContext(), songs.get(3), Toast.LENGTH_LONG).show();

//                selectedSong = songs.get(position);
//                fileList2Array(fileList);


                selectedSong = musicNames.get(position);
                selectedIndex = musicNames.indexOf(selectedSong);


                try {
                    //mPlayer = new MediaPlayer();
                    mPlayer.reset();
//                    mPlayer.setDataSource(MP3_PATH + selectedSong);
                    mPlayer.setDataSource(musicFiles.get(selectedIndex).getPath());
                    mPlayer.prepare();
                    mPlayer.start();
                    //btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    playingMusicName.setText(selectedSong);
                } catch (IOException e) {
                }

//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri uri = Uri.parse("file://"+musicFiles.get(selectedIndex).getPath());
//                intent.setDataAndType(uri, "audio/mp3");
//                startActivity(intent);


                playingMusicName.setText(selectedSong);


            }
        });

//        selectedSong = songs.get(0);

        selectedSong = musicNames.get(0);

        mPlayer = new MediaPlayer();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //mPlayer = new MediaPlayer();
                    mPlayer.reset();
//                    mPlayer.setDataSource(MP3_PATH + selectedSong);
                    mPlayer.setDataSource(musicFiles.get(selectedIndex).getPath());
                    mPlayer.prepare();
                    mPlayer.start();
                    //btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    playingMusicName.setText(selectedSong);
                } catch (IOException e) {
                }


//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri uri = Uri.parse("file://"+musicFiles.get(selectedIndex).getPath());
//                intent.setDataAndType(uri, "audio/mp3");
//                startActivity(intent);
//

//                Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);


            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.pause();
                btnPlay.setClickable(true);
                btnStop.setClickable(true);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.reset();
                btnPlay.setClickable(true);
                btnStop.setClickable(false);
                playingMusicName.setText("");
            }
        });
        btnStop.setClickable(false);


        return view;
    }

    @Override
    protected int getLayout()  {
        return R.layout.fragment_video;
    }


    public String getAbsolutePath(String strFolder) {
        String strPath;

        // 이전 폴더일때

        if (strFolder == "..") {

            // 전체 경로에서 최하위 폴더를 제거

            int pos = mPath.lastIndexOf("/");

            strPath = mPath.substring(0, pos);

        } else

            strPath = mPath + "/" + strFolder;

        return strPath;
    }

    public String[] getFileList(String strPath) {

        // 폴더 경로를 지정해서 File 객체 생성

        File fileRoot = new File(strPath);

        // 해당 경로가 폴더가 아니라면 함수 탈출

        if (fileRoot.isDirectory() == false)

            return null;

        mPath = strPath;

        // 파일 목록을 구한다

        String[] fileList = fileRoot.list();

        return fileList;

    }

//    public void fileList2Array(String[] fileList) {   //ㅋㅋㅋ
//
//        if( fileList == null )
//
//            return;
//
//        songs.clear();
//
//        // 현재 선택된 폴더가 루트 폴더가 아니라면
//
//        if( mRoot.length() < mPath.length() )
//
//            // 이전 폴더로 이동하기 위해서 ListView에 ".."항목을 추가
//
//            songs.add("..");
//        for(int i=0; i < fileList.length; i++) {
//
//            Log.d("tag", fileList[i]);
//
//            songs.add(fileList[i]);
//        }
//        ArrayAdapter adapter = (ArrayAdapter)musicList.getAdapter();
//        adapter.notifyDataSetChanged();
//    }


    public void selectMusicFile(String source) {
        File dir = new File(source);

        File[] fileList = dir.listFiles();

        try {

            for (int i = 0; i < fileList.length; i++) {

                File file = fileList[i];

                if (file.isFile()) {
                    if (file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3") || file.getName().endsWith(".m4a")) {
                        musicFiles.add(file);
                        musicNames.add(file.getName());

                    }
                    // 파일이 있다면 파일 이름 출력
                } else if (file.isDirectory()) {

                    // 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색

                    selectMusicFile(file.getCanonicalPath().toString());
                }

            }

        } catch (IOException e) {


        }

    }


}