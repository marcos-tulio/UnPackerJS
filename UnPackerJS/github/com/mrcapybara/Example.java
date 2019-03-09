/** *****************************************************************************
 * Copyright 2019 Marcos T. S. Martins (MrCapybara)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************** */
package github.com.mrcapybara;

/**
 *
 * @author Marcos Santos
 */
public class Example {

    public static void main(String[] args) {        
        UnPackerJS unPacker = new UnPackerJS();
        
        String packedCode = "eval(function(p,a,c,k,e,r){e=String;if(!''.replace(/^/,String))"
                + "{while(c--)r[c]=k[c]||c;k=[function(e){return r[e]}];"
                + "e=function(){return'\\\\w+'};c=1};while(c--)if(k[c])"
                + "p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c]);return p}"
                + "('1 0(){2(\"3!\")}0();',4,4,'unpacked|function|alert|MrCapybara'.split('|'),0,{}))";
        
        String unpackedCode = "";
        
        unpackedCode = unPacker.unPackerBase62(packedCode);
        
        System.out.println(unpackedCode);
    }
}
