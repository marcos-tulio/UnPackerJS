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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marcos Santos
 */
public class UnPackerJS {

    /**
     * Forbidden characters
     */
    private final Collection finalChar = new ArrayList(
            Arrays.asList(" ", "=", "\"", ";", ":", "/", "&", ".", "?", "$", "(", ")", "<", ">", "'",
                    ",", "{", "}", "-", (char) 0x5C, "\\", "[", "]", "#", "+", "*", "!", "%", "|", "@")
    );

    /**
     * Base62 instance.
     */
    private final Base62 base62;

    /**
     * Constructs that create a base62 instance.
     */
    public UnPackerJS() {
        base62 = new Base62();
    }

    /**
     * Constructs with your base62 instance.
     *
     * @param base62 Your Base62 instance (Andreas Holley`s code).
     */
    public UnPackerJS(Base62 base62) {
        this.base62 = base62;
    }

    /**
     * Dictionary that contains all words.
     */
    private final Map<String, String> words = new HashMap<>();

    /**
     * Unpack a javascript function that has been compressed into a base62
     * algorithm by Dean Edwards.
     *
     * @param packed Packed function like as eval(...) or
     * function(p,a,c,k,e,r){...}.
     * @return Unpacked function.
     */
    public String unPackerBase62(String packed) {
        packed = removeStr(packed, "return p}(");      // Select only parameter values

        String p, k;
        int c = 0;                                      // Dictionary size 

        p = packed.substring(packed.indexOf("\'") + 1); // p
        p = p.substring(0, p.indexOf("',"));

        packed = removeStr(packed, "',");               // a 
        packed = removeStr(packed, ",");                // c 

        try {
            c = Integer.parseInt(packed.substring(0, packed.indexOf(",")));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Can not possible convert parameter c!");
        }

        packed = removeStr(packed, ",");                // k
        k = packed.substring(0, packed.indexOf(".split")).replaceAll("\'", "").concat("|");

        // Create a map with all alphabet characters combinations 
        for (int i = 0; i < c; i++) {
            if (k.contains("|")) {
                words.put(
                        base62.encodeBase10(i), // Encode actual position
                        k.substring(0, k.indexOf("|")) // Add word in database
                );

                k = removeStr(k, "|");                 // Remove word added
            }
        }

        StringBuilder unpacked = new StringBuilder();
        StringBuilder back = new StringBuilder();
        String actual = "";

        // Unpacker script
        for (int i = 0; i < p.length(); i++) {
            actual = p.substring(i, i + 1);             // 'actual' receive one letter

            if (isFinalChar(actual)) {                  // is 'actual' an unpacked character? 
                if (!back.toString().isEmpty()) {       // last letter is not empty?
                    if (!words.containsKey(back.toString())
                            || words.get(back.toString()).isEmpty()) { // Is a valid code? not...

                        unpacked.append(back.toString());            // Add unpacked letter
                    } else
                        unpacked.append(words.get(back.toString())); // Add word in unpacked string

                    back.delete(0, back.length());          // Clear backup variable
                }

                unpacked.append(actual);                // Add character not packed in unpacked string                
            } else
                back.append(actual);                    // Save in 'back' the last letter
        }

        words.clear();                                  // Clear hash map

        // Format ' and " 
        return unpacked.toString().replace("\\\'", "'").replace("\\\\\"", "\"");
    }

    /**
     * Remove a string at the key.
     *
     * @param str Remove here.
     * @param part Remove this.
     * @return String without part.
     */
    private String removeStr(String str, String part) {
        return str.substring(str.indexOf(part) + part.length());
    }

    /**
     * Is a forbidden character?
     */
    private boolean isFinalChar(String letter) {
        return finalChar.contains(letter);
    }
}
