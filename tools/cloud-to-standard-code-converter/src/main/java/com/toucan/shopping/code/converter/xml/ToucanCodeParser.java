package com.toucan.shopping.code.converter.xml;

import com.toucan.shopping.code.converter.vo.ToucanCode;

public class ToucanCodeParser {

    private static final ToucanCodeParser instance = new ToucanCodeParser();

    private ToucanCodeParser()
    {

    }

    public ToucanCodeParser inst()
    {
        return instance;
    }

    public ToucanCode parse()
    {
        ToucanCode toucanCode = new ToucanCode();

        return toucanCode;
    }

}
