package com.huypo.tase.Model;

import com.huypo.tase.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListDetails {



    public static ArrayList<PersonalTable> getlist(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ArrayList<PersonalTable> personalTables = new ArrayList<>();
        //personalTables(image,String,String,Date)
        personalTables.add(new PersonalTable( "As a physical object, a book is a stack of usually rectangular pages (made of papyrus, parchment, vellum, or paper) oriented with one edge tied, sewn, or otherwise fixed together and then bound to the flexible"," The technical term for this physical arrangement is codex (in the plural, codices)", calendar.getTime()));
        personalTables.add(new PersonalTable("As a physical object, a book is a stack of usually rectangular pages (made of papyrus, parchment, vellum, or paper) oriented with one edge tied, sewn, or otherwise fixed together and then bound to the flexible"," The technical term for this physical arrangement is codex (in the plural, codices)", calendar.getTime()));
        personalTables.add(new PersonalTable("As a physical object, a book is a stack of usually rectangular pages (made of papyrus, parchment, vellum, or paper) oriented with one edge tied, sewn, or otherwise fixed together and then bound to the flexible"," The technical term for this physical arrangement is codex (in the plural, codices)", calendar.getTime()));

        return personalTables;
    }
}
