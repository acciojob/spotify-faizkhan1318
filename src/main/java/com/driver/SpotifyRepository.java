package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User currUser : users){
            if(currUser.getMobile().equals(mobile)){
                return currUser;
            }
        }
        User newUser = new User(name, mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist artist : artists){
            if(artist.getName().equals(name)){
                return artist;
            }
        }
        Artist newArtist = new Artist(name);
        artists.add(newArtist);
        return newArtist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title)){
                return album;
            }
        }
        Album newAlbum = new Album(title);
        albums.add(newAlbum);

        List<Album> alb = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            alb = artistAlbumMap.get(artist);
        }
        alb.add(newAlbum);
        artistAlbumMap.put(artist, alb);
        return newAlbum;

    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean isAlbumPresent = false;
        Album album = new Album();
        for(Album currAlbum : albums){
            if(currAlbum.getTitle().equals(albumName)){
                album=currAlbum;
                isAlbumPresent=true;
                break;
            }
        }
        if(isAlbumPresent==false){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title, length);
        songs.add(song);
        List<Song> listSong = new ArrayList<>();
        if(albumSongMap.containsKey(title)){
            listSong = albumSongMap.get(album);
        }
        listSong.add(song);
        albumSongMap.put(album, listSong);
        return song;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title)){
                return playlist;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> songList = new ArrayList<>();
        for(Song song :songs){
            if(song.getLength()==length){
                songList.add(song);
            }
        }
        playlistSongMap.put(playlist, songList);
        boolean isUserPresent = false;
        User curUser = new User();
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                isUserPresent=true;
                break;
            }
        }
        if(isUserPresent==false){
            throw new Exception("User does not exist");
        }

        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        userList.add(curUser);
        playlistListenerMap.put(playlist, userList);
        creatorPlaylistMap.put(curUser, playlist);

        List<Playlist> playlistList = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            playlistList = userPlaylistMap.get(curUser);
        }
        playlistList.add(playlist);
        userPlaylistMap.put(curUser, playlistList);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title)){
                return playlist;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> songList = new ArrayList<>();
        for(Song song :songs){
            if(songTitles.contains(song.getTitle())){
                songList.add(song);
            }
        }
        playlistSongMap.put(playlist, songList);
        boolean isUserPresent = false;
        User curUser = new User();
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                isUserPresent=true;
                break;
            }
        }
        if(isUserPresent==false){
            throw new Exception("User does not exist");
        }

        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        userList.add(curUser);
        playlistListenerMap.put(playlist, userList);
        creatorPlaylistMap.put(curUser, playlist);

        List<Playlist> playlistList = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            playlistList = userPlaylistMap.get(curUser);
        }
        playlistList.add(playlist);
        userPlaylistMap.put(curUser, playlistList);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean isPlaylistPresnt = false;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist : playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                isPlaylistPresnt=true;
                break;
            }
        }
        if(isPlaylistPresnt==false){
            throw new Exception("Playlist does not exist");
        }
        User curUser = new User();
        boolean isUserPresent =false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                isUserPresent=true;
                break;
            }
        }
        if(isUserPresent==false){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        if(creatorPlaylistMap.get(curUser)!=playlist){
            creatorPlaylistMap.put(curUser, playlist);
        }
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curUser = new User();
        boolean isUserPresent = false;
        for(User user : users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                isUserPresent=true;
                break;
            }
        }
        if(isUserPresent==false){
            throw new Exception("User does not exist");
        }
        Song song =new Song();
        boolean isSongPresent=false;
        for(Song curSong:songs){
            if(curSong.getTitle().equals(songTitle)){
                song=curSong;
                isSongPresent=true;
                break;
            }
        }
        if(isSongPresent==false){
            throw new Exception("Song does not exist");
        }
        List<User> listUser = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            listUser=songLikeMap.get(song);
        }
        //public HashMap<Song, List<User>> songLikeMap;
        if(!listUser.contains(curUser)){
            listUser.add(curUser);
            songLikeMap.put(song, listUser);
            song.setLikes(song.getLikes()+1);

            //public HashMap<Album, List<Song>> albumSongMap;

            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album = curAlbum;
                    break;
                }
            }
            //public HashMap<Artist, List<Album>> artistAlbumMap;
            Artist artist = new Artist();
            for(Artist curArtist:artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Artist art : artists){
            maxLikes= Math.max(maxLikes,art.getLikes());
        }
        for(Artist art : artists){
            if(maxLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;

    }

    public String mostPopularSong() {
        String name = "";
        int maxLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLikes = Math.max(maxLikes, song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes==song.getLikes()){
                name = song.getTitle();
            }
        }
        return name;
    }
}
