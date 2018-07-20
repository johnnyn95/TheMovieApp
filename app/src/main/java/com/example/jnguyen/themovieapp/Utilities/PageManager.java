package com.example.jnguyen.themovieapp.Utilities;

public class PageManager {

    private int TotalPageCount;
    private int CurrentPage;

    public PageManager(int totalPageCount){
        TotalPageCount = totalPageCount;
    }

    public int nextPage(){
        if(CurrentPage < TotalPageCount) {
            CurrentPage++;
        }
        return  CurrentPage;
    }
}
