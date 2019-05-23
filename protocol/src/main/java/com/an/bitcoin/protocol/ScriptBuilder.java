package com.an.bitcoin.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ScriptBuilder
 * @Description ScriptBuilder
 * @Author an
 * @Date 2019/5/21 上午10:47
 * @Version 1.0
 */
public class ScriptBuilder {

    private List<Script.Element> elements = new ArrayList<>();


    public Script build() {
        return new Script(elements);
    }


    public ScriptBuilder append(Script.Element element) {
        elements.add(element);
        return this;
    }


}
