package com.example.lixiang.threekingdoms;

public class MessageEvent {
    private CharacterInfo Info;
    public MessageEvent(CharacterInfo Item){
        this.Info = Item;
    }
    public CharacterInfo getCharacterInfo(){
        return Info;
    }
}
