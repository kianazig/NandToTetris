import java.util.HashMap;

public class SymbolTable {
	
	HashMap<String, Integer> symbols;
	
	public SymbolTable(){
		symbols = new HashMap<String, Integer>();
	}
	
	public void addEntry(String symbol, int address){
		symbols.put(symbol, address);
	}
	
	public boolean contains(String symbol) {
		return symbols.containsKey(symbol);
	}
	
	public int getAddress(String symbol) {
		return symbols.get(symbol);
	}
}
