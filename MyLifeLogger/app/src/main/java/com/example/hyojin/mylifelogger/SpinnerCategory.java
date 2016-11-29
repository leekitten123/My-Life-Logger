package com.example.hyojin.mylifelogger;

public class SpinnerCategory {
    String resultStr = "" ;

    public String CategoryToString(int i) {
        switch(i) {
            case 0:
                resultStr = "게임";
                break;
            case 1:
                resultStr = "식사";
                break;
            case 2:
                resultStr = "이동";
                break;
            case 3:
                resultStr = "공부";
                break;
            case 4:
                resultStr = "일";
                break;
            case 5:
                resultStr = "그외";
                break;
        }

        return resultStr ;
    }
}
