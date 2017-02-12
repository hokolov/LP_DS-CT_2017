package DST.Seminar01;

import java.util.LinkedList;
import java.util.List;

public class LineParser {
	private String local;
    private List<String> parseLineComand;
    private int leng, i;

    public void pars(String inLine) {

        parseLineComand = new LinkedList<String>();
        leng = inLine.length();
        i = 0;
        while (i < leng && inLine.charAt(i) != '(') {
            i++;
        }
        parseLineComand.add(inLine.substring(0, i).trim());
        if (i != leng) {
            i++;
            while (inLine.charAt(i) == ' ' || inLine.charAt(i) == '(') {
                i++;
            }
            for (int j = i; j < leng; j++) {
                switch (inLine.charAt(j)) {
                    case ',':
                        local = inLine.substring(i, j).trim();
                        if (!local.isEmpty() && !local.equals(",")) {
                            parseLineComand.add(local);
                        }
                        i = j + 1;
                        break;
                    case ')':
                        local = inLine.substring(i, j).trim();
                        if (!local.isEmpty() && !local.equals(",")) {
                            parseLineComand.add(local);
                        }
                        if (j != leng) {
                            j++;
                            local = inLine.substring(j, leng).trim();
                            
                            if (!local.isEmpty()) {
                                parseLineComand.add(local);
                            }
                        }
                        j = leng;
                        break;
                }
            }
        }
        soutParseComand(parseLineComand);        
    }
//---------------------------------------------------------------------------------------------    
    public void soutParseComand(List<String> parsListComand) {
        this.parseLineComand = parsListComand;

        System.out.print("Entered command = “");
        for (int i = 0; i < parsListComand.size(); i++){
        	if (i == 0){
        		System.out.print(parsListComand.get(0) + "”, parameters = [");
        	} else {
        		System.out.print(parsListComand.get(i));

                if (i < parsListComand.size() - 1) {
                    System.out.print(",");
                }
        	}
        }
        System.out.println("]");
    }
}