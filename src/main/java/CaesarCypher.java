import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class CaesarCypher {
    //init alphabet
    public ArrayList<Character> charsList = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ',', '.', '/', '<', '>', ':', ';', '"', '[', ']', '{', '}', '(', ')', '\\', '|', '!', '?', '@', '#', '$', '%', '^', '&', '*', '-', '_', '=', '+','\'',' '));
    //init seed used to randomize the alphabet
    public long seed;
    //default shift amount if no other is specified
    public int shiftAmount;
    public CaesarCypher(int seed, int shiftAmount){
        this.seed = seed;
        this.shiftAmount = shiftAmount;
    }
    public CaesarCypher(int seed){
        this.seed = seed;
        this.shiftAmount = 3;
    }
    public CaesarCypher(String fileName) throws IOException, ParseException {
        loadConfiguration(fileName);
        if(this.shiftAmount == 0){
            this.shiftAmount = 3;
        }
    }
    //Encrypts a string using the alphabet above (to make it harder to decrypt)
    public String encryptCaesar(String s,int shiftAmount){
        char[] chars = s.toCharArray();
        ArrayList<Integer> charNums = new ArrayList<>();
        ArrayList<Character> finalNum = new ArrayList<>();
        String finalString = "";
        //Get the index of all the chars from the string s
        for (Character character: chars){
            charNums.add(charsList.indexOf(character));
        }
        // shift the index by the shift amount and checking if the index is out of bounds of the alphabet
        for (Integer index: charNums) {
            int newIndex = index-shiftAmount;
            while(newIndex < 0) {
                newIndex *= -1;
                newIndex = charsList.size() - newIndex;
            }
            //converting the index into a list of chars
            finalNum.add(charsList.get(newIndex));
        }
        // converting the charlist into an actual string
        for (Character finalNumChar: finalNum) {
            finalString = finalString + finalNumChar;
        }
        return finalString;
    }
    public String unencryptCaesar(String s,int shiftAmount){
        char[] chars = s.toCharArray();
        ArrayList<Integer> charNums = new ArrayList<>();
        ArrayList<Character> finalNum = new ArrayList<>();
        String finalString = "";
        //converting to an array of indexs
        for (Character character: chars){
            charNums.add(charsList.indexOf(character));
        }
        for (Integer index: charNums) {
            //shifting the indexs the opposing way
            int newIndex = index+shiftAmount;
            //checking if it goes out of bounds
            while(newIndex > charsList.size()) {
                newIndex = newIndex-charsList.size();
            }
            finalNum.add(charsList.get(newIndex));
        }
        //converting to a string
        for (Character finalNumChar: finalNum) {
            finalString = finalString + finalNumChar;
        }
        return finalString;
    }
    public String unencryptCaesar(String s){
        char[] chars = s.toCharArray();
        ArrayList<Integer> charNums = new ArrayList<>();
        ArrayList<Character> finalNum = new ArrayList<>();
        String finalString = "";
        //converting to an array of indexs
        for (Character character: chars){
            charNums.add(charsList.indexOf(character));
        }
        for (Integer index: charNums) {
            //shifting the indexs the opposing way
            int newIndex = index+shiftAmount;
            //checking if it goes out of bounds
            while(newIndex >= charsList.size()) {
                newIndex = newIndex-charsList.size();
            }
            finalNum.add(charsList.get(newIndex));
        }
        //converting to a string
        for (Character finalNumChar: finalNum) {
            finalString = finalString + finalNumChar;
        }
        return finalString;
    }
    //randomize list using seed
    public void randomizeList(int seed){
        //init temp list which will then become the final
        ArrayList<Character> tempList = new ArrayList<Character>();
        while(!charsList.isEmpty()) {
            Random randomGen = new Random(seed);
            int randInt = randomGen.nextInt(charsList.size());
            charsList.remove(randInt);
            tempList.add(charsList.get(randInt));
            //the number generated is now used as the seed to make the same formation of numberes, it doesn't matter what is generated, only that it can make the same thing everytime
            seed = randInt;
        }
        charsList = tempList;
    }
    public void randomizeList(){
        ArrayList<Character> tempList = new ArrayList<>();
        long tempSeed = seed;
        while(!charsList.isEmpty()) {
            Random randomGen = new Random(tempSeed);
            int randInt = randomGen.nextInt(charsList.size());
            tempList.add(charsList.get(randInt));
            charsList.remove(randInt);
            tempSeed = randInt;
        }
        charsList = tempList;
    }
    public void saveConfiguration(String fileName) throws IOException {
        JSONObject config = new JSONObject();
        JSONArray charlist = new JSONArray();
        for (int i = 0; i < charsList.size(); i++) {
            charlist.add(charsList.get(i).toString());
        }
        config.put("Alphabet",charlist);
        config.put("seed",seed);
        config.put("shiftAmountDefault",shiftAmount);
        FileWriter file = new FileWriter("configs/" + fileName + ".json");
        file.write(config.toString());
        file.flush();
    }
    public void loadConfiguration(String fileName) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("configs/"+fileName+".json"));
        String fr = br.readLine();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(fr);
        JSONObject jobj = (JSONObject) obj;
        ArrayList tempList = (ArrayList) jobj.get("Alphabet");
        this.charsList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            charsList.add(String.valueOf(tempList.get(i)).charAt(0));
        }
        this.seed = (long) jobj.get("seed");
        this.shiftAmount = Integer.parseInt(jobj.get("shiftAmountDefault").toString());
    }
}
