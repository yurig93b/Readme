package com.ariel.readme.understand;

import java.util.ArrayList;
import java.util.List;

class MewoHello<T>{

}

public class MyTest {

    void t(List<? super Object> src){
          src.add(new BJ());
    }

    void fff(){
        List<Object> aaa = new ArrayList<>();

       t(aaa);
       Object me = aaa.get(0);
       me = aaa.get(0);


    }
}
